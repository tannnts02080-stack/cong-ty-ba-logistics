# Hệ Thống Quản Lý Giao Hàng & Khách Hàng (Công Ty Ba)

Dự án quản lý thông tin khách hàng, lưu lịch sử vận chuyển (số kg, đơn giá vận chuyển), và phân quyền hệ thống (Admin/Nhân viên). Hệ thống được xây dựng trên nền tảng **Java Web** (Tomcat 10 - Jakarta Servlet).

## 🚀 Công nghệ sử dụng
- **Backend:** Java 17, Servlet JSP (Jakarta EE)
- **Cơ sở dữ liệu:** Microsoft SQL Server
- **Build Tool:** Maven

## 🗄️ Cấu trúc Database (Sơ lược)
Hệ thống bao gồm các bảng chính:
- `users`: Quản lý tài khoản đăng nhập (Admin, Shipper).
- `customers`: Quản lý danh sách khách hàng gửi hàng.
- `shipping_price`: Bảng giá tính theo `kg` ứng với từng khách hàng.
- `shipping_log`: Ghi chép lịch sử gửi hàng và chốt sổ giá (business_date).

## 🔧 Hướng dẫn Cài đặt & Khởi chạy

### 1. Chuẩn bị Cơ sở dữ liệu
* Tạo database `CONG_TY` trong SQL Server.
* Chạy Script SQL đi kèm (nếu có lưu script database) hoặc Execute file backup DB.

### 2. Cấu hình kết nối SQL (DBConnect.java)
Cấp lại chuỗi đăng nhập SQL cho chuẩn tại đường dẫn: 
`src/main/java/utils/DBConnect.java`
```java
private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=CONG_TY;encrypt=true;trustServerCertificate=true";
private static final String USER = "sa";        // Hoặc user SQL của bạn
private static final String PASS = "123456";    // Cập nhật lại mật khẩu SA ở đây
```

### 3. Deploy trên Tomcat
* Add project vào **Eclipse IDE**.
* Cấu hình Eclipse liên kết với Server **Apache Tomcat 10.x** (Bắt buộc dùng bản 10+ vì hệ thống dùng `jakarta` thay cho `javax`).
* Chuột phải vào Project -> Run As -> Run on Server.

### 3. Deploy trên Tomcat
* ADMIN: Tam/123
* SHIPPER: Tan/123
---

*Lưu ý: Mọi đóng góp xin gửi Pull Request thông qua Github.*
