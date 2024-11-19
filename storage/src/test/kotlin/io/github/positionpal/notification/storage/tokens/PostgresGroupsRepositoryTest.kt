package io.github.positionpal.notification.storage.tokens

import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.storage.PostgresConnectionFactory
import io.github.positionpal.notification.storage.groups.PostgresGroupsRepository
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class PostgresGroupsRepositoryTest : WordSpec({

    beforeSpec {
        connectionFactory.connect()
    }

    "PostgresUsersTokensRepository" When {
        "attempting to save a user in a group" should {
            "create successfully a new record" {
                runBlocking {
                    groups.forEach { (group, users) ->
                        users.forEach { user ->
                            val result = repository.addMember(group, user)
                            result.isSuccess shouldBe true
                        }
                    }
                }
            }
        }

        "attempting to get all members of a group" should {
            "work seamlessly" {
                runBlocking {
                    groups.forEach { (group, users) ->
                        val result = repository.getMembersOf(group)
                        result.isSuccess shouldBe true
                        result.getOrThrow() shouldBe users
                    }
                }
            }
        }

        "attempting to get all groups of a user" should {
            "work seamlessly" {
                runBlocking {
                    groups.forEach { (_, users) ->
                        users.forEach { user ->
                            val result = repository.getGroupsOf(user)
                            result.isSuccess shouldBe true
                            val expectedGroups = groups.filter { user in it.second }.map { it.first }.toSet()
                            result.getOrThrow() shouldBe expectedGroups
                        }
                    }
                }
            }
        }

        "removing a user from a group" should {
            "delete the record correctly" {
                runBlocking {
                    val targetGroup = groups.first()
                    targetGroup.let { (group, users) ->
                        users.forEach {
                            val result = repository.removeMember(group, it)
                            result.isSuccess shouldBe true
                        }
                    }
                    repository.getMembersOf(targetGroup.first).getOrThrow() shouldBe emptySet()
                }
            }
        }
    }
}) {
    private companion object {
        private val connectionFactory = PostgresConnectionFactory(
            databaseName = "notifications_service",
            username = "postgres",
            password = "postgres",
            host = "localhost",
            port = 5432,
        )
        private val repository = PostgresGroupsRepository()
        private val groups = setOf(
            GroupId.create("astro") to setOf(UserId.create("Luca"), UserId.create("Greg")),
            GroupId.create("divine") to setOf(UserId.create("Luca"), UserId.create("Josh"), UserId.create("Alice")),
        )
    }
}
