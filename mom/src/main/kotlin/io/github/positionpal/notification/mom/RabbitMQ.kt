package io.github.positionpal.notification.mom

import com.rabbitmq.client.Connection
import io.github.positionpal.notification.commons.ConnectionFactory

/**
 * A [ConnectionFactory] for a RabbitMQ message broker.
 */
class RabbitMQ(private val configuration: Configuration) : ConnectionFactory<Connection> {

    /**
     * The configuration to connect to the RabbitMQ broker.
     * @property host The host address of the RabbitMQ broker.
     * @property virtualHost The virtual host to connect to.
     * @property port The port of the RabbitMQ broker.
     * @property username The username to connect to the RabbitMQ broker.
     * @property password The password to connect to the RabbitMQ broker.
     */
    data class Configuration(
        val host: String,
        val virtualHost: String,
        val port: Int,
        val username: String,
        val password: String,
    )

    override fun connect(): Result<Connection> = runCatching {
        with(configuration) {
            com.rabbitmq.client.ConnectionFactory().also {
                it.username = username
                it.password = password
                it.virtualHost = virtualHost
                it.host = host
                it.port = port
            }.newConnection()
        }
    }
}
