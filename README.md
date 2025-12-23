# 🤖 Ứng dụng AI Agent với Tìm kiếm Internet

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M4-blue)](https://spring.io/projects/spring-ai)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue)](https://www.docker.com/)

## 🌟 Tổng quan

Đây là một ứng dụng AI Agent tinh vi được xây dựng bằng **Spring Boot** và **Spring AI**, được thiết kế để cung cấp trải nghiệm trò chuyện thông minh tích hợp khả năng tìm kiếm internet thời gian thực. Ứng dụng tận dụng các mô hình ngôn ngữ mạnh mẽ của OpenAI để cung cấp phản hồi nhận thức ngữ cảnh, được nâng cao bởi chức năng tìm kiếm web để có thông tin cập nhật.

## ✨ Tính năng

- 🔐 **Xác thực Bảo mật**: Đăng ký và đăng nhập người dùng với Spring Security
- 💬 **Trò chuyện Thông minh**: Các cuộc trò chuyện được hỗ trợ bởi AI với lịch sử cuộc trò chuyện
- 🌐 **Tích hợp Tìm kiếm Internet**: Khả năng tìm kiếm web thời gian thực để có thông tin chính xác, hiện tại
- 🗄️ **Lưu trữ Bền vững**: Cơ sở dữ liệu MySQL cho dữ liệu người dùng, cuộc trò chuyện và tin nhắn trò chuyện
- 🎨 **Giao diện Hiện đại**: Giao diện web đáp ứng dựa trên Thymeleaf
- 🐳 **Đóng gói**: Hỗ trợ Docker và Docker Compose để triển khai dễ dàng
- 🔧 **API RESTful**: Các điểm cuối API sạch cho khả năng mở rộng

## 🛠️ Công nghệ Sử dụng

### Khung Lõi
- **Spring Boot 3.4.1** - Khung ứng dụng chính
- **Java 17** - Ngôn ngữ lập trình

### AI & Trí tuệ
- **Spring AI 1.0.0-M4** - Khung tích hợp AI
- **OpenAI Spring Boot Starter** - Tích hợp API OpenAI

### Backend
- **Spring Web** - Dịch vụ web RESTful
- **Spring Data JPA** - Lưu trữ dữ liệu
- **Spring Security** - Xác thực và ủy quyền

### Cơ sở dữ liệu
- **MySQL Connector/J** - Kết nối cơ sở dữ liệu
- **MySQL 8.0** - Cơ sở dữ liệu quan hệ

### Frontend
- **Thymeleaf** - Công cụ mẫu phía máy chủ

### DevOps & Công cụ
- **Maven** - Tự động hóa xây dựng
- **Docker** - Đóng gói
- **Docker Compose** - Điều phối đa container

## 🚀 Cài đặt

### Yêu cầu tiên quyết
- Java 17 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0
- Docker (tùy chọn, cho triển khai đóng gói)

### Thiết lập Cục bộ

1. **Sao chép kho lưu trữ**
   ```bash
   git clone <repository-url>
   cd ai-agent
   ```

2. **Cấu hình Cơ sở dữ liệu**
   - Tạo cơ sở dữ liệu MySQL có tên `ai_agent`
   - Cập nhật `src/main/resources/application.properties` với thông tin đăng nhập cơ sở dữ liệu của bạn

3. **Xây dựng và Chạy**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Truy cập ứng dụng**
   - Mở trình duyệt và điều hướng đến `http://localhost:8080`

### Triển khai Docker

1. **Xây dựng và chạy với Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Truy cập ứng dụng**
   - Mở trình duyệt và điều hướng đến `http://localhost:8080`

## 📖 Cách sử dụng

1. **Đăng ký** tài khoản mới hoặc **Đăng nhập** với thông tin đăng nhập hiện có
2. **Bắt đầu cuộc trò chuyện** với AI agent
3. **Đặt câu hỏi** - AI sẽ phản hồi thông minh và sử dụng tìm kiếm internet khi cần thiết
4. **Xem lịch sử cuộc trò chuyện** - Tất cả tương tác được lưu và có thể truy xuất

## 🏗️ Kiến trúc

```
src/main/java/com/example/demo/
├── config/          # Cấu hình bảo mật và ứng dụng
├── controller/      # Bộ điều khiển REST và trình xử lý web
├── model/           # Thực thể JPA
├── repository/      # Lớp truy cập dữ liệu
└── service/         # Logic nghiệp vụ và tích hợp AI
```

## 🤝 Đóng góp

Đóng góp được chào đón! Vui lòng gửi Pull Request.

## 📄 Giấy phép

Dự án này được cấp phép theo Giấy phép MIT - xem tệp LICENSE để biết chi tiết.

---

*Được xây dựng với ❤️ sử dụng Spring Boot và Spring AI*
