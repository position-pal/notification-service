package io.github.positionpal.notification.storage

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

/**
 * A [ConnectionFactory] for Postgres SQL database.
 * @property databaseName the name of the database
 * @property username the username to connect to the database
 * @property password the password to connect to the database
 * @property host the host address of the database
 * @property port the port of the database
 */
class PostgresConnectionFactory(
    val databaseName: String,
    val username: String,
    val password: String,
    val host: String,
    val port: Int,
) : ConnectionFactory<Database> {

    override fun connect(): Result<Database> = runCatching {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://$host:$port/$databaseName"
            driverClassName = "org.postgresql.Driver"
            username = this@PostgresConnectionFactory.username
            password = this@PostgresConnectionFactory.password
            validate()
        }
        Database.connect(HikariDataSource(config))
    }
}
