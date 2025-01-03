package io.github.positionpal.notification.entrypoint

import io.github.positionpal.notification.application.tokens.impl.UsersTokensServiceImpl
import io.github.positionpal.notification.fcm.Firebase
import io.github.positionpal.notification.fcm.FirebaseCloudNotificationPublisher
import io.github.positionpal.notification.grpc.GrpcServer
import io.github.positionpal.notification.grpc.tokens.GrpcUsersTokensService
import io.github.positionpal.notification.mom.RabbitMQ
import io.github.positionpal.notification.mom.RabbitMQGroupsEventsConsumer
import io.github.positionpal.notification.mom.RabbitMQNotificationsConsumer
import io.github.positionpal.notification.storage.Postgres
import io.github.positionpal.notification.storage.groups.PostgresGroupsRepository
import io.github.positionpal.notification.storage.tokens.PostgresUsersTokensRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * The entry point of the service.
 */
object Launcher {

    /**
     * The main function of the application.
     * @param args The command line arguments.
     */
    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        val firebaseConfiguration = Firebase.Configuration(
            serviceAccountFilePath = env("FIREBASE_SERVICE_ACCOUNT_FILE_PATH"),
        )
        val firebase = Firebase.create(firebaseConfiguration).getOrThrow()
        val rabbitMqConfiguration = RabbitMQ.Configuration(
            host = env("RABBITMQ_HOST"),
            virtualHost = env("RABBITMQ_VIRTUAL_HOST"),
            port = env("RABBITMQ_PORT").toInt(),
            username = env("RABBITMQ_USERNAME"),
            password = env("RABBITMQ_PASSWORD"),
        )
        val postgresConfiguration = Postgres.Configuration(
            username = env("POSTGRES_USERNAME"),
            password = env("POSTGRES_PASSWORD"),
            host = env("POSTGRES_HOST"),
            port = env("POSTGRES_PORT").toInt(),
        )
        Postgres(postgresConfiguration).connect().getOrThrow()
        val groupsRepository = PostgresGroupsRepository()
        val usersTokensRepository = PostgresUsersTokensRepository()
        val usersTokensService = GrpcUsersTokensService(UsersTokensServiceImpl(usersTokensRepository))
        val grpcServerConfiguration = GrpcServer.Configuration(
            port = env("GRPC_PORT").toInt(),
            services = listOf(usersTokensService),
        )
        val grpcServer = GrpcServer(grpcServerConfiguration)
        val grpcService = launch { grpcServer.start() }
        val rabbitMqNotificationService = RabbitMQNotificationsConsumer(
            FirebaseCloudNotificationPublisher(firebase, usersTokensRepository, groupsRepository),
            rabbitMqConfiguration,
        )
        val rabbitMqGroupsService = RabbitMQGroupsEventsConsumer(groupsRepository, rabbitMqConfiguration)
        rabbitMqNotificationService.setup()
        rabbitMqGroupsService.setup()
        grpcService.join()
    }

    private fun env(key: String): String =
        System.getenv(key) ?: error("The required environment variable `$key` is not set!")
}
