FROM eclipse-temurin:25@sha256:bdccc9761169b39da130bf829e6c30206eda16f2a87d86bd6774a0b2a5f8d33c

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
