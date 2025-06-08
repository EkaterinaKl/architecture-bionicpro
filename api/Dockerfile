# Production stage
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/api-1.0.jar my-app.jar

# Expose port
EXPOSE 8080

CMD ["java", "-jar", "my-app.jar"]