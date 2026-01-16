FROM eclipse-temurin:25@sha256:10331564d9ae41b6a534ddea472f37270a3c286e89857261631a0d772a4d8617

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
