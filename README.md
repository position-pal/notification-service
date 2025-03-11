# Notification service

PositionPal service for notifications management.

## Development

> [!WARNING]
> This repository depends on the [`shared-kernel`](https://github.com/orgs/position-pal/packages?repo_name=shared-kernel) package published on GitHub Packages, which requires authentication to be successfully resolved.
> In CI environments, credentials are automatically inherited from the context of the GitHub Actions workflow.
> However, to correctly build and run the project locally, you need to make sure to have configured your GitHub username and a valid personal access token either in the `gradle.properties` file or as environment variables:
> 
> **Credential setup**:
> 
> - **`gradle.properties`**:
>   Add your credentials to the `gradle.properties` file located in `GRADLE_USER_HOME` (`~/.gradle` on Unix and `C:\Users\YourUser\.gradle` on Windows) as follows:
>     ```properties
>     gpr.user=<USERNAME>
>     gpr.key=<TOKEN>
>     ```
>   For more information about `gradle.properties` file refer to the [Gradle documentation](https://docs.gradle.org/current/userguide/build_environment.html).
>
> - **Environment variables**:
>   Setup the following environment variables:
>     ```scala
>     export GPR_USER=<USERNAME>
>     export GPR_KEY=<TOKEN>
>     ```
> For more information about how to create a personal access token, refer to the [GitHub documentation](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens).
>
> In case you encounter any problem, please open a new issue in the repository.

## Pre-requisites

For the correct operation of the service, it is necessary the following environment variables are set and available at startup:

| Variable Name                        | Description                                                                                                                                                                                                                                                                                                                                                   |
|--------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `FIREBASE_SERVICE_ACCOUNT_FILE_PATH` | The **absolute** path to the [Firebase service account file](https://firebase.google.com/docs/admin/setup#initialize_the_sdk_in_non-google_environments) (`.json`) used to authenticate with the Firebase services. Ensure the file is stored and used securely, for example, by using Docker or Kubernetes secrets. |
| `RABBITMQ_HOST`                      | The host address of the RabbitMQ server where the message broker is running (e.g. `localhost`)                                                                                                                                                                                                                                                                |
| `RABBITMQ_VIRTUAL_HOST`              | The [virtual host](https://www.rabbitmq.com/docs/vhosts) in RabbitMQ used for logical separation of resources (e.g. `/`).                                                                                                                                                                                                                                     |
| `RABBITMQ_PORT`                      | The port on which the RabbitMQ server is listening for incoming connections.                                                                                                                                                                                                                                                                                  |
| `RABBITMQ_USERNAME`                  | The username used to authenticate with the RabbitMQ server. This should be an account with sufficient permissions to interact with the necessary queues and exchanges in the virtual host.                                                                                                                                                                    |
| `RABBITMQ_PASSWORD`                  | The password associated with the `RABBITMQ_USERNAME`. This password is used in conjunction with the username for authentication purposes. Ensure the password is stored and used securely, for example, by using Docker or Kubernetes secrets.                                                                                                                |
| `GRPC_PORT`                          | The port on which the gRPC server listens for incoming requests. If not specified,                                                                                                                                                                                                                                                                            |
| `POSTGRES_USERNAME`                  | The username used to authenticate to the Postgres database instance.                                                                                                                                                                                                                                                                                          |
| `POSTGRES_PASSWORD`                  | The password associated with the `POSTGRES_USERNAME`. This password is used in conjunction with the username for authentication purposes. Ensure the password is stored and used securely, for example, by using Docker or Kubernetes secrets.                                                                                                                |
| `POSTGRES_HOST`                      | The host address of the Postgres database instance.                                                                                                                                                                                                                                                                                                           |
| `POSTGRES_PORT`                      | The port on which the Postgres database instance is listening for incoming connections.                                                                                                                                                                                                                                                                       |

An example of valid environment setup is shown below:

```env
FIREBASE_SERVICE_ACCOUNT_FILE_PATH=/Users/lucatassi/Projects/PositionPal/notification-service/service-account.json
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
