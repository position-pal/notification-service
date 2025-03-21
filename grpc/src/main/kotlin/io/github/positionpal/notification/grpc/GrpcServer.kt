package io.github.positionpal.notification.grpc

import io.grpc.BindableService
import io.grpc.Server
import io.grpc.ServerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/** A gRPC server instance, acting as entry point for managing gRPC services lifecycle. */
class GrpcServer(
    private val configuration: Configuration,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AutoCloseable {

    /**
     * Configuration for the gRPC server.
     * @property port The port where the server will listen for incoming connections.
     * @property services The list of services that the server will expose.
     * @property shutdownTimeout The maximum time to wait for the server to shut down. Default is `3000` milliseconds.
     */
    data class Configuration(
        val port: Int,
        val services: List<BindableService> = emptyList(),
        val shutdownTimeout: Long = 3_000L,
    )

    private lateinit var server: Server
    private val scope = CoroutineScope(dispatcher)

    /** Starts the gRPC server. */
    fun start() {
        require(!this::server.isInitialized) { "Server already started" }
        server = ServerBuilder.forPort(configuration.port).apply {
            configuration.services.forEach { addService(it) }
        }.build()
        scope.launch {
            server.start()
            Runtime.getRuntime().addShutdownHook(Thread { server.shutdown() })
            server.awaitTermination()
        }
    }

    /** Shuts down the gRPC server. */
    fun shutdown() {
        server.shutdown()
        if (!server.awaitTermination(configuration.shutdownTimeout, TimeUnit.MILLISECONDS)) {
            server.shutdownNow()
        }
    }

    override fun close() = shutdown()
}
