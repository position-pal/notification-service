import Utils.inCI
import Utils.normally
import Utils.onMac
import Utils.onWindows

dependencies {
    api(project(":application"))
    with(libs) {
        implementation(postgresql)
        api(exposed.core)
        api(exposed.jdbc)
        implementation(hikaricp)
    }
}

normally {
    dockerCompose {
        val postgresService = "postgres-db"
        startedServices = listOf(postgresService)
        isRequiredBy(tasks.test)
    }
} except { inCI and (onMac or onWindows) } where {
    tasks.test { enabled = false }
} cause "GitHub Actions runner does not support Docker Compose"
