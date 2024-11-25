package io.github.positionpal.notification.mom

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope

typealias QueueName = String

/**
 * A facade over the RabbitMQ client to interact with the RabbitMQ broker.
 * @property connection the connection to the RabbitMQ broker.
 */
internal class RabbitMQClient(private val connection: Connection) : AutoCloseable {

    override fun close() {
        closeConnection()
    }

    fun closeConnection() = connection.close()

    fun createChannel(): Channel = connection.createChannel()

    fun Channel.declareDirectExchange(name: String): Boolean =
        declareExchange(name, BuiltinExchangeType.DIRECT)

    fun Channel.declareHeadersExchange(name: String): Boolean =
        declareExchange(name, BuiltinExchangeType.HEADERS)

    private fun Channel.declareExchange(name: String, type: BuiltinExchangeType): Boolean {
        runCatching {
            // Every operation throwing an error closes the channel, so we need to create a new one
            createChannel().apply { exchangeDeclarePassive(name) }.close()
        }.fold(
            onSuccess = { return false },
            onFailure = {
                exchangeDeclare(name, type, true)
                return true
            },
        )
    }

    fun Channel.declareBindAndRegisterCallbackTo(
        exchange: String,
        callback: (AMQP.BasicProperties, ByteArray) -> Unit,
    ) = registerCallbackTo(declareAndBindQueueTo(exchange), callback)

    fun Channel.declareAndBindQueueTo(exchange: String, routingKey: String = ""): QueueName =
        queueDeclare().queue.also { queueBind(it, exchange, routingKey) }

    fun Channel.registerCallbackTo(queue: String, callback: (AMQP.BasicProperties, ByteArray) -> Unit) {
        basicConsume(
            queue,
            false, // Disable auto ack, so we can manually ack/nack messages after being processed
            object : DefaultConsumer(this) {
                override fun handleDelivery(
                    consumerTag: String,
                    envelope: Envelope,
                    properties: AMQP.BasicProperties,
                    body: ByteArray,
                ) = runCatching { callback(properties, body) }.fold(
                    onSuccess = { basicAck(envelope.deliveryTag, false) },
                    onFailure = { basicNack(envelope.deliveryTag, false, true) },
                )
            },
        )
    }
}
