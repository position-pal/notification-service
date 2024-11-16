package io.github.positionpal.notification.storage

/**
 * A factory to create database connections.
 * @param Connection the type of the connection.
 */
fun interface ConnectionFactory<Connection> {

    /**
     * Attempts to connect to the database.
     * @return a [Result] with the connection if successful, an [Error] otherwise.
     */
    fun connect(): Result<Connection>
}
