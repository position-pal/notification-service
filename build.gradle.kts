import DotenvUtils.dotenv
import DotenvUtils.injectInto
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    with(libs.plugins) {
        alias(kotlin.jvm)
        alias(kotlin.qa)
        alias(kotlin.dokka)
        alias(gradle.docker.compose)
    }
}

allprojects {
    group = "io.github.positionpal"

    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/position-pal/shared-kernel")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

subprojects {

    with(rootProject.libs.plugins) {
        apply(plugin = kotlin.jvm.get().pluginId)
        apply(plugin = kotlin.qa.get().pluginId)
        apply(plugin = kotlin.dokka.get().pluginId)
        apply(plugin = gradle.docker.compose.get().pluginId)
    }

    with(rootProject.libs) {
        dependencies {
            implementation(kotlin.stdlib)
            implementation(kotlin.stdlib.jdk8)
            testImplementation(bundles.kotlin.testing)
        }
    }

    kotlin {
        compilerOptions {
            allWarningsAsErrors = true
        }
    }

    tasks.withType<Test>().configureEach {
        testLogging {
            events(*TestLogEvent.values())
            exceptionFormat = TestExceptionFormat.FULL
        }
        useJUnitPlatform()
    }

    rootProject.dotenv?.let { injectInto(JavaExec::class, Test::class) environmentsFrom it }
}
