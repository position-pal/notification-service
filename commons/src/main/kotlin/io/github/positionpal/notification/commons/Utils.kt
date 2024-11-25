package io.github.positionpal.notification.commons

/**
 * @return the result of applying the given [onSuccess] function to an element of this [Result]
 *  if it is successful or the original [Result] if it is a failure.
 */
inline fun <T, R> Result<T>.flatMap(onSuccess: (T) -> Result<R>): Result<R> = this.fold(
    onSuccess = onSuccess,
    onFailure = { Result.failure(it) },
)
