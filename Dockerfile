FROM eclipse-temurin:25@sha256:dcf835e52330939b6c9f90ecab8aafcbcaa8fbf48423db44de884cf978c10144

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
