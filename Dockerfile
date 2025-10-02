FROM eclipse-temurin:25@sha256:85a6a2d4186aa54e27ff0b280e063ac56bf960249982dd933539190ddd8cce99

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
