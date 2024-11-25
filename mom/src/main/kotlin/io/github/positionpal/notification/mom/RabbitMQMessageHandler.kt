package io.github.positionpal.notification.mom

import com.rabbitmq.client.Channel
import io.github.positionpal.notification.commons.flatMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A message handler for RabbitMQ messages.
 * @property configuration The configuration to connect to the RabbitMQ broker.
 */
abstract class RabbitMQMessageHandler(private val configuration: RabbitMQ.Configuration) : AutoCloseable {

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private lateinit var client: RabbitMQClient

    /** Set up the RabbitMQ connection and declare the exchange and queue. Here side effects occur. */
    fun setup() {
        require(!this::client.isInitialized) { "`setup` method can only be called once" }
        RabbitMQ(configuration).connect().flatMap { conn ->
            runCatching {
                client = RabbitMQClient(conn).apply {
                    onChannelCreated(createChannel())
                }
            }.onFailure { logger.error("Failed to setup RabbitMQ: {}", it.message) }
        }
    }

    internal abstract fun RabbitMQClient.onChannelCreated(channel: Channel)

    override fun close() = client.close()
}
