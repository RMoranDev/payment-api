# Payments API

A REST API built with Java for customer registration and payment lifecycle management. The project is under active development and serves as a practical, domain-oriented backend learning project.

## Current Stage

The application core and persistence layer are implemented. The customer flow and the first version of the payment creation flow are available through the API. Automated test coverage for payment business rules is the next delivery.

### Implemented

- Customer registration, lookup by ID, and partial updates.
- Individual (`INDIVIDUAL`/CPF) and business (`BUSINESS`/CNPJ) customers.
- Email and document normalization, with duplicate-email and duplicate-document protection.
- JPA entities, Spring Data repositories, and domain rules for customers and payments.
- Payment creation through `POST /api/v1/payments`.
- Request handling through `CreatePaymentRequest`, with Bean Validation.
- Customer existence and `ACTIVE` status validation before payment creation.
- New payments created with the initial `PENDING` status and persisted through `PaymentRepository`.
- Payment entity conversion to `PaymentResponse`.
- Payment domain transitions: approval, rejection, and cancellation.
- PostgreSQL persistence versioned with Flyway, including constraints, keys, and indexes.
- Initial global handling for customer-not-found and empty-update-request errors.
- Context-loading test with PostgreSQL through Testcontainers.

### Not Yet Exposed by the API

- Payment lookup and payment status-transition endpoints.
- Customer status updates.
- Listing with pagination and filters.
- OpenAPI/Swagger documentation, authentication/authorization, and containerized deployment.

## Technologies

- Java 21
- Spring Boot 4
- Spring Web MVC, Spring Data JPA, and Bean Validation
- PostgreSQL and Flyway
- Maven and Lombok
- JUnit 6 and Mockito (planned for payment service tests)
- Testcontainers
- Spring Boot Actuator (dependency already added)

## Architecture

The code is organized by domain. Each module groups its contracts, business rules, and data access, while cross-cutting concerns remain in `shared`.

```text
src/main/java/dev/rmoran/paymentapi
├── customer/       # customer API, DTOs, entity, service, and repository
├── payment/        # payment API, DTOs, entity, service, and repository
└── shared/          # error handling and shared components

src/main/resources
├── application.yaml
└── db/migration/    # Flyway migrations
```

## Domain Model

A customer has a CPF or CNPJ, a unique email address, and an `ACTIVE` or `INACTIVE` status. A payment belongs to a customer and records its amount, currency, description, method, and status.

| Field | Current values |
| --- | --- |
| Customer type | `INDIVIDUAL`, `BUSINESS` |
| Customer status | `ACTIVE`, `INACTIVE` |
| Payment method | `PIX`, `CREDIT_CARD`, `DEBIT_CARD` |
| Payment status | `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED` |
| Stored currency | `BRL` |

## Prerequisites

- JDK 21
- PostgreSQL running, with a database named `payments_db`
- Docker, if you run integration tests with Testcontainers

## Run Locally

Create the database and provide the credentials through environment variables. In PowerShell:

```powershell
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "your-password"
```

Then, from the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

The application starts at `http://localhost:8080`. On startup, Flyway applies the migrations in `src/main/resources/db/migration` and Hibernate validates the schema.

On Unix environments, use `./mvnw spring-boot:run`.

## Available Endpoints

Base URL: `http://localhost:8080/api/v1`

| Method | Route | Description |
| --- | --- | --- |
| `POST` | `/customers` | Creates a customer. |
| `GET` | `/customers/{id}` | Retrieves a customer by UUID. |
| `PATCH` | `/customers/{id}` | Updates a customer name and/or email. |
| `POST` | `/payments` | Creates a payment for an active customer. |

### Create a Customer

```http
POST /api/v1/customers
Content-Type: application/json

{
  "fullName": "Ana Silva",
  "email": "ana.silva@example.com",
  "document": "12345678901"
}
```

For a business customer, provide a 14-digit CNPJ in the `document` field.

### Update a Customer

```http
PATCH /api/v1/customers/{id}
Content-Type: application/json

{
  "email": "ana.nova@example.com"
}
```

### Create a Payment

```http
POST /api/v1/payments
Content-Type: application/json

{
  "customerId": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 150.00,
  "currency": "BRL",
  "description": "Order payment",
  "paymentMethod": "PIX"
}
```

New payments are created with the `PENDING` status. The endpoint validates the request and verifies that the customer exists and is `ACTIVE`.

## Testing

```powershell
.\mvnw.cmd test
```

The context test uses Testcontainers to start PostgreSQL. Docker must therefore be available before running it.

## Next Steps

1. **Add automated tests for `PaymentService`** — use JUnit 6 and Mockito to cover successful and failing payment-creation scenarios.
2. **Complete the payment API** — expose payment lookup and status transitions with consistent responses and auditing rules.
3. **Strengthen HTTP contracts** — standardize error responses and HTTP status codes, and document the API with OpenAPI.
4. **Complete customer operations** — provide status changes, listing, pagination, and filters.
5. **Prepare for production** — configure authentication and authorization, metrics/health checks, structured logging, Docker/Compose, CI, and an environment-configuration strategy.

## Author

Ricardo Moran — [RMoranDev](https://github.com/RMoranDev)
