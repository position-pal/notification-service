package io.github.positionpal.notification.mom

import io.github.positionpal.AddedMemberToGroup
import io.github.positionpal.AvroSerializer
import io.github.positionpal.MessageType
import io.github.positionpal.RemovedMemberToGroup
import io.github.positionpal.User
import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.groups.GroupsRepository
import io.github.positionpal.notification.mom.RabbitMQGroupsAdapter.Companion.GROUP_UPDATES_EXCHANGE
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

class RabbitMQGroupsAdapterTest : WordSpec({

    "RabbitMQGroupsAdapter" should {
        "receive groups events and forward appropriately to the groups repository" {
            runBlocking {
                RabbitMQGroupsAdapter(fakeGroupsRepository, localRabbitMQConfig, CoroutineScope(Dispatchers.IO)).use {
                    it.setup()
                    publish(
                        GROUP_UPDATES_EXCHANGE,
                        MessageType.MEMBER_ADDED.name,
                        serializer.serializeAddedMemberToGroup(addedEvent),
                    )
                    publish(
                        GROUP_UPDATES_EXCHANGE,
                        MessageType.MEMBER_REMOVED.name,
                        serializer.serializeRemovedMemberToGroup(removedEvent),
                    )
                    eventually(eventuallyConfig) {
                        coVerify(exactly = 1) {
                            fakeGroupsRepository.addMember(testGroup, UserId.create(testUser.id()))
                        }
                        coVerify(exactly = 1) {
                            fakeGroupsRepository.removeMember(testGroup, UserId.create(testUser.id()))
                        }
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
        val testUser = User.create("luke", "Luke", "Skywalker", "lukesky@gmail.com", "member")
        val addedEvent = AddedMemberToGroup.create(testGroup.value(), testUser)
        val removedEvent = RemovedMemberToGroup.create(testGroup.value(), testUser)
        val fakeGroupsRepository = mockk<GroupsRepository> {
            coEvery { addMember(testGroup, UserId.create(testUser.id())) } returns Result.success(Unit)
            coEvery { removeMember(testGroup, UserId.create(testUser.id())) } returns Result.success(Unit)
        }
    }
}
