dependencies {
    api(project(":application"))
    with(libs) {
        implementation(postgresql)
        implementation(exposed.core)
        implementation(exposed.jdbc)
    }
}

dockerCompose.isRequiredBy(tasks.test)
