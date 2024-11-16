package io.github.positionpal.notification.storage.tokens

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.tokens.Conflict
import io.github.positionpal.notification.application.tokens.NotFound
import io.github.positionpal.notification.domain.UserToken
import io.github.positionpal.notification.storage.PostgresConnectionFactory
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class PostgresUsersTokensRepositoryTest : WordSpec() {

    init {
        "PostgresUsersTokensRepository" When {
            "attempting to save non-existing tokens" should {
                "work seamlessly" {
                    runBlocking {
                        val results = mutableSetOf<Result<Unit>>()
                        userTokens.forEach { results.add(repository.save(it)) }
                        results.all { it.isSuccess } shouldBe true
                    }
                }
            }

            "attempting to save an existing token" should {
                "return a failed result" {
                    runBlocking {
                        val result = repository.save(UserToken(user, token))
                        result.isFailure shouldBe true
                        shouldThrow<Conflict> { result.getOrThrow() }
                    }
                }
            }

            "getting the tokens of a user" should {
                "return the saved token" {
                    runBlocking {
                        val result = repository.get(user)
                        result.isSuccess shouldBe true
                        result.getOrThrow() shouldBe userTokens
                    }
                }
            }

            "deleting existing tokens" should {
                "work seamlessly" {
                    runBlocking {
                        val results = mutableSetOf<Result<Unit>>()
                        userTokens.forEach { results.add(repository.delete(it)) }
                        results.all { it.isSuccess } shouldBe true
                        repository.get(user).getOrThrow() shouldBe emptySet()
                    }
                }
            }

            "attempting to delete a non-existing token" should {
                "return a failed result" {
                    runBlocking {
                        val result = repository.delete(UserToken(user, token))
                        result.isFailure shouldBe true
                        shouldThrow<NotFound> { result.getOrThrow() }
                    }
                }
            }
        }
    }

    private companion object {
        val repository = PostgresUsersTokensRepository(
            PostgresConnectionFactory(
                databaseName = "notifications_service",
                username = "postgres",
                password = "postgres",
                host = "localhost",
                port = 5432,
            ),
        )
        private val user = UserId.create("test-user")
        private const val token = "dXsG47jkHv1edfdffkj_ek:APA91bHq7klZj2R7DfO_MoL4t1O9aHDeEZK8Acd2A3h9E-" +
            "Fuz_p9WXe_kYp5FkX0G7QuKvcU45asduE6bJ5Y4_mTY9GsTTg8Iklpe3Oa7DyR9D6tGzqg4uP"
        private const val token2 = "fKg49LmnPQ2hgghghghvvb:APA91bGcT84qT9FZ7YhXp8aZCJNUD7Fk3cWxH9BErYZ7OMqHJ" +
            "_1JmnvKD8Q2p7gX3TyHY8PLTQoXSk7bH5MvKybX5xZTyDR9XTZKmVoLdRwQ9BKMLkZNDV2PxGpGm6Ksopm0L"
        private val userTokens = setOf(UserToken(user, token), UserToken(user, token2))
    }
}
