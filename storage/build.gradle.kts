dependencies {
    api(project(":application"))
    with(libs) {
        implementation(postgresql)
        api(exposed.core)
        api(exposed.jdbc)
        implementation(hikaricp)
    }
}

dockerCompose {
    val postgresService = "postgres-db"
    startedServices = listOf(postgresService)
}

dockerCompose.isRequiredBy(tasks.test)
