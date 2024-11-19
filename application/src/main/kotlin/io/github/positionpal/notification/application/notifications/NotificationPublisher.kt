package io.github.positionpal.notification.application.notifications

import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.NotificationMessage
import io.github.positionpal.entities.UserId

/** A notification publisher service which is responsible for publishing notifications to the clients. */
interface NotificationPublisher {

    /** @return a [PublishingTargetStrategy] to specify the target of the notification message. */
    suspend infix fun send(notificationMessage: NotificationMessage): PublishingTargetStrategy

    /** Sends the given [message] to the specified [userIds]. */
    suspend fun send(message: NotificationMessage, userIds: Set<UserId>)
}

/** A strategy to specify the target to send the notification message. */
interface PublishingTargetStrategy {

    /** Sends the notification message to all members of the specified [groupId]. */
    suspend infix fun toAllMembersOf(groupId: GroupId)

    /** Sends the notification message to all members of all groups which the given [userId] is a member of. */
    suspend infix fun toAllMembersSharingGroupWith(userId: UserId)
}
