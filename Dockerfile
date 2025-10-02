FROM eclipse-temurin:25@sha256:d6538ec6e6a6b5dce98a4f34e7b7be7f18e1ed6d99fa6bd9af045057afcf7aea

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
