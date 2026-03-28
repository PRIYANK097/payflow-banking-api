# PayFlow — Banking & Wallet REST API

A RESTful backend API for a digital wallet application built with Spring Boot and PostgreSQL. Users can register, create a wallet account, deposit/withdraw money, and transfer funds between accounts.

Built as a hands-on learning project while preparing for Java backend developer roles — focused on writing clean, well-structured code rather than just getting things to work.

---

## Tech Stack

- **Java 17** — current LTS version
- **Spring Boot 3.2** — REST API framework
- **Spring Data JPA + Hibernate** — database layer
- **PostgreSQL 16** — relational database
- **MapStruct** — entity to DTO mapping
- **Lombok** — reduces boilerplate code
- **Bean Validation** — input validation on request DTOs
- **Swagger / OpenAPI 3.0** — API documentation
- **JUnit 5 + Mockito** — unit testing
- **Maven** — build and dependency management

---

## Project Structure

```
src/main/java/com/payflow/
├── controller/       handles HTTP requests and responses
├── service/          business logic lives here
├── repository/       database operations via JPA
├── entity/           JPA entities mapped to DB tables
├── dto/
│   ├── request/      incoming request payloads
│   └── response/     outgoing response payloads
├── mapper/           MapStruct mappers for entity <-> DTO
├── exception/        custom exceptions + global handler
└── config/           Swagger and app configuration
```

The architecture follows a strict layered pattern — controller calls service, service calls repository. No business logic in controllers, no direct DB calls outside repository layer.

---

## Database Design

Three tables — users, accounts, and transactions.

```
users
  id, full_name, email (unique), phone, created_at, updated_at

accounts
  id, user_id (FK), account_number (unique), balance (DECIMAL 10,2),
  account_type, status, created_at

transactions
  id, sender_account_id (FK), receiver_account_id (FK),
  amount (DECIMAL 10,2), transaction_type, status, description, created_at
```

balance is stored as DECIMAL(10,2) — not FLOAT or DOUBLE. Floating point types cause precision errors with money which is not acceptable in a financial application.

---

## API Endpoints

**Users**
```
POST   /api/users/register          register a new user
GET    /api/users/{id}              get user profile
PUT    /api/users/{id}              update user profile
```

**Accounts**
```
POST   /api/accounts                create wallet account
GET    /api/accounts/{id}           get account and balance
GET    /api/accounts/user/{userId}  get account by user
```

**Transactions**
```
POST   /api/transactions/deposit                  deposit money
POST   /api/transactions/withdraw                 withdraw money
POST   /api/transactions/transfer                 transfer to another account
GET    /api/transactions/history/{accountId}      paginated transaction history
GET    /api/transactions/{id}                     get single transaction
```

---

## How to Run

**Prerequisites:** Java 17, PostgreSQL 16, Maven

```bash
# clone the repo
git clone https://github.com/PRIYANK097/payflow-banking-api.git
cd payflow-banking-api

# create the database
psql -U postgres -c "CREATE DATABASE payflow_db;"

# update DB credentials in src/main/resources/application.yml

# run the app
mvn spring-boot:run
```

Swagger UI available at: `http://localhost:8080/swagger-ui/index.html`

Tables are auto-created by Hibernate on first run.

---

## Key Concepts and Why I Used Them

**DTO Pattern**
I kept request/response objects separate from JPA entities. This way the database structure and the API contract can change independently without breaking each other. It also prevents accidentally exposing fields that should stay internal.

**MapStruct**
Used for mapping between entity and DTO. It generates the mapping code at compile time so errors show up before runtime, unlike reflection-based mappers. The generated code is also plain readable Java — easy to debug.

**@Transactional on transfer**
The transfer operation debits one account and credits another. Both need to succeed or neither should. Wrapping it in @Transactional ensures the entire operation rolls back if anything fails midway — money is never lost in between.

**Global Exception Handling**
One @ControllerAdvice class handles all exceptions across the application. Every error returns the same structured JSON response instead of random stack traces or inconsistent formats.

**Pagination on transaction history**
A user could have thousands of transactions over time. Returning everything in one call would be slow and memory heavy. Pagination lets the client request one page at a time.

**Bean Validation**
Validation annotations on DTOs (@NotNull, @Email, @Min etc.) run before the request even reaches the service layer. Invalid input is rejected early with a clear error message.

---

## Running Tests

```bash
mvn test
```

Service layer is unit tested with JUnit 5 and Mockito. Repositories are mocked so tests run without a real database.

---

## What's Next

- [ ] Spring Security + JWT authentication
- [ ] Role-based access control (USER / ADMIN)
- [ ] Docker setup

---

## Author

Priyank Sharma — Java Backend Developer, Bengaluru

[LinkedIn](https://linkedin.com/in/priyank-sharma408/) · [GitHub](https://github.com/PRIYANK097)


## Build Status

### Phase 1 — Core REST API ✅ Complete
- [x] Spring Boot project setup with PostgreSQL
- [x] Entity design — User, Account, Transaction
- [x] Repository layer with Spring Data JPA
- [x] DTO pattern — request and response objects
- [x] MapStruct compile-time entity to DTO mapping
- [x] Custom exceptions with global exception handler
- [x] Bean Validation on all incoming requests
- [x] Service layer with full business logic
- [x] REST Controllers — User, Account, Transaction
- [x] Swagger / OpenAPI 3.0 documentation
- [x] End-to-end tested via Postman

### Phase 2 — Coming Next
- [ ] Spring Security + JWT authentication
- [ ] Role-based access control (USER / ADMIN)
- [ ] Password hashing with BCrypt
