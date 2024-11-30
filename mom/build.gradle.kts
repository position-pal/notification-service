import Utils.inCI
import Utils.normally
import Utils.onMac
import Utils.onWindows

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
} except { inCI and (onMac or onWindows) } where {
    tasks.test { enabled = false }
} cause "GitHub Actions runner does not support Docker Compose"
