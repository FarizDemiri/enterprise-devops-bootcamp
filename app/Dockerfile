# ===============================
# Stage 1: Build the Application
# ===============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml first to cache dependencies
COPY pom.xml .

# Go offline to cache dependencies (this makes subsequent builds faster)
RUN mvn dependency:go-offline

# Copy the actual source code
COPY src ./src

# Build the jar (skip tests here if you want faster builds, but for this demo we run them)
RUN mvn clean package -DskipTests=false

# ===============================
# Stage 2: Create the Runtime Image
# ===============================
FROM eclipse-temurin:17-jre-alpine

# Create a non-root user for security (Best Practice!)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copy only the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080 (default Spring Boot port)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
