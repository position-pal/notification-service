FROM eclipse-temurin:25@sha256:656533f346d29cee151e2a7b697c84c878ea40cc9d04ba8bab59784d8c1e6299

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
