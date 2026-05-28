# TechSpark – Full Stack E-Commerce Platform

TechSpark is a full-stack e-commerce web application built using React + Vite for the frontend and Spring Boot for the backend. The system includes authentication, cart management, orders, product management, payments, shipment tracking, refunds, wishlist functionality, and admin management features.

---

# 📦 Technologies Used

## Frontend
- React
- Vite
- Tailwind CSS
- React Router DOM
- Axios
- Context API

## Backend
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- MySQL
- Maven
- Java 21

---

# 📁 Full Project Structure

```bash
TechSpark/
│
├── backend/
│   │
│   ├── .gitattributes
│   ├── .gitignore
│   │
│   ├── .idea/
│   │   ├── compiler.xml
│   │   ├── encodings.xml
│   │   ├── jarRepositories.xml
│   │   ├── misc.xml
│   │   └── workspace.xml
│   │
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   │
│   ├── HELP.md
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   │
│   ├── src/
│   │   │
│   │   ├── main/
│   │   │   │
│   │   │   ├── java/
│   │   │   │   │
│   │   │   │   └── com/
│   │   │   │       └── sulaks/
│   │   │   │           └── TechSpark/
│   │   │   │               │
│   │   │   │               ├── config/
│   │   │   │               │   └── SecurityConfig.java
│   │   │   │               │
│   │   │   │               ├── controller/
│   │   │   │               │   ├── AddressController.java
│   │   │   │               │   ├── AuthController.java
│   │   │   │               │   ├── BrandController.java
│   │   │   │               │   ├── CartController.java
│   │   │   │               │   ├── CategoryController.java
│   │   │   │               │   ├── CouponController.java
│   │   │   │               │   ├── NotificationController.java
│   │   │   │               │   ├── OrderController.java
│   │   │   │               │   ├── PaymentController.java
│   │   │   │               │   ├── ProductController.java
│   │   │   │               │   ├── ProductImageController.java
│   │   │   │               │   ├── ProductSpecAttributeController.java
│   │   │   │               │   ├── ProductVariantController.java
│   │   │   │               │   ├── RefundController.java
│   │   │   │               │   ├── ReturnController.java
│   │   │   │               │   ├── ReviewController.java
│   │   │   │               │   ├── ShipmentController.java
│   │   │   │               │   ├── StockMovementController.java
│   │   │   │               │   ├── UserActivityLogController.java
│   │   │   │               │   ├── VariantAttributeController.java
│   │   │   │               │   ├── VariantAttributeValueController.java
│   │   │   │               │   ├── VariantInventoryController.java
│   │   │   │               │   └── WishlistController.java
│   │   │   │               │
│   │   │   │               ├── dto/
│   │   │   │               │   ├── address/
│   │   │   │               │   ├── auth/
│   │   │   │               │   ├── brand/
│   │   │   │               │   ├── cart/
│   │   │   │               │   ├── category/
│   │   │   │               │   ├── coupon/
│   │   │   │               │   ├── notification/
│   │   │   │               │   ├── order/
│   │   │   │               │   ├── payment/
│   │   │   │               │   ├── product/
│   │   │   │               │   ├── product_img/
│   │   │   │               │   ├── product_spec_attribute/
│   │   │   │               │   ├── product_variant/
│   │   │   │               │   ├── refund/
│   │   │   │               │   ├── return_request/
│   │   │   │               │   ├── review/
│   │   │   │               │   ├── shipment/
│   │   │   │               │   ├── stock_movement/
│   │   │   │               │   ├── user_activity_log/
│   │   │   │               │   ├── variant_attribute/
│   │   │   │               │   ├── variant_attribute_value/
│   │   │   │               │   ├── variant_inventory/
│   │   │   │               │   └── wishlist/
│   │   │   │               │
│   │   │   │               ├── enums/
│   │   │   │               ├── exception/
│   │   │   │               ├── mapper/
│   │   │   │               ├── models/
│   │   │   │               ├── repository/
│   │   │   │               ├── security/
│   │   │   │               ├── service/
│   │   │   │               ├── service_impl/
│   │   │   │               ├── util/
│   │   │   │               │
│   │   │   │               └── TechSparkApplication.java
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── static/
│   │   │       └── templates/
│   │   │
│   │   └── test/
│   │       └── java/
│   │
│   └── target/
│
│
└── frontend/
    │
    ├── assets/
    │   ├── hero.png
    │   ├── react.svg
    │   └── vite.svg
    │
    ├── components/
    │   ├── common/
    │   │   ├── DataTable.jsx
    │   │   ├── EmptyState.jsx
    │   │   ├── Footer.jsx
    │   │   ├── LoadingState.jsx
    │   │   ├── Modal.jsx
    │   │   ├── Navbar.jsx
    │   │   └── SectionTitle.jsx
    │   │
    │   └── shop/
    │       ├── ProductCard.jsx
    │       └── ProductFilters.jsx
    │
    ├── config/
    │   ├── adminResources.js
    │   └── env.js
    │
    ├── contexts/
    │   ├── AuthContext.jsx
    │   └── CartContext.jsx
    │
    ├── layouts/
    │   ├── AdminLayout.jsx
    │   └── PublicLayout.jsx
    │
    ├── lib/
    │   ├── toast.js
    │   └── utils.js
    │
    ├── pages/
    │   ├── admin/
    │   │   ├── AdminOrdersPage.jsx
    │   │   ├── AdminResourcePage.jsx
    │   │   └── DashboardPage.jsx
    │   │
    │   ├── auth/
    │   │   ├── LoginPage.jsx
    │   │   └── RegisterPage.jsx
    │   │
    │   ├── public/
    │   │   ├── HomePage.jsx
    │   │   ├── ProductDetailPage.jsx
    │   │   └── ShopPage.jsx
    │   │
    │   ├── shared/
    │   │   └── NotFoundPage.jsx
    │   │
    │   └── user/
    │       ├── AddressesPage.jsx
    │       ├── CartPage.jsx
    │       ├── CheckoutPage.jsx
    │       ├── OrdersPage.jsx
    │       ├── ProfilePage.jsx
    │       └── WishlistPage.jsx
│
├── routes/
│   ├── RequireAdmin.jsx
│   └── RequireAuth.jsx
│
├── services/
│   ├── adminService.js
│   ├── api.js
│   ├── authService.js
│   ├── storeService.js
│   └── userService.js
│
├── App.jsx
├── main.jsx
└── index.css
```

---

# ✨ Main Features

## 👤 Customer Features
- User Registration
- Login & Authentication
- OTP Verification
- Product Browsing
- Product Variants
- Shopping Cart
- Wishlist
- Checkout System
- Order Tracking
- Address Management
- Product Reviews
- Return Requests
- Refund Requests

---

## 🛠️ Admin Features
- Product Management
- Category Management
- Brand Management
- Order Management
- Shipment Management
- Stock Management
- Coupon Management
- Notification Management
- User Activity Logs
- Refund Handling

---

# 🔐 Security Features

- JWT Authentication
- Spring Security
- Role-Based Authorization
- Protected Routes
- Secure REST APIs

---

# ⚙️ Backend Setup

## 1. Navigate to Backend

```bash
cd backend
```

---

## 2. Configure Database

Create database:

```sql
CREATE DATABASE techspark;
```

---

## 3. Configure application.properties

Location:

```bash
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/techspark
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

---

## 4. Run Backend

```bash
mvn spring-boot:run
```

Backend runs on:

```bash
http://localhost:8080
```

---

# 🎨 Frontend Setup

## 1. Navigate to Frontend

```bash
cd frontend
```

---

## 2. Install Dependencies

```bash
npm install
```

---

## 3. Run Frontend

```bash
npm run dev
```

Frontend runs on:

```bash
http://localhost:5173
```

---

# 🌐 API Configuration

File:

```bash
config/env.js
```

Example:

```javascript
export const API_BASE_URL = "http://localhost:8080/api";
```

---

# 🐞 Common Errors

## Axios Network Error

Possible reasons:
- Backend not running
- Wrong API URL
- CORS issue
- Incorrect port

---

## MySQL Connection Error

Check:
- MySQL service running
- Database exists
- Correct credentials

---

## Tailwind Error

Install Tailwind dependencies:

```bash
npm install -D tailwindcss postcss autoprefixer
```

---

# 🚀 Build Project

## Frontend Build

```bash
npm run build
```

---

## Backend Build

```bash
mvn clean install
```

Run jar:

```bash
java -jar target/*.jar
```

---

# ☁️ Deployment

## Frontend
- Vercel
- Netlify

## Backend
- Railway
- Render
- AWS
- DigitalOcean

---

# 👨‍💻 Author

Developed for the TechSpark project.

---

# 📜 License

This project is developed for educational and learning purposes.
