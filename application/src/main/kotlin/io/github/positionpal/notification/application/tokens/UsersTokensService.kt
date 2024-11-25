package io.github.positionpal.notification.application.tokens

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.domain.Token
import io.github.positionpal.notification.domain.UserToken

/**
 * A service to manage [UserToken]s.
 */
interface UsersTokensService {

    /**
     * Registers a new [userId] - [token] association.
     * @return a successful [Result] with the registered [UserToken] or a failed one in case of error.
     */
    suspend fun register(userId: UserId, token: Token): Result<UserToken>

    /**
     * Invalidates the given [token] for the given [userId], i.e., removes the [userId] - [token] association.
     * @return a [Result] indicating the success or failure of the operation.
     */
    suspend fun invalidate(userId: UserId, token: Token): Result<Unit>
}
