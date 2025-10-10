FROM eclipse-temurin:25@sha256:954b6f1a5610dd42d4ff220be2d928aed6e314b8a240590c5c4bbeee2dcde121

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
