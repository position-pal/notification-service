FROM eclipse-temurin:25@sha256:6c1d93df46bf046c701f1dea0338ff2570c581e3ba6aabbaf6a4bf41834b5443

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
