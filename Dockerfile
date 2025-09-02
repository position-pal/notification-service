FROM eclipse-temurin:21@sha256:3d9321a057aa78045e118724788eac52da58b5d40afa5492b59fc5863f936206

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
