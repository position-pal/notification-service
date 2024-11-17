package io.github.positionpal.notification.grpc.tokens

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.tokens.Conflict
import io.github.positionpal.notification.application.tokens.NotFound
import io.github.positionpal.notification.application.tokens.UsersTokensService
import io.github.positionpal.notification.domain.UserToken
import io.github.positionpal.notification.presentation.Proto
import io.github.positionpal.notification.presentation.ProtoConversion.toProto
import io.github.positionpal.notification.presentation.UsersTokensServiceGrpcKt
import io.grpc.ManagedChannel
import io.grpc.Server
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class GrpcUsersTokensServiceTest : WordSpec({

    lateinit var server: Server
    lateinit var channel: ManagedChannel
    lateinit var delegatingService: UsersTokensService
    lateinit var service: GrpcUsersTokensService
    lateinit var stub: UsersTokensServiceGrpcKt.UsersTokensServiceCoroutineStub

    beforeEach {
        val serverName = InProcessServerBuilder.generateName()
        delegatingService = mockk()
        service = GrpcUsersTokensService(delegatingService)
        server = InProcessServerBuilder
            .forName(serverName)
            .directExecutor()
            .addService(service)
            .build()
            .start()
        channel = InProcessChannelBuilder.forName(serverName).directExecutor().build()
        stub = UsersTokensServiceGrpcKt.UsersTokensServiceCoroutineStub(channel)
    }

    afterEach {
        channel.shutdownNow()
        server.shutdownNow()
    }

    "GrpcUsersTokensService" When {
        "receiving a request to save a token" should {
            "return a successful response if the delegating service returns a successful result" {
                coEvery { delegatingService.register(any(), any()) } returns Result.success(testToken)
                val response = runBlocking { stub.register(testToken.toProto()) }
                response.status.code shouldBe Proto.StatusCode.OK
            }

            "return a Conflict response if the delegating service returns a Conflict error" {
                val error = Conflict("Token already present")
                coEvery { delegatingService.register(any(), any()) } returns Result.failure(error)
                val response = runBlocking { stub.register(testToken.toProto()) }
                response.status.code shouldBe Proto.StatusCode.CONFLICT
                response.status.message shouldBe error.message
            }
        }

        "receives a request to invalidate a token " should {
            "return a successful response if the delegating service returns a successful result" {
                coEvery { delegatingService.invalidate(any(), any()) } returns Result.success(Unit)
                val response = runBlocking { stub.invalidate(testToken.toProto()) }
                response.status.code shouldBe Proto.StatusCode.OK
            }

            "return a NotFound response if the delegating service returns a NotFound error" {
                val error = NotFound("Token not found")
                coEvery { delegatingService.invalidate(any(), any()) } returns Result.failure(error)
                val response = runBlocking { stub.invalidate(testToken.toProto()) }
                response.status.code shouldBe Proto.StatusCode.NOT_FOUND
                response.status.message shouldBe error.message
            }
        }
    }
}) {
    private companion object {
        private val testToken = UserToken(UserId.create("user"), "token")
    }
}
