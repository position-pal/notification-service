FROM eclipse-temurin:25@sha256:dd23c917b42d5ba34b726c3b339ba0f71fac76a8bdebb936b511bb98832dc287

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
