FROM eclipse-temurin:25@sha256:726ff25234d2189db3c509e040bc220f61466d17c41f768eb7c55d92dcdcf162

WORKDIR /app

COPY ./entrypoint/build/libs/*-all.jar app.jar

# GRPC
EXPOSE 5052

ENTRYPOINT ["java", "-jar", "app.jar"]
