FROM eclipse-temurin:25@sha256:1bda4d9e668f44f399abed30636c34e0befb727408fba27b1e6aaefcf9df346b

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
