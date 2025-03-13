# Étape 1 : Construire l'application
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Étape 2 : Créer l'image de l'application
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]