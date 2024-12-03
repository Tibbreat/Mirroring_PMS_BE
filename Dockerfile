# Stage 1: Build ứng dụng
FROM maven:3-eclipse-temurin-22 AS build
WORKDIR /app

# Copy toàn bộ mã nguồn và file cấu hình vào container
COPY . .

# Build ứng dụng Spring Boot, bỏ qua test để tăng tốc độ
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng
FROM eclipse-temurin:22
WORKDIR /app

# Cài đặt múi giờ cho container
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Copy file JAR đã build từ stage 1
COPY --from=build /app/target/PMS_BE-0.0.1-SNAPSHOT.jar /app/app.jar

# Mở cổng 8080 cho ứng dụng
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
