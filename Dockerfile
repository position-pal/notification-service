FROM eclipse-temurin:25@sha256:8112b421669e4ce5c5a550c493565d66d6efcb7fead75d6477d686378d9ca3f6

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
