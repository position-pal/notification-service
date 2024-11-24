package io.github.positionpal.notification.storage.groups

import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.application.groups.GroupsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/** A [GroupsRepository] adapter for a PostgresSQL database. * */
class PostgresGroupsRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : GroupsRepository {
    override suspend fun addMember(groupId: GroupId, userId: UserId): Result<Unit> = runCatching {
        newSuspendedTransaction(dispatcher) {
            UsersGroupsTable.insert {
                it[UsersGroupsTable.groupId] = groupId.value()
                it[UsersGroupsTable.userId] = userId.username()
            }
        }
    }

    override suspend fun removeMember(groupId: GroupId, userId: UserId): Result<Unit> = runCatching {
        newSuspendedTransaction(dispatcher) {
            UsersGroupsTable.deleteWhere {
                (UsersGroupsTable.groupId eq groupId.value()) and (UsersGroupsTable.userId eq userId.username())
            }
        }
    }

    override suspend fun getMembersOf(groupId: GroupId): Result<Set<UserId>> = runCatching {
        newSuspendedTransaction(dispatcher) {
            UsersGroupsTable.selectAll().where { UsersGroupsTable.groupId eq groupId.value() }
                .map { UserId.create(it[UsersGroupsTable.userId]) }
                .toSet()
        }
    }

    override suspend fun getGroupsOf(userId: UserId): Result<Set<GroupId>> = runCatching {
        newSuspendedTransaction(dispatcher) {
            UsersGroupsTable.selectAll().where { UsersGroupsTable.userId eq userId.username() }
                .map { GroupId.create(it[UsersGroupsTable.groupId]) }
                .toSet()
        }
    }
}
