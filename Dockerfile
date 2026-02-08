FROM eclipse-temurin:25@sha256:5b691907413c1c67b1f2402a39c64736d02404e703aa6f688164be23f83f06c4

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
