package io.github.positionpal.notification.presentation

import io.github.positionpal.entities.UserId
import io.github.positionpal.notification.domain.UserToken

/** Utility object to convert entities from and to proto messages. */
object ProtoConversion {

    /** @return a [UserToken] from the given [Proto.UserToken] message class. */
    fun Proto.UserToken.toKt(): UserToken = UserToken(UserId.create(user), token)

    /** @return a [Proto.UserToken] message class from the given [UserToken]. */
    fun UserToken.toProto(): Proto.UserToken = userToken {
        user = this@toProto.userId.value()
        token = this@toProto.token
    }
}
