# Étape 1 : Build
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Copier le wrapper Maven et config
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Télécharger les dépendances en cache (accélère les builds)
RUN ./mvnw dependency:go-offline -B

# Copier le code source et compiler
COPY src src
RUN ./mvnw clean package -DskipTests

# Étape 2 : Image finale minimale
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copier uniquement le JAR final
COPY --from=build /app/target/*.jar app.jar

# Config externe montée depuis le docker-compose ou k8s
VOLUME /config
ENV ROUTES_FILE=/config/routes.json

# Profil Spring par défaut (surchargeable via .env)
ENV SPRING_PROFILES_ACTIVE=prod

# Port exposé (doit correspondre à ton docker-compose)
EXPOSE 12345

# Lancement
ENTRYPOINT ["java","-jar","app.jar"]
