FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
ENV SPRING_DEVTOOLS_RESTART_ENABLED=true
ENV SPRING_DEVTOOLS_ADD_PROPERTIES=true
ENTRYPOINT ["java", "-Dspring.devtools.restart.enabled=true", "-Dspring-boot.run.jvmArguments='-Dspring.devtools.restart.enabled=true'", "-jar", "app.jar"]
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring-boot.run.jvmArguments='-Dspring.devtools.restart.enabled=true'", "app.jar"]
