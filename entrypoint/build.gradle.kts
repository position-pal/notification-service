plugins {
    application
}

dependencies {
    implementation(project(":grpc"))
    implementation(project(":storage"))
    implementation(project(":mom"))
    implementation(project(":fcm"))
}

application {
    mainClass = "$group.notification.entrypoint.Launcher"
}
