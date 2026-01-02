# 🚀 User Management System API

This project provides a robust backend for user identity and management, powered by **Spring Boot 3** and **PostgreSQL**. The API is fully documented using **OpenAPI 2.5**.

---

## 📖 API Documentation

The documentation is split into logical groups. You can use the links below to access the interactive UI or the raw JSON specifications.

### 🔐 1. Authentication Module
*Manages the security lifecycle: Registration, Login, Logout, and JWT Token Refresh.*

* **Interactive UI:** [Authentication Dashboard](http://localhost:8080/api/v1/swagger-ui/index.html?urls.primaryName=Authentication)
* **Raw Spec:** [`JSON Definition`](http://localhost:8080/api/v1/v3/api-docs/Authentication)

### 👥 2. User Management Module
*Handles core user operations, profile updates, and administrative CRUD tasks.*

* **Interactive UI:** [User Management Dashboard](http://localhost:8080/api/v1/swagger-ui/index.html?urls.primaryName=User-Management)
* **Raw Spec:** [`JSON Definition`](http://localhost:8080/api/v1/v3/api-docs/User-Management)

---

## 🛠️ Access Summary

| Component | Endpoint | Description |
| :--- | :--- | :--- |
| **Main Gateway** | `http://localhost:8080/api/v1` | Base URL for all API calls |
| **Swagger UI** | `/swagger-ui/index.html` | Visual interface for testing |
| **OpenAPI Docs** | `/v3/api-docs` | Raw JSON for Postman/Insomnia import |



---

## 🔑 How to Test Authenticated Routes

1.  **Register/Login:** Use the `Authentication` group to get an `accessToken`.
2.  **Authorize:** Click the green **Authorize** button at the top of the Swagger page.
3.  **Enter Token:** Paste your JWT (do not include the "Bearer " prefix; the UI adds it automatically).
4.  **Execute:** You can now test the `User Management` endpoints directly from the browser.

---

### 🚀 Technical Stack
* **Java:** 17
* **Framework:** Spring Boot 3.3.4
* **Security:** Spring Security (JWT Stateless)
* **Database:** PostgreSQL
* **Docs:** SpringDoc OpenAPI 2.5.0
