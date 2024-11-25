package io.github.positionpal.notification.mom

import com.rabbitmq.client.AMQP

internal val localRabbitMQConfig = RabbitMQ.Configuration(
    host = "localhost",
    virtualHost = "/",
    port = 5672,
    username = "guest",
    password = "admin",
)

internal fun publish(exchange: String, messageType: String, msg: ByteArray) =
    RabbitMQ(localRabbitMQConfig).connect().onSuccess {
        with(RabbitMQClient(it)) {
            createChannel().use { ch ->
                ch.basicPublish(
                    exchange,
                    "",
                    AMQP.BasicProperties.Builder().headers(mapOf("message_type" to messageType)).build(),
                    msg,
                )
            }
        }
    }
