FROM eclipse-temurin:25@sha256:3a35a62d3ece346cfde11b08fafdc3f99d3946d31d5ebc15d678b44a67c3b9ec

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
