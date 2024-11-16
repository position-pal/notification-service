package io.github.positionpal.notification.commons

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EnvDelegateTest : StringSpec({

    "Delegating a non existing environment variable should return null" {
        val myEnv: String? by env("NON_EXISTING_ENV_VAR")
        myEnv shouldBe null
    }

    "Delegating an existing environment variable should return its value" {
        val myEnv: String? by env("PATH")
        myEnv shouldNotBe null
        myEnv shouldBe System.getenv("PATH")
    }
})
