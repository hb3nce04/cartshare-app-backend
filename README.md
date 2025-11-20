# 🛒 Cartshare App Backend

A **collaborative shopping list backend** that allows users to **create, share, and manage grocery lists** together.  
Designed like a to-do app, but optimized for shopping — **invite friends or family, update lists in sync, and never forget an item again!**

> 👉 This API is deployed and accessible at: **[https://cartshare-app-backend.onrender.com/api](https://cartshare-app-backend.onrender.com/api)**

> 🎓 Built as part of a **university course project** to demonstrate full-stack collaboration and REST API design.

---

## 🚀 Functionality

- 🧾 **Create and manage shopping lists** — add, edit, or delete items with ease
- 👥 **Collaborate in real time** — share lists with friends or family
- 🔄 **Instant updates** — changes reflect across all connected users
- 🔐 **User authentication with Google** — secure sign-up and login system
- 📱 **API-first architecture** — ready for integration with web or mobile clients

---

## 🗄️ Database Schema
![ER](https://github.com/hb3nce04/cartshare-app-backend/blob/main/docs/er.png?raw=true)

---

## 🧠 Tech Stack

| Layer | Technology |
|-------|-------------|
| Backend Framework | **Spring Boot, Spring ecosystem** |
| Database | **H2, PostgreSQL** |
| Authentication | **JWT (JSON Web Tokens), Google OAuth 2**
| Testing (unit, integration) | **Junit**, **Mockito**, JaCoCo |
| API Documentation | **Swagger** |
| Deployment | **Docker, Docker Compose, Render** |
| CI/CD | **Maven, GitHub Actions -> Docker Hub** |

---

## 🧰 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/hb3nce04/cartshare-app-backend
   cd cartshare-app-backend

2. **Add environment variables into docker.compose.yml**
    ```yaml
        environment:
          - GOOGLE_CLIENT_ID=your-google-client-id

3. **Run**
    ```bash
    docker compose up
