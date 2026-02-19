# Use Java 17 Alpine
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy JAR from Maven target folder
COPY target/movies-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
