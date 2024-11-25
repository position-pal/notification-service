package io.github.positionpal.notification.mom

import com.rabbitmq.client.Connection
import io.github.positionpal.notification.commons.ConnectionFactory

/** The configuration to connect to the RabbitMQ broker. */
data class RabbitMQConfiguration(
    /** The host address of the RabbitMQ broker. */
    val host: String,
    /** The virtual host to connect to. */
    val virtualHost: String,
    /** The port of the RabbitMQ broker. */
    val port: Int,
    /** The username to connect to the RabbitMQ broker. */
    val username: String,
    /** The password to connect to the RabbitMQ broker. */
    val password: String,
)

internal class RabbitMQConnectionFactory(
    private val configuration: RabbitMQConfiguration,
) : ConnectionFactory<Connection> {

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
