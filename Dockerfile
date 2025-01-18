# Utiliser une image Java comme base
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers pom.xml et src
COPY pom.xml .
COPY src ./src
# Installer les dépendances
RUN mvn clean install -DskipTests

# Utiliser une image alpine pour servir l'application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8086

CMD ["java","-jar","app.jar"]