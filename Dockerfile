FROM eclipse-temurin:25@sha256:ddd55eda5ad0ef851a6c6b5169a83d6f9c9481449de77ae511a3118a3cf8fe91

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
