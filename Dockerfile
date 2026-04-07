FROM eclipse-temurin:25@sha256:0182539a4355ab94bf1690ae86602536d8a30486c2675ba777ccbed965e1358f

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
