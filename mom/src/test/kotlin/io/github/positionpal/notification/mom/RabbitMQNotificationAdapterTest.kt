package io.github.positionpal.notification.mom

import io.github.positionpal.AvroSerializer
import io.github.positionpal.commands.CoMembersPushNotification
import io.github.positionpal.commands.CommandType
import io.github.positionpal.commands.GroupWisePushNotification
import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.NotificationMessage
import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.notifications.NotificationPublisher
import io.github.positionpal.notification.application.notifications.PublishingTargetStrategy
import io.github.positionpal.notification.mom.RabbitMQNotificationAdapter.Companion.PUSH_NOTIFICATIONS_EXCHANGE
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.eventuallyConfig
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.WordSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class RabbitMQNotificationAdapterTest : WordSpec({

    "RabbitMQNotificationAdapter" should {
        "receive notification events and forward appropriately to the notification publisher" {
            runBlocking {
                RabbitMQNotificationAdapter(fakePublisher, localRabbitMQConfig, CoroutineScope(Dispatchers.IO)).use {
                    it.setup()
                    publish(
                        PUSH_NOTIFICATIONS_EXCHANGE,
                        CommandType.GROUP_WISE_NOTIFICATION.name,
                        serializer.serializeGroupWiseNotification(groupWiseNotification),
                    )
                    publish(
                        PUSH_NOTIFICATIONS_EXCHANGE,
                        CommandType.CO_MEMBERS_NOTIFICATION.name,
                        serializer.serializeCoMembersNotification(coMembersNotification),
                    )
                    eventually(eventuallyConfig) {
                        coVerify(exactly = 1) { fakePublisher.send(groupWiseMessage) }
                        coVerify(exactly = 1) { fakePublisher.send(coMembersMessage) }
                    }
                }
            }
        }
    }
}) {

    private companion object {
        val serializer = AvroSerializer()
        val eventuallyConfig = eventuallyConfig {
            duration = 5.seconds
            interval = 200.milliseconds
        }
        val testGroup = GroupId.create("astro")
        val testUser = UserId.create("luke")
        val groupWiseMessage = NotificationMessage.create("Group notification", "A nice group-wise message")
        val groupWiseNotification = GroupWisePushNotification.of(testGroup, testUser, groupWiseMessage)
        val coMembersMessage = NotificationMessage.create("Co-members notification", "A nice co-members message")
        val coMembersNotification = CoMembersPushNotification.of(testUser, testUser, coMembersMessage)
        val fakePublisher = mockk<NotificationPublisher> {
            coEvery { send(groupWiseMessage) } returns mockk<PublishingTargetStrategy>(relaxed = true)
            coEvery { send(coMembersMessage) } returns mockk<PublishingTargetStrategy> {
                coEvery { toAllMembersSharingGroupWith(testUser) } returns Unit
            }
        }
    }
}
