dependencies {
    api(project(":presentation"))
    with(libs) {
        api(grpc.stub)
        api(grpc.protobuf)
        api(protobuf.kotlin)
        implementation(grpc.netty.shaded)
        testImplementation(libs.grpc.testing)
        testImplementation("io.grpc:grpc-inprocess:1.68.1")
    }
}