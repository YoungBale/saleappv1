# BÀI THỰC HÀNH 2: PHÁT TRIỂN ỨNG DỤNG WEB VỚI JAVA SPRING BOOT

## Project: saleappv1

Dự án mẫu xây dựng ứng dụng Bán hàng trực tuyến đơn giản sử dụng **Java 17+**, **Spring Boot**, **Spring Web**, **Thymeleaf**, **Jackson (ObjectMapper)** và **Maven**. Dữ liệu sản phẩm và danh mục được lưu trữ trong các tập tin JSON.

---

## 1. Cấu trúc thư mục (Project Structure)

```text
saleappv1
├── src
│   └── main
│       ├── java
│       │   └── com.example.saleappv1
│       │       ├── Saleappv1Application.java
│       │       ├── controller
│       │       │   ├── HomeController.java
│       │       │   └── ProductController.java
│       │       ├── model
│       │       │   ├── Product.java
│       │       │   └── Category.java
│       │       └── service
│       │           └── ProductService.java
│       └── resources
│           ├── data
│           │   ├── categories.json
│           │   └── products.json
│           ├── static
│           │   ├── css
│           │   │   └── style.css
│           │   ├── js
│           │   └── images
│           ├── templates
│           │   ├── index.html
│           │   ├── products.html
│           │   └── product-detail.html
│           └── application.properties
├── pom.xml
├── README.md
└── .gitignore
```

---

## 2. Tính năng đã hiện thực (Features)

- **Trang chủ (`/`)**:
  - Tiêu đề: **"BÁN HÀNG TRỰC TUYẾN"**
  - Các liên kết:
    - *Tất cả sản phẩm* (`/products`)
    - *Điện thoại di động* (`/products?categoryId=1`)
    - *Máy tính bảng* (`/products?categoryId=2`)
- **Danh sách sản phẩm (`/products`)**:
  - Đọc và phân giải dữ liệu từ `src/main/resources/data/products.json` & `categories.json` bằng Jackson `ObjectMapper`.
  - Hiển thị đầy đủ thông tin: Hình ảnh, Tên sản phẩm, Mô tả, Giá bán.
- **Tìm kiếm và lọc sản phẩm (`/products`)**:
  - Lọc theo danh mục: `categoryId=1` (Điện thoại), `categoryId=2` (Máy tính bảng).
  - Tìm kiếm theo từ khóa tên hoặc mô tả: `keyword=...` (không phân biệt hoa thường).
  - Lọc theo khoảng giá: `fromPrice=...` và `toPrice=...`.
- **Xem chi tiết sản phẩm (`/products/{productId}`)**:
  - Lấy thông tin sản phẩm qua `@PathVariable`.
  - Hiển thị hình ảnh chi tiết, tên, mô tả, giá niêm yết, tên danh mục và bảng thông số kỹ thuật.

---

## 3. Hướng dẫn chạy ứng dụng

```powershell
# Trên Windows
.\mvnw.cmd spring-boot:run

# Trên Linux / macOS
./mvnw spring-boot:run
```

Truy cập:
- Trang chủ: http://localhost:8080/
- Danh sách sản phẩm: http://localhost:8080/products
