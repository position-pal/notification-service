# Notification service

PositionPal service for notifications management.

## Pre-requisites

For the correct operation of the service, it is necessary the following environment variables are set and available at startup:

| Variable Name                        | Description                                                                                                                                                                                                                                            |
|--------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `FIREBASE_SERVICE_ACCOUNT_FILE_PATH` | The path, relative to the root project directory, to the [Firebase service account file](https://firebase.google.com/docs/admin/setup#initialize_the_sdk_in_non-google_environments) (`.json`) used to authenticate with the Firebase services. Ensure the file is stored and used securely, for example, by using Docker or Kubernetes secrets. |
| `RABBITMQ_HOST`                      | The host address of the RabbitMQ server where the message broker is running (e.g. `localhost`)                                                                                                                                                         |
| `RABBITMQ_VIRTUAL_HOST`              | The [virtual host](https://www.rabbitmq.com/docs/vhosts) in RabbitMQ used for logical separation of resources (e.g. `/`).                                                                                                                              |
| `RABBITMQ_PORT`                      | The port on which the RabbitMQ server is listening for incoming connections.                                                                                                                                                                           |
| `RABBITMQ_USERNAME`                  | The username used to authenticate with the RabbitMQ server. This should be an account with sufficient permissions to interact with the necessary queues and exchanges in the virtual host.                                                             |
| `RABBITMQ_PASSWORD`                  | The password associated with the `RABBITMQ_USERNAME`. This password is used in conjunction with the username for authentication purposes. Ensure the password is stored and used securely, for example, by using Docker or Kubernetes secrets.         |
| `GRPC_PORT`                          | The port on which the gRPC server listens for incoming requests. If not specified,                                                                                                                                                                     |
| `POSTGRES_USERNAME`                  | The username used to authenticate to the Postgres database instance.                                                                                                                                                                                   |
| `POSTGRES_PASSWORD`                  | The password associated with the `POSTGRES_USERNAME`. This password is used in conjunction with the username for authentication purposes. Ensure the password is stored and used securely, for example, by using Docker or Kubernetes secrets.         |
| `POSTGRES_HOST`                      | The host address of the Postgres database instance.                                                                                                                                                                                                    |
| `POSTGRES_PORT`                      | The port on which the Postgres database instance is listening for incoming connections.                                                                                                                                                                |

An example of valid environment setup is shown below:

```env
FIREBASE_SERVICE_ACCOUNT_FILE_PATH=service-account.json
RABBITMQ_HOST=localhost
RABBITMQ_VIRTUAL_HOST=/
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=admin
GRPC_PORT=50051
POSTGRES_USERNAME=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
```

Moreover, the service requires a Postgres database. Before starting the service, make sure the database is properly configured with all the necessary tables.
The scripts to create the tables can be found in the following SQL scripts:
- [`./storage/src/main/resources/ddl-scripts/create-tables.sql`](./storage/src/main/resources/ddl-scripts/create-tables.sql)

## Documentation

The dokka documentation can be found [here](https://position-pal.github.io/notification-service/).

Refer to the [project documentation](https://position-pal.github.io/docs/) for more details on the service implementation and the overall system architecture.
