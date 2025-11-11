FROM eclipse-temurin:25@sha256:11d2909cab32d13eabaaa9b3bc2618a82c4749d19eb4dc2ad57eb268612a8cc7

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
