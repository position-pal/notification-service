package io.github.positionpal.notification.application.tokens

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.domain.UserToken

/** A repository to manage [UserToken] CRUD operations. */
interface UsersTokensRepository {

    /** Store or updates the given [userToken]. */
    suspend fun save(userToken: UserToken): Result<Unit>

    /** @return all the [UserToken]s associated to the given [userId]. */
    suspend fun get(userId: UserId): Result<Set<UserToken>>

    /** Deletes the given [userToken]. */
    suspend fun delete(userToken: UserToken): Result<Unit>
}
