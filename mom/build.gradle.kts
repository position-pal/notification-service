dependencies {
    api(project(":presentation"))
    implementation(libs.rabbitmq.amqp.client)
}

dockerCompose {
    val rabbitMqService = "rabbitmq-broker"
    startedServices = listOf(rabbitMqService)
}

dockerCompose.isRequiredBy(tasks.test)
