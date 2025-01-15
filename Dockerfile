FROM eclipse-temurin:21@sha256:a20cfa6afdbf57ff2c4de77ae2d0e3725a6349f1936b5ad7c3d1b06f6d1b840a

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
