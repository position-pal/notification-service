package io.github.positionpal.notification.storage.groups

import org.jetbrains.exposed.sql.Table

internal object UsersGroupsTable : Table("users_groups") {
    val groupId = varchar("group_id", length = 50)
    val userId = varchar("user_id", length = 50)
    override val primaryKey = PrimaryKey(userId, groupId)
}
