FROM maven:3-openjdk-22 AS build
WORKDIR /app

COPY . .
RUN mvn clean package -DskipTests


# Run stage

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/PMS_BE-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]