package io.github.positionpal.notification.storage.tokens

import org.jetbrains.exposed.sql.Table

internal object UsersTokensTable : Table("users_tokens") {
    val userId = varchar("user_id", length = 50)
    val token = varchar("token", length = 255)
    override val primaryKey = PrimaryKey(userId, token)
}
