# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copy source code and build
COPY src src
RUN ./mvnw clean package -DskipTests

# Stage 2: Production image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Add a non-root user for security
RUN useradd -m spring

# Copy jar from builder
COPY --from=builder /app/target/*.jar app.jar
RUN chown spring:spring app.jar

USER spring

# Optional: set environment profile
ENV SPRING_PROFILES_ACTIVE=prod

# Expose port
EXPOSE ${SERVER_PORT:-8082}

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
