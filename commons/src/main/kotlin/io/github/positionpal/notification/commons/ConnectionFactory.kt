package io.github.positionpal.notification.commons

/**
 * A generic factory of connections.
 * @param Connection the type of the connection.
 */
fun interface ConnectionFactory<Connection> {

    /**
     * Attempts to connect.
     * @return a [Result] with the connection if successful, an [Error] otherwise.
     */
    fun connect(): Result<Connection>
}
