# Build stage
FROM openjdk:22 AS build
WORKDIR /app

# Cài đặt Maven
RUN apt-get update && apt-get install -y maven

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build dự án Spring Boot, bỏ qua test để tăng tốc độ build
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:22
WORKDIR /app

# Copy file JAR từ build stage
COPY --from=build /app/target/PMS_BE-0.0.1-SNAPSHOT.jar /app/app.jar

# Mở cổng 8080
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]