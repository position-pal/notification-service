package io.github.positionpal.notification.storage.tokens

import io.github.positionpal.notification.storage.Postgres

val localPostgresConfiguration = Postgres.Configuration(
    databaseName = "notifications_service",
    username = "postgres",
    password = "postgres",
    host = "localhost",
    port = 5432,
)
