package io.github.positionpal.notification.application.tokens

/** A result of an operation that failed because of a conflict with the current state. */
class Conflict(override val message: String, override val cause: Throwable? = null) : Error(message, cause)

/** A result of an operation that failed because of a not found resource. */
class NotFound(override val message: String, override val cause: Throwable? = null) : Error(message, cause)
