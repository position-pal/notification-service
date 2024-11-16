package io.github.positionpal.notification.application.tokens.impl

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.tokens.UsersTokensRepository
import io.github.positionpal.notification.application.tokens.UsersTokensService
import io.github.positionpal.notification.domain.Token
import io.github.positionpal.notification.domain.UserToken

/** A basic implementation of [UsersTokensService]. */
class UsersTokensServiceImpl(private val repository: UsersTokensRepository) : UsersTokensService {

    override suspend fun register(userId: UserId, token: Token): Result<UserToken> =
        UserToken(userId, token).let { userToken ->
            repository.save(userToken).map { userToken }
        }

    override suspend fun invalidate(userId: UserId, token: Token): Result<Unit> =
        UserToken(userId, token).let { repository.delete(it) }
}
