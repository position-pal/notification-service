package io.github.positionpal.notification.entrypoint

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.library.Architectures.onionArchitecture
import io.kotest.core.spec.style.StringSpec

class ArchitecturalTest : StringSpec() {

    init {
        "Project-wise architecture should adhere to ports and adapters, a.k.a onion architecture" {
            val code = ClassFileImporter().importPackages("$PROJECT_GROUP..")
            onionArchitecture()
                .domainModels("$PROJECT_GROUP.commons..", "$PROJECT_GROUP.domain..")
                .applicationServices("$PROJECT_GROUP.application..", "$PROJECT_GROUP.presentation..")
                .adapter("Firebase Cloud Messaging service", "$PROJECT_GROUP.fcm..")
                .adapter("gRPC API", "$PROJECT_GROUP.grpc..")
                .adapter("storage", "$PROJECT_GROUP.storage..")
                .adapter("message broker", "$PROJECT_GROUP.mom..")
                .ignoreDependency(havingEntrypointAsOrigin, andAnyTarget)
                .because("`Entrypoint` submodule contains the main method wiring all the adapters together.")
                .ensureAllClassesAreContainedInArchitectureIgnoring(havingEntrypointAsOrigin)
                .withOptionalLayers(true)
                .check(code)
        }
    }

    private companion object {
        private const val PROJECT_GROUP = "io.github.positionpal.notification"

        val havingEntrypointAsOrigin: DescribedPredicate<JavaClass> = DescribedPredicate.describe(
            "in `entrypoint` package",
        ) { it.packageName.contains("entrypoint") }

        val andAnyTarget: DescribedPredicate<JavaClass> = DescribedPredicate.alwaysTrue()
    }
}
