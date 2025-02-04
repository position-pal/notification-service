FROM eclipse-temurin:21@sha256:eacf3d6d0a478649d35a65d05857cb85521b867756328dd0af3879ed401a32d6

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
