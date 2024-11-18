package io.github.positionpal.notification.application.notifications

import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.NotificationMessage
import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.groups.GroupsRepository
import io.github.positionpal.notification.application.notifications.impl.BasicNotificationPublisher
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class BasicNotificationPublisherTest : StringSpec({

    lateinit var notificationPublisher: TestableNotificationPublisher

    beforeEach {
        notificationPublisher = TestableNotificationPublisher()
    }

    "BasicNotificationPublisher should be able to send a notification to all members of a group" {
        val message = NotificationMessage.create("A greeting", "Hello, world!")
        with(notificationPublisher) {
            send(message) toAllMembersOf GroupId.create("astro")
        }
        notificationPublisher.messages shouldBe mapOf(message to groups.getValue(GroupId.create("astro")))
    }

    "BasicNotificationPublisher should be able to send a notification to all users sharing a group with a user" {
        val message = NotificationMessage.create("A greeting", "Hello, world!")
        with(notificationPublisher) {
            send(message) toAllMembersSharingGroupWith UserId.create("Luca")
        }
        notificationPublisher.messages shouldBe mapOf(message to groups.values.flatten().toSet())
    }
}) {
    private companion object {
        private val groups = mapOf(
            GroupId.create("astro") to setOf(UserId.create("Luca"), UserId.create("Greg")),
            GroupId.create("divine") to setOf(UserId.create("Luca"), UserId.create("Josh"), UserId.create("Alice")),
        )
        private val groupsRepository = mockk<GroupsRepository> {
            coEvery { getMembersOf(any()) } answers { Result.success(groups.getValue(firstArg())) }
            coEvery { getGroupsOf(any()) } answers {
                Result.success(groups.filter { (_, users) -> firstArg() in users }.keys)
            }
        }
        class TestableNotificationPublisher : BasicNotificationPublisher(groupsRepository) {
            val messages = mutableMapOf<NotificationMessage, Set<UserId>>()

            override suspend fun send(message: NotificationMessage, userIds: Set<UserId>) {
                messages.merge(message, userIds) { old, new -> old + new }
            }
        }
    }
}
