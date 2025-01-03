package io.github.positionpal.notification.storage

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.positionpal.notification.commons.ConnectionFactory
import org.jetbrains.exposed.sql.Database

/**
 * A [ConnectionFactory] for a PostgresSQL database.
 */
class Postgres(private val configuration: Configuration) : ConnectionFactory<Database> {

    /**
     * The configuration to connect to the PostgresSQL database.
     * @property databaseName the name of the database. Default is `notifications_service`.
     * @property username the username to connect to the database
     * @property password the password to connect to the database
     * @property host the host address of the database
     * @property port the port of the database
     */
    data class Configuration(
        val databaseName: String = "notifications_service",
        val username: String,
        val password: String,
        val host: String,
        val port: Int,
    )

    override fun connect(): Result<Database> = runCatching {
        with(configuration) {
            val config = HikariConfig().also {
                it.jdbcUrl = "jdbc:postgresql://$host:$port/$databaseName"
                it.driverClassName = "org.postgresql.Driver"
                it.username = username
                it.password = password
                it.validate()
            }
            Database.connect(HikariDataSource(config))
        }
    }.onFailure { println("FAILED TO CONNECT: $it") }
}
