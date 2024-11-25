package io.github.positionpal.notification.mom

import io.github.positionpal.AvroSerializer
import io.github.positionpal.commands.CommandType
import io.github.positionpal.notification.application.notifications.NotificationPublisher
import io.github.positionpal.notification.commons.flatMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * RabbitMQ message adapter for notification events.
 */
class RabbitMQNotificationAdapter(
    private val notificationPublisher: NotificationPublisher,
    private val configuration: RabbitMQConfiguration,
    private val scope: CoroutineScope,
) : AutoCloseable {

    private lateinit var client: RabbitMQClient
    private val serializer = AvroSerializer()

    /** Set up the RabbitMQ connection and declare the exchange and queue. Here side effects occur. */
    fun setup() = RabbitMQConnectionFactory(configuration).connect().flatMap { conn ->
        runCatching {
            client = RabbitMQClient(conn).apply {
                val channel = createChannel()
                channel.declareDirectExchange(PUSH_NOTIFICATIONS_EXCHANGE)
                channel.declareBindAndRegisterCallbackTo(PUSH_NOTIFICATIONS_EXCHANGE) { props, body ->
                    when (props.headers["message_type"].toString()) {
                        CommandType.GROUP_WISE_NOTIFICATION.name -> handleGroupWiseNotification(body)
                        CommandType.CO_MEMBERS_NOTIFICATION.name -> handleCoMembersNotification(body)
                        else -> println("Received unknown message type: ${props.headers["message_type"]}")
                    }
                }
            }
        }.onFailure { println("[RabbitMQNotificationAdapter] Failed to setup RabbitMQ: $it") }
    }

    private fun handleGroupWiseNotification(body: ByteArray) = scope.launch {
        val command = serializer.deserializeGroupWiseNotification(body)
        with(notificationPublisher) {
            send(command.message()) toAllMembersOf command.recipient()
        }
    }

    private fun handleCoMembersNotification(body: ByteArray) = scope.launch {
        val command = serializer.deserializeCoMembersNotification(body)
        with(notificationPublisher) {
            send(command.message()) toAllMembersSharingGroupWith command.sender()
        }
    }

    override fun close() = client.close()

    internal companion object {
        internal const val PUSH_NOTIFICATIONS_EXCHANGE = "push-notifications"
    }
}
