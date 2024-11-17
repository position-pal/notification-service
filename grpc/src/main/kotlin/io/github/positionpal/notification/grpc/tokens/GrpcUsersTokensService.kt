package io.github.positionpal.notification.grpc.tokens

import io.github.positionpal.notification.application.tokens.Conflict
import io.github.positionpal.notification.application.tokens.NotFound
import io.github.positionpal.notification.application.tokens.UsersTokensService
import io.github.positionpal.notification.presentation.Proto
import io.github.positionpal.notification.presentation.Proto.StatusCode
import io.github.positionpal.notification.presentation.ProtoConversion.toKt
import io.github.positionpal.notification.presentation.UsersTokensServiceGrpcKt
import io.github.positionpal.notification.presentation.emptyResponse
import io.github.positionpal.notification.presentation.status

/** A gRPC service to manage user tokens. */
class GrpcUsersTokensService(
    private val usersTokensService: UsersTokensService,
) : UsersTokensServiceGrpcKt.UsersTokensServiceCoroutineImplBase() {

    override suspend fun register(request: Proto.UserToken): Proto.EmptyResponse = with(request.toKt()) {
        executeServiceCall { usersTokensService.register(userId, token) }
    }

    override suspend fun invalidate(request: Proto.UserToken): Proto.EmptyResponse = with(request.toKt()) {
        executeServiceCall { usersTokensService.invalidate(userId, token) }
    }

    private suspend fun <T> executeServiceCall(call: suspend () -> Result<T>): Proto.EmptyResponse =
        call().fold(
            onFailure = { handleError(it) },
            onSuccess = { createResponse(StatusCode.OK) },
        )

    private fun handleError(error: Throwable): Proto.EmptyResponse =
        when (error) {
            is Conflict -> createResponse(StatusCode.CONFLICT, error.message)
            is NotFound -> createResponse(StatusCode.NOT_FOUND, error.message)
            else -> createResponse(StatusCode.GENERIC_ERROR, error.message)
        }

    private fun createResponse(code: StatusCode, message: String? = null): Proto.EmptyResponse =
        emptyResponse {
            status = status {
                this.code = code
                message?.let { this.message = it }
            }
        }
}
