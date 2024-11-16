FROM openjdk:22

WORKDIR /app

COPY target/PMS_BE-0.0.1-SNAPSHOT.jar /app/PMS_BE-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "PMS_BE-0.0.1-SNAPSHOT.jar"]
