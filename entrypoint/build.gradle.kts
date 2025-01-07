plugins {
    application
    alias(libs.plugins.shadowJar)
}

dependencies {
    implementation(project(":grpc"))
    implementation(project(":storage"))
    implementation(project(":mom"))
    implementation(project(":fcm"))
    testImplementation(libs.archunit)
}

application {
    mainClass = "$group.notification.entrypoint.Launcher"
}
