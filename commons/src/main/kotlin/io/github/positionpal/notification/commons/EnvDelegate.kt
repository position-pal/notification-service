package io.github.positionpal.notification.commons

import kotlin.reflect.KProperty

/** A delegate to access environment variables. */
class EnvDelegate(private val key: String) {

    /** Returns the value of the environment variable associated to this delegate. */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? =
        System.getenv(key)
}

/** Creates a new [EnvDelegate] for the given [key]. */
fun env(key: String): EnvDelegate = EnvDelegate(key)
