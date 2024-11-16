# Use the official OpenJDK base image
FROM openjdk:22

# Set working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/PMS_BE-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/app.jar"]
