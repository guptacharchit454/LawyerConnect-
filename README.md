# Legal Connect (LC Connect) Monorepo

Welcome to the Legal Connect (LC Connect) workspace. This project is structured as a clean monorepo with distinct modules for the frontend and backend applications.

## Directory Structure

```
LC Connect/
├── backend/          # Enterprise Spring Boot Service
│   ├── src/          # Java source code and configuration properties
│   ├── pom.xml       # Maven build configuration
│   ├── schema.sql    # Database schema script
│   └── ...           
└── frontend/         # React Client Application
    ├── src/          # TypeScript React application
    ├── package.json  # NPM project configuration
    └── ...           
```

---

## 1. Backend Service (`backend/`)

The backend is an enterprise-level Spring Boot application configured with REST endpoints, WebSockets/STOMP, Kafka broker streaming, and Redis caching.

### Tech Stack
- **Core**: Java 21, Spring Boot 3.2.4
- **Persistence**: Spring Data JPA, PostgreSQL (using Hibernate)
- **Caching**: Spring Data Redis
- **Messaging**: Apache Kafka (Publish-Subscribe Broker)
- **WebSockets**: Spring WebSocket (STOMP Protocol)
- **Security**: Spring Security (Stateless filter chain, customized route policies)

### Getting Started (Backend)
1. Ensure PostgreSQL, Redis, and Kafka services are running. You can launch them using the Docker Compose file inside the backend directory:
   ```bash
   cd backend
   docker-compose up -d
   ```
2. Build and run tests using Maven:
   ```bash
   mvn clean test
   ```
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

---

## 2. Frontend Application (`frontend/`)

The frontend is a modern React web application built with TypeScript and Tailwind CSS, leveraging Supabase database/auth integration.

### Tech Stack
- **Framework**: React 18, TypeScript, Vite
- **Styling**: Tailwind CSS
- **Database/Auth**: Supabase PostgreSQL client and Auth

### Getting Started (Frontend)
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install npm dependencies:
   ```bash
   npm install
   ```
3. Create your `.env` file using the keys from `.env.example`.
4. Run the development server:
   ```bash
   npm run dev
   ```
