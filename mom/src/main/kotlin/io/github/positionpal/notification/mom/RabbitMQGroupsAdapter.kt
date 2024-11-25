package io.github.positionpal.notification.mom

import io.github.positionpal.AvroSerializer
import io.github.positionpal.MessageType
import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.groups.GroupsRepository
import io.github.positionpal.notification.commons.flatMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * RabbitMQ message adapter for groups events.
 */
class RabbitMQGroupsAdapter(
    private val groupsRepository: GroupsRepository,
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
                channel.declareHeadersExchange(GROUP_UPDATES_EXCHANGE)
                channel.declareBindAndRegisterCallbackTo(GROUP_UPDATES_EXCHANGE) { props, body ->
                    when (val messageType = props.headers["message_type"].toString()) {
                        MessageType.MEMBER_ADDED.name -> handleMemberAdded(body)
                        MessageType.MEMBER_REMOVED.name -> handleMemberRemoved(body)
                        else -> println("Received unknown message type: $messageType")
                    }
                }
            }
        }.onFailure { println("[RabbitMQGroupsAdapter] Failed to setup RabbitMQ: $it") }
    }

    private fun handleMemberAdded(body: ByteArray) = scope.launch {
        val command = serializer.deserializeAddedMemberToGroup(body)
        groupsRepository.addMember(GroupId.create(command.groupId()), UserId.create(command.addedMember().id()))
    }

    private fun handleMemberRemoved(body: ByteArray) = scope.launch {
        val command = serializer.deserializeRemovedMemberToGroup(body)
        groupsRepository.removeMember(GroupId.create(command.groupId()), UserId.create(command.removedMember().id()))
    }

    override fun close() = client.close()

    internal companion object {
        internal const val GROUP_UPDATES_EXCHANGE = "group_updates_exchange"
    }
}
