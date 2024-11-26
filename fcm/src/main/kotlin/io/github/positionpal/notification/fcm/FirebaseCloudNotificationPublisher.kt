package io.github.positionpal.notification.fcm

import io.github.positionpal.entities.NotificationMessage
import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.groups.GroupsRepository
import io.github.positionpal.notification.application.notifications.impl.BasicNotificationPublisher
import io.github.positionpal.notification.application.tokens.UsersTokensRepository
import org.slf4j.LoggerFactory

/**
 * A notifications publisher sending notifications using Firebase Cloud Messaging.
 */
class FirebaseCloudNotificationPublisher(
    private val firebase: Firebase,
    private val usersTokensRepository: UsersTokensRepository,
    groupsRepository: GroupsRepository,
) : BasicNotificationPublisher(groupsRepository) {

    override suspend fun send(message: NotificationMessage, userIds: Set<UserId>) =
        userIds.map { usersTokensRepository.get(it) }.forEach {
            it.mapCatching { userTokens ->
                userTokens.forEach { userToken -> firebase.sendMessage(userToken.token, message) }
            }.onFailure { err -> logger.error("Failure in sending notification {}: {}", message, err.message) }
        }

    private companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
