# Matching Engine Backend

This is a simple backend-only **Matching Engine** application built with **Spring Boot**. It supports order placement, basic asset and portfolio management, and a JWT-based authentication system. It is intended as a lightweight foundation for building trading platforms or learning about matching engine internals.


## Features

-  **Assets**: Create and manage tradable assets with pricing.
-  **Orders**: Place, modify, and match buy/sell orders.
-  **Portfolios**: Track holdings and positions per user.
-  **Authentication & Authorization**:
  - JWT-based login & register.
  - Role-based access control.
-  **Transactions**: Persist matched trades.
-  **Swagger/OpenAPI** integration for interactive API documentation.
-  **Flyway** for database versioning & migration.

---

##  Tech Stack

- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA + Hibernate**
- **H2/PostgreSQL (configurable)**
- **Flyway** – DB migration
- **Swagger/OpenAPI** – API documentation

---

##  Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Optional: PostgreSQL or run with in-memory H2 (default)

##  Authentication

This application uses **JWT (JSON Web Tokens)** for stateless authentication and role-based access control.


##  Database Migrations

The app uses **Flyway** for schema management



