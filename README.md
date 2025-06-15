Here is a `README.md` for your project, based on all the code you've shared:

---

````markdown
# 🛡️ Rest Foundation Demo

A modular and extensible Java Spring Boot application for executing external REST API requests with automatic audit logging, provider-specific authentication, and a strategy-based execution model.

## ✨ Features

- ✅ Strategy pattern for REST API request execution
- 🔐 Token-based authentication (decorated via `TokenManager`)
- 🧾 Persistent API audit trail with request/response metadata
- 🔍 Dynamic support for multiple providers via `TokenManagerFactory`
- 🧼 Sensitive data masking before logging or persisting
- 🌐 Extensible provider helper structure (`ProviderXRestHelper`)
- 📊 Clean, logged request lifecycle with response validation

---

## 🧱 Project Structure

```text
.
├── model/
│   └── Provider.java
│   └── ApiAuditTrail.java
│
├── dto/
│   └── ProviderXRestDto.java
│
├── service/
│   ├── ApiAuditTrailService.java
│   ├── ApiAuditTrailServiceImpl.java
│   └── TokenManager.java
│
├── strategy/
│   ├── RestRequestStrategy.java
│   ├── ProviderXCheckAccount.java
│   └── ProviderXTokenDecoratedStrategy.java
│
├── factory/
│   └── TokenManagerFactory.java
│
├── helper/
│   └── RestHelper.java
│   └── ProviderXRestHelper.java
│
├── repository/
│   └── ApiAuditTrailRepository.java
│
├── token/
│   └── ProviderXTokenManager.java
│
└── RestFoundationDemoApplication.java
````

---

## 🚀 How It Works

1. **Define a Request Strategy**
   Create a class implementing `RestRequestStrategy<REQ, RES>` for a specific provider (e.g., `ProviderXCheckAccount`).

2. **Decorate with Token Logic**
   Use `ProviderXTokenDecoratedStrategy` to inject tokens and modify the base URL before execution.

3. **Execute with Helper**
   Call `ProviderXRestHelper.execute(...)`, which delegates to `RestHelper`.

4. **Audit Logging**
   Every request/response is logged and saved into `api_audit_trail_tbl`, with sensitive fields masked.

---

## 🧪 Sample Flow (Provider X)

```java
RestRequestStrategy<CheckAccountReqDto, CheckAccountRespDto> strategy = new ProviderXCheckAccount();
providerXRestHelper.execute(strategy);
```

* Token is fetched via `ProviderXTokenManager`
* Request is wrapped with auth headers
* Response is validated for `statusCode: SUCCESS`
* Audit record is saved with masked request/response

---

## 📄 API Audit Trail Schema

| Field           | Description                       |
| --------------- | --------------------------------- |
| id              | Primary key                       |
| correlationId   | Request correlation ID            |
| method          | HTTP method                       |
| url             | Request URL                       |
| requestHeaders  | Serialized and sanitized headers  |
| requestBody     | Serialized and sanitized body     |
| responseStatus  | HTTP status code                  |
| responseBody    | Serialized and sanitized response |
| responseHeaders | Response headers                  |
| status          | `SUCCESS` or `FAILED`             |
| errorMessage    | Error details if any              |
| durationMs      | Execution time in ms              |
| createdAt       | Timestamp                         |
| createdBy       | Auto-set by JPA auditing          |

---

## 🧰 Tech Stack

* **Java 17+**
* **Spring Boot**
* **Spring Data JPA**
* **Lombok**
* **SLF4J Logging**
* **Jackson (for JSON parsing)**
* **PostgreSQL / MySQL** (any JPA-supported DB)

---

## 🧼 Sensitive Fields Masking

These fields are masked in logs/audit:

* `password`
* `token`
* `secret`
* `authorization`
* `apiKey`

---

## 🧑‍💻 Author

Sharif
🔗 [https://github.com/sharifrahim](https://github.com/sharifrahim)

---

## 📌 TODO

* [ ] Implement `ProviderXTokenManager.getToken()`
* [ ] Add support for additional providers in `TokenManagerFactory`
* [ ] Externalize base URLs via `application.yml`

---

## 🏁 Getting Started

```bash
# Build and run
./mvnw spring-boot:run

# API behavior is executed programmatically, not exposed as controller (yet)
```

---

## 🧩 Extending for New Providers

To support a new provider:

1. Create a new `Enum` in `Provider.java`
2. Implement `TokenManager`
3. Add to `TokenManagerFactory`
4. Define a `RestRequestStrategy` for your API endpoint
5. Optionally create a `RestHelper` wrapper class (like `ProviderXRestHelper`)

---

