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
import java.nio.file.Paths
import kotlin.io.path.pathString

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
        val rootProjectDir = Paths.get("").toAbsolutePath().normalize().parent
        val firebaseConfiguration = Firebase.Configuration(
            serviceAccountFilePath = rootProjectDir
                .resolve(System.getenv("FIREBASE_SERVICE_ACCOUNT_FILE_PATH"))
                .pathString,
        )
        val firebase = Firebase.create(firebaseConfiguration).getOrThrow()
        val rabbitMqConfiguration = RabbitMQ.Configuration(
            host = System.getenv("RABBITMQ_HOST"),
            virtualHost = System.getenv("RABBITMQ_VIRTUAL_HOST"),
            port = System.getenv("RABBITMQ_PORT").toInt(),
            username = System.getenv("RABBITMQ_USERNAME"),
            password = System.getenv("RABBITMQ_PASSWORD"),
        )
        val postgresConfiguration = Postgres.Configuration(
            username = System.getenv("POSTGRES_USERNAME"),
            password = System.getenv("POSTGRES_PASSWORD"),
            host = System.getenv("POSTGRES_HOST"),
            port = System.getenv("POSTGRES_PORT").toInt(),
        )
        Postgres(postgresConfiguration).connect().getOrThrow()
        val groupsRepository = PostgresGroupsRepository()
        val usersTokensRepository = PostgresUsersTokensRepository()
        val usersTokensService = GrpcUsersTokensService(UsersTokensServiceImpl(usersTokensRepository))
        val grpcServerConfiguration = GrpcServer.Configuration(
            port = System.getenv("GRPC_PORT").toInt(),
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
}
