# Movin

Movin is an application to help you transfer between bank without fee.

### Architecture
![Movin Simple Architecture Diagram.png](Movin%20Simple%20Architecture%20Diagram.png)

### Services Overview
- API Gateway -> Routes requests to appropriate services
- Authentication Service -> Handles user registration and login
- Bank Service -> Provides a list of available banks and their configurations
- Common Service -> Provides shared utilities such as standardized API responses
- Discovery Server -> Service discovery and registration for microservices
- Intra Bank Transfer Service -> Handles bank transfers and transaction processing
- Notification Service -> Send notification (Currently only support via email)
- User Service -> Manages user authentication, profiles, and account settings
- Verification Service -> Handles verification for phone numbers, emails, and identities

### Tech Stack
- Spring Cloud Gateway (Eureka)
- Spring Cloud Service Discovery (Eureka)
- Spring Cloud OpenFeign
- Lombok
- Validation
- JPA (Jakarta Persistence API)
- PostgreSQL
- Redis
- Kafka

### Quick Start

### Prerequisites
Ensure you have the following installed:
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose)
- [Java 17+](https://adoptium.net)
- [Maven](https://maven.apache.org)
- [Kafka](https://kafka.apache.org)
- [PostgreSQL](https://www.postgresql.org)
- [Redis](https://redis.io)

### Installation
1. Clone the repository
```
git clone https://github.com/GtFoBAE05/Movin
```
2. Fill .env.sample and rename it to .docker.env
3. Run Docker Compose
```
docker-compose up -d
```