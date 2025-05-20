# ---------- Build Stage ----------
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# Copy Maven wrapper and dependencies
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# ---------- Production Stage ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Copy jar from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Set file permissions
RUN chown spring:spring /app/app.jar
USER spring

# Use production profile
ENV SPRING_PROFILES_ACTIVE=prod

# Expose application port
EXPOSE 8080

# Health check (optional)
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl --fail http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
