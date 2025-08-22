# Étape 1 : Build
FROM openjdk:21-slim AS build

WORKDIR /app

# Copy mvn wrapper and config
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download deps in cache
RUN ./mvnw dependency:go-offline -B

# Copy source code + compiling
COPY src src
RUN ./mvnw clean package -DskipTests

# Étape 2 : Final minimal image
FROM openjdk:21-slim

WORKDIR /app

# Copy only final jar
COPY --from=build /app/target/*.jar app.jar

# External config mounted wince docker compose
VOLUME /config

# Lancement
ENTRYPOINT ["java","-jar","app.jar"]
