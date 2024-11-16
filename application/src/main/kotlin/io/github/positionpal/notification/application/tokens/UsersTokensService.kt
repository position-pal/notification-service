package io.github.positionpal.notification.application.tokens

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.domain.Token
import io.github.positionpal.notification.domain.UserToken

/** A service to manage [UserToken]s. */
interface UsersTokensService {

    /** Registers a new [userId] - [token] association. */
    suspend fun register(userId: UserId, token: Token): Result<UserToken>

    /** Invalidates the given [token] for the given [userId]. */
    suspend fun invalidate(userId: UserId, token: Token): Result<Unit>
}
