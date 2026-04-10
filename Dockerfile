FROM eclipse-temurin:25@sha256:b4a48ff72ab09c8ecfc3ae35b3f9b96750437eb436bb1d9dc5cd6e1a885f7511

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
