package io.github.positionpal.notification.application.tokens

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.domain.UserToken

/**
 * A repository to manage [UserToken]s CRUD operations.
 */
interface UsersTokensRepository {

    /**
     * Store or updates the given [userToken].
     * @return a [Result] indicating the success or failure of the operation.
     */
    suspend fun save(userToken: UserToken): Result<Unit>

    /**
     * @return a successful [Result] with all the [UserToken]s associated to the
     *  given [userId] or a failed one in case of errors.
     */
    suspend fun get(userId: UserId): Result<Set<UserToken>>

    /**
     * Deletes the given [userToken].
     * @return a [Result] indicating the success or failure of the operation.
     */
    suspend fun delete(userToken: UserToken): Result<Unit>
}
