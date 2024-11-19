dependencies {
    api(project(":application"))
    with(libs) {
        implementation(postgresql)
        implementation(exposed.core)
        implementation(exposed.jdbc)
        implementation(hikaricp)
    }
}

dockerCompose.isRequiredBy(tasks.test)
