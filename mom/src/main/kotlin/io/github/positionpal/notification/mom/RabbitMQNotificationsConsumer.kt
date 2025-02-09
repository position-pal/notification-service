package io.github.positionpal.notification.mom

import com.rabbitmq.client.Channel
import io.github.positionpal.AvroSerializer
import io.github.positionpal.commands.CommandType
import io.github.positionpal.notification.application.notifications.NotificationPublisher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * RabbitMQ notification commands consumer adapter.
 */
class RabbitMQNotificationsConsumer(
    private val notificationPublisher: NotificationPublisher,
    configuration: RabbitMQ.Configuration,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RabbitMQMessageHandler(configuration) {

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val serializer = AvroSerializer()

    override fun RabbitMQClient.onChannelCreated(channel: Channel) {
        channel.declareHeadersExchange(PUSH_NOTIFICATIONS_EXCHANGE)
        channel.declareBindAndRegisterCallbackTo(PUSH_NOTIFICATIONS_EXCHANGE) { props, body ->
            when (props.headers["message_type"].toString()) {
                CommandType.GROUP_WISE_NOTIFICATION.name -> handleGroupWiseNotification(body)
                CommandType.CO_MEMBERS_NOTIFICATION.name -> handleCoMembersNotification(body)
                else -> logger.debug("Received unknown message type: {}", props.headers["message_type"])
            }
        }
    }

    private fun handleGroupWiseNotification(body: ByteArray) = scope.launch {
        val command = serializer.deserializeGroupWiseNotification(body)
        logger.debug("Handling group wise notification event {}", command)
        with(notificationPublisher) {
            send(command.message()) toAllMembersOf command.recipient()
        }
    }

    private fun handleCoMembersNotification(body: ByteArray) = scope.launch {
        val command = serializer.deserializeCoMembersNotification(body)
        logger.debug("Handling co-members notification event {}", command)
        with(notificationPublisher) {
            send(command.message()) toAllMembersSharingGroupWith command.sender()
        }
    }

    internal companion object {
        internal const val PUSH_NOTIFICATIONS_EXCHANGE = "push-notifications"
    }
}
