# Sử dụng Java 17 làm nền tảng
FROM eclipse-temurin:17-jdk-alpine

# Đặt working directory
WORKDIR /app

# Copy file .jar từ thư mục target vào container
COPY target/*.jar app.jar

# Expose port 8081 (port của ứng dụng)
EXPOSE 8081

# Lệnh chạy ứng dụng
ENTRYPOINT ["java","-jar","app.jar"]