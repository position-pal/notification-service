package io.github.positionpal.notification.storage.tokens

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.tokens.Conflict
import io.github.positionpal.notification.application.tokens.NotFound
import io.github.positionpal.notification.application.tokens.UsersTokensRepository
import io.github.positionpal.notification.domain.UserToken
import io.github.positionpal.notification.storage.PostgresConnectionFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/** A [UsersTokensRepository] adapter for a PostgreSQL database. */
class PostgresUsersTokensRepository(
    private val connectionFactory: PostgresConnectionFactory,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UsersTokensRepository {

    private val connection by lazy { connectionFactory.connect().getOrThrow() }

    override suspend fun save(userToken: UserToken): Result<Unit> = get(userToken.userId)
        .mapCatching { if (userToken in it) throw Conflict("Token $userToken already present.") }
        .mapCatching {
            suspendTransaction {
                UsersTokensTable.insert {
                    it[userId] = userToken.userId.username()
                    it[token] = userToken.token
                }
            }
        }

    override suspend fun get(userId: UserId): Result<Set<UserToken>> = runCatching {
        suspendTransaction {
            UsersTokensTable.selectAll().where { UsersTokensTable.userId eq userId.username() }
                .map { UserToken(userId, it[UsersTokensTable.token]) }
                .toSet()
        }
    }

    override suspend fun delete(userToken: UserToken): Result<Unit> = get(userToken.userId)
        .mapCatching { if (userToken !in it) throw NotFound("Token $userToken not present") }
        .mapCatching {
            suspendTransaction {
                UsersTokensTable.deleteWhere {
                    userId eq userToken.userId.username() and (token eq userToken.token)
                }
            }
        }

    private suspend fun <T> suspendTransaction(database: Database = connection, block: Transaction.() -> T): T =
        newSuspendedTransaction(context = dispatcher, db = database, statement = block)
}
