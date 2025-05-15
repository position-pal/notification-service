FROM eclipse-temurin:21@sha256:88214b12ef97dcb4d44a96f23043041a78ab08fc035740309f1f0b026ce79940

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
