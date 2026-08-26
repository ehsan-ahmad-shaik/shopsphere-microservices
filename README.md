# ShopSphere Microservices

ShopSphere is a Spring Boot based e-commerce microservices application.

The project follows a microservices architecture where different business functionalities are separated into independent services.

---

## Architecture

The application contains the following services:

- Config Server
- Eureka Server
- API Gateway
- Admin Server
- Zipkin Server
- User Service
- Product Service
- Order Service

---

## Technologies

- Java
- Spring Boot
- Spring Cloud
- Spring Cloud Config
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Spring Boot Admin
- OpenFeign
- Resilience4j
- Zipkin
- Spring Data JPA
- MySQL
- ModelMapper
- Lombok
- Jakarta Validation
- Maven

---

## Microservices

### Config Server

Centralized configuration management for all microservices.

### Eureka Server

Service discovery and registration for the microservices.

### API Gateway

Single entry point for client requests and routes requests to the appropriate microservice.

### Admin Server

Provides monitoring and management of Spring Boot microservices using Spring Boot Admin.

### Zipkin Server

Provides distributed tracing for requests flowing between microservices.

Zipkin helps to track:

- Request flow
- Service-to-service communication
- Request duration
- Service dependencies
- Distributed tracing information

### User Service

Responsible for:

- Creating users
- Getting user by ID
- Getting user by email
- Getting all users
- Updating users
- User validation

### Product Service

Responsible for:

- Creating products
- Getting products
- Updating products
- Product validation
- Category-based product search
- Product quantity management

### Order Service

Responsible for:

- Creating orders
- Getting orders
- Getting orders by user
- Updating order status
- Cancelling orders
- Deleting orders
- Calculating order subtotal
- Calculating total order amount
- Validating product availability

The Order Service communicates with User Service and Product Service using OpenFeign.

---

## Architecture Flow

```text
                         +----------------+
                         |     Client     |
                         |   Postman/Web  |
                         +-------+--------+
                                 |
                                 v
                         +----------------+
                         |  API Gateway   |
                         |     :9090      |
                         +-------+--------+
                                 |
              +------------------+------------------+
              |                  |                  |
              v                  v                  v
       +-------------+    +-------------+    +-------------+
       | User Service|    |Product      |    |Order Service|
       |    :8081    |    |Service      |    |    :8083   |
       +-------------+    |    :8082    |    +------+------+
                          +-------------+           |
                                                   |
                                      +------------+------------+
                                      |                         |
                                      v                         v
                               +-------------+           +-------------+
                               |User Service |           |Product      |
                               |    :8081    |           |Service      |
                               +-------------+           |    :8082    |
                                                         +-------------+

                         +-------------------+
                         |   Eureka Server   |
                         |       :8761       |
                         +-------------------+
                                  |
                           Service Discovery

                         +-------------------+
                         |   Config Server   |
                         |       :8888       |
                         +-------------------+
                                  |
                       Centralized Configuration

                         +-------------------+
                         |    Admin Server   |
                         |       :8080       |
                         +-------------------+
                                  |
                            Monitoring

                         +-------------------+
                         |    Zipkin Server  |
                         |       :9411       |
                         +-------------------+
                                  |
                       Distributed Tracing
