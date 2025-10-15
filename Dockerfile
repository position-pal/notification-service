FROM eclipse-temurin:25@sha256:1dad643c8ccf9534e555612dd13e976a39336c3e5fb479030dd5f7a3ce274b1c

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
