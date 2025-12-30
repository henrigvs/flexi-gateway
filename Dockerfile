FROM eclipse-temurin:21-jre-jammy AS build

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]