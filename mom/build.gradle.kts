import Utils.isInCI
import Utils.isOnLinux
import Utils.normally

dependencies {
    api(project(":presentation"))
    implementation(libs.rabbitmq.amqp.client)
}

normally {
    dockerCompose {
        val rabbitMqService = "rabbitmq-broker"
        startedServices = listOf(rabbitMqService)
        isRequiredBy(tasks.test)
    }
} exceptOn { isInCI && !isOnLinux } where {
    // Github Actions Windows and MacOS runners do not support Docker
    tasks.test { enabled = false }
}
