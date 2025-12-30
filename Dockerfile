FROM eclipse-temurin:25-jre-jammy AS build

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]