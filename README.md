# LawyerConnect Platform

Welcome to the LawyerConnect. This project is structured as a clean monorepo with distinct modules for the backend applications.

## Directory Structure

```
LC Connect/
├── backend/          # Enterprise Spring Boot Service
│   ├── src/          # Java source code and configuration properties
│   ├── pom.xml       # Maven build configuration
│   ├── schema.sql    # Database schema script
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

