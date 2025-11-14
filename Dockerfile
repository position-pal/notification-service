FROM eclipse-temurin:25@sha256:d776c7ab7f1bc0eb4d1ebaddae76a027408c63422932fdda665714a58de75bb9

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
