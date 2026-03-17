FROM eclipse-temurin:25@sha256:c3b86e3867140345626eba1927a29e2df58c8619ec1c9b0951c0fdd7df97a145

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
