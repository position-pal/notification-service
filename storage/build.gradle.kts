import Utils.isInCI
import Utils.isOnLinux
import Utils.normally

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
} exceptOn { isInCI && !isOnLinux } where {
    // Github Actions Windows and MacOS runners do not support Docker
    tasks.test { enabled = false }
}
