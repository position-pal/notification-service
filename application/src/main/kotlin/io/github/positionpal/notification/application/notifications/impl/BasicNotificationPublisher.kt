package io.github.positionpal.notification.application.notifications.impl

import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.NotificationMessage
import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.groups.GroupsRepository
import io.github.positionpal.notification.application.notifications.NotificationPublisher
import io.github.positionpal.notification.application.notifications.PublishingTargetStrategy

/**
 * A basic implementation of [NotificationPublisher] encapsulating the strategy logic to send notifications.
 */
abstract class BasicNotificationPublisher(private val groupsRepository: GroupsRepository) : NotificationPublisher {

    override suspend infix fun send(notificationMessage: NotificationMessage): PublishingTargetStrategy =
        BasicPublishingTargetStrategy(notificationMessage, groupsRepository)

    private inner class BasicPublishingTargetStrategy(
        private val message: NotificationMessage,
        private val groupsRepository: GroupsRepository,
    ) : PublishingTargetStrategy {
        override suspend fun toAllMembersOf(groupId: GroupId) {
            groupsRepository.getMembersOf(groupId).onSuccess { send(message, it) }
        }

        override suspend fun toAllMembersSharingGroupWith(userId: UserId) {
            groupsRepository.getGroupsOf(userId).onSuccess { groups ->
                groups.map { groupsRepository.getMembersOf(it) }
                    .map { it.onSuccess { members -> send(message, members) } }
            }
        }
    }
}
