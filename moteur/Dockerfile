# Étape de build Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /moteur

# Copie pom.xml + téléchargement des dépendances en cache
COPY pom.xml .

RUN mvn dependency:go-offline

# Copier tout le projet (src + pom.xml)
COPY . .

# Compiler et packager le projet sans les tests
RUN mvn clean package -DskipTests

# Étape d'exécution
FROM eclipse-temurin:17-jre-alpine

WORKDIR /moteur

# Copier le jar généré depuis l'étape BUILD
COPY --from=build /moteur/target/*.jar moteur.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "moteur.jar"]
#FROM maven:3.9.9-eclipse-temurin-11 AS BUILD
#
#WORKDIR /moteur
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package
#
#FROM openjdk:25-jdk-slim
#WORKDIR /moteur
#COPY --from=build /moteur/target/*.jar moteur.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java","-jar", "moteur.jar"]