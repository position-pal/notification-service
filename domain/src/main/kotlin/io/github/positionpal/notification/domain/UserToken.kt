package io.github.positionpal.notification.domain

import io.github.positionpal.entities.UserId

/** A type alias for a token. */
typealias Token = String

/** A value object representing a token associated to a [UserId] used to send push notifications. */
interface UserToken {

    /** The [UserId] associated to this token. */
    val userId: UserId

    /** The token used to send push notifications. */
    val token: Token

    /** Factory companion object to create new instances of [UserToken]. */
    companion object {
        /** Creates a new [UserToken] for the given [userId] associated to [token]. */
        operator fun invoke(userId: UserId, token: String): UserToken = UserTokenImpl(userId, token)
    }
}

private data class UserTokenImpl(override val userId: UserId, override val token: Token) : UserToken {
    override fun toString(): String = "UserToken(userId=${userId.value()}, token=$token)"
}
