# Étape de build Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /fronted

# Copie pom.xml + téléchargement des dépendances en cache
COPY pom.xml .

RUN mvn dependency:go-offline

# Copier tout le projet (src + pom.xml)
COPY . .

# Compiler et packager le projet sans les tests
RUN mvn clean package -DskipTests

# Étape d'exécution
FROM eclipse-temurin:17-jre-alpine

WORKDIR /fronted

# Copier le jar généré depuis l'étape BUILD
COPY --from=build /fronted/target/*.jar fronted.jar

ENTRYPOINT ["java", "-jar", "fronted.jar"]