# --- Stage 1: Build the JAR inside Docker ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# The -DskipTests flag is CRITICAL to avoid database errors during build
RUN mvn clean package -DskipTests

# --- Stage 2: Create the final small image ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copy the built JAR from the first stage
COPY --from=build /app/target/aeris-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]