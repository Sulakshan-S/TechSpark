# TechSpark – Full Stack E-Commerce Platform

TechSpark is a full-stack e-commerce web application built using React, Vite, Tailwind CSS, Spring Boot, Spring Security, JWT Authentication, and MySQL.

---

# 🚀 Technologies Used

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

# 📁 Project Structure

```bash
TechSpark/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── application.properties
│
└── frontend/
    ├── src/
    ├── package.json
    └── vite.config.js
```

---

# ✨ Features

## Customer Features
- User Registration
- User Login
- JWT Authentication
- Product Browsing
- Product Filtering
- Product Search
- Product Details
- Shopping Cart
- Wishlist
- Checkout System
- Order History
- Address Management
- Profile Management

## Admin Features
- Admin Dashboard
- Product Management
- Order Management
- User Management
- Protected Admin Routes

---

# 🖥️ Frontend Folder Structure

```bash
src/
├── assets/
├── components/
│   ├── common/
│   ├── customer/
│   └── admin/
├── contexts/
├── layouts/
├── pages/
│   ├── admin/
│   ├── auth/
│   ├── public/
│   └── user/
├── routes/
├── services/
├── config/
├── App.jsx
├── main.jsx
└── index.css
```

---

# ⚙️ Backend Folder Structure

```bash
src/main/java/com/techspark/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── security/
├── config/
└── TechSparkApplication.java
```

---

# 📌 Prerequisites

Install the following before running the project:

- Java 21
- Node.js
- Maven
- MySQL
- Git
- VS Code / IntelliJ IDEA

---

# 🛠️ Backend Setup

## 1. Navigate to Backend Folder

```bash
cd backend
```

---

## 2. Create MySQL Database

```sql
CREATE DATABASE techspark;
```

---

## 3. Configure application.properties

Open:

```bash
src/main/resources/application.properties
```

Update with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/techspark
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

---

## 4. Run Backend Server

Using Maven:

```bash
mvn spring-boot:run
```

Or using Maven Wrapper:

### Windows

```bash
./mvnw spring-boot:run
```

### Linux / Mac

```bash
./mvnw spring-boot:run
```

Backend will start on:

```bash
http://localhost:8080
```

---

# 🎨 Frontend Setup

## 1. Navigate to Frontend Folder

```bash
cd frontend
```

---

## 2. Install Dependencies

```bash
npm install
```

---

## 3. Run Frontend Development Server

```bash
npm run dev
```

Frontend will start on:

```bash
http://localhost:5173
```

---

# 🌐 API Configuration

Inside:

```bash
src/config/env.js
```

Add:

```javascript
export const API_BASE_URL = "http://localhost:8080/api";
```

---

# 🔐 Authentication Flow

1. User logs in
2. Backend validates credentials
3. JWT token generated
4. Token stored in frontend
5. Protected routes verify token
6. Authorized API requests include JWT token

---

# 📄 Available Pages

## Public Pages
- Home
- Shop
- Product Details
- Login
- Register

## User Pages
- Cart
- Wishlist
- Checkout
- Orders
- Profile
- Addresses

## Admin Pages
- Dashboard
- Manage Products
- Manage Orders
- Manage Users

---

# 🔌 API Services

Located in:

```bash
src/services/
```

Services include:

```bash
authService.js
adminService.js
userService.js
storeService.js
api.js
```

---

# 🔒 Security Features

- JWT Authentication
- Protected Routes
- Role-Based Access
- Secure REST APIs
- Spring Security Configuration

---

# ▶️ Running Full Project

## Step 1 – Start Backend

```bash
cd backend
mvn spring-boot:run
```

---

## Step 2 – Start Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

---

# 🐞 Common Errors & Fixes

## Axios Network Error

Possible reasons:

- Backend not running
- Wrong API URL
- CORS issue
- Firewall blocking requests

---

## CORS Error

Ensure backend CORS configuration allows frontend URL.

Example:

```java
@CrossOrigin(origins = "http://localhost:5173")
```

---

## MySQL Connection Error

Check:

- MySQL service running
- Database exists
- Correct username/password

---

## npm install Errors

Try:

```bash
npm cache clean --force
npm install
```

---

# 🏗️ Production Build

## Frontend Build

```bash
npm run build
```

---

## Backend Build

```bash
mvn clean install
```

Generated JAR:

```bash
target/TechSpark-0.0.1-SNAPSHOT.jar
```

Run:

```bash
java -jar target/TechSpark-0.0.1-SNAPSHOT.jar
```

---

# ☁️ Deployment

## Frontend Deployment
- Vercel
- Netlify
- GitHub Pages

## Backend Deployment
- Railway
- Render
- AWS
- DigitalOcean

## Database Hosting
- MySQL Server
- Railway MySQL
- PlanetScale

---

# 🔮 Future Improvements

- Payment Gateway
- Product Reviews
- Image Uploads
- Email Notifications
- Inventory Management
- Analytics Dashboard
- Responsive Improvements

---

# 🧾 Git Commands

## Clone Repository

```bash
git clone <repository-url>
```

---

## Create Branch

```bash
git checkout -b feature-name
```

---

## Commit Changes

```bash
git add .
git commit -m "Added new feature"
```

---

## Push Changes

```bash
git push origin main
```

---

# 👨‍💻 Author

Developed by TechSpark Team.

---

# 📜 License

This project is developed for educational and development purposes.
