FROM eclipse-temurin:25@sha256:c7cf8f103714da175842713f705e2a70551daa74f7b5cb47f40059553e6b9cde

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
