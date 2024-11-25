package io.github.positionpal.notification.mom

import com.rabbitmq.client.Channel
import io.github.positionpal.AvroSerializer
import io.github.positionpal.MessageType
import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.groups.GroupsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * RabbitMQ groups events consumer adapter.
 */
class RabbitMQGroupsEventsConsumer(
    private val groupsRepository: GroupsRepository,
    configuration: RabbitMQ.Configuration,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RabbitMQMessageHandler(configuration) {

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val serializer = AvroSerializer()

    override fun RabbitMQClient.onChannelCreated(channel: Channel) {
        channel.declareHeadersExchange(GROUP_UPDATES_EXCHANGE)
        channel.declareBindAndRegisterCallbackTo(GROUP_UPDATES_EXCHANGE) { props, body ->
            when (val messageType = props.headers["message_type"].toString()) {
                MessageType.MEMBER_ADDED.name -> handleMemberAdded(body)
                MessageType.MEMBER_REMOVED.name -> handleMemberRemoved(body)
                else -> logger.debug("Received unknown message type: $messageType")
            }
        }
    }

    private fun handleMemberAdded(body: ByteArray) = scope.launch {
        val command = serializer.deserializeAddedMemberToGroup(body)
        logger.debug("Handling member added event {}", command)
        groupsRepository.addMember(GroupId.create(command.groupId()), UserId.create(command.addedMember().id()))
    }

    private fun handleMemberRemoved(body: ByteArray) = scope.launch {
        val command = serializer.deserializeRemovedMemberToGroup(body)
        logger.debug("Handling member removed event {}", command)
        groupsRepository.removeMember(GroupId.create(command.groupId()), UserId.create(command.removedMember().id()))
    }

    internal companion object {
        internal const val GROUP_UPDATES_EXCHANGE = "group_updates_exchange"
    }
}
