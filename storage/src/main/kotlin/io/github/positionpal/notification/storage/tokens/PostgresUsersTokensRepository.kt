package io.github.positionpal.notification.storage.tokens

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.tokens.Conflict
import io.github.positionpal.notification.application.tokens.NotFound
import io.github.positionpal.notification.application.tokens.UsersTokensRepository
import io.github.positionpal.notification.domain.UserToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/** A [UsersTokensRepository] adapter for a PostgresSQL database. */
class PostgresUsersTokensRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UsersTokensRepository {

    override suspend fun save(userToken: UserToken): Result<Unit> = get(userToken.userId)
        .mapCatching { if (userToken in it) throw Conflict("Token $userToken already present.") }
        .mapCatching {
            newSuspendedTransaction(dispatcher) {
                UsersTokensTable.insert {
                    it[userId] = userToken.userId.username()
                    it[token] = userToken.token
                }
            }
        }

    override suspend fun get(userId: UserId): Result<Set<UserToken>> = runCatching {
        newSuspendedTransaction(dispatcher) {
            UsersTokensTable.selectAll().where { UsersTokensTable.userId eq userId.username() }
                .map { UserToken(userId, it[UsersTokensTable.token]) }
                .toSet()
        }
    }

    override suspend fun delete(userToken: UserToken): Result<Unit> = get(userToken.userId)
        .mapCatching { if (userToken !in it) throw NotFound("Token $userToken not present") }
        .mapCatching {
            newSuspendedTransaction(dispatcher) {
                UsersTokensTable.deleteWhere {
                    userId eq userToken.userId.username() and (token eq userToken.token)
                }
            }
        }
}
