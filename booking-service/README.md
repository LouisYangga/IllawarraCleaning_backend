# Booking Service

## Overview
The Booking Service is a core microservice of the Illawarra Cleaning Project that handles booking management and quotations for cleaning services. It provides RESTful APIs for creating, managing, and tracking cleaning service bookings while integrating with other microservices.

## Features
- **Booking Management**
  - Create and manage cleaning service bookings
  - Track booking status (PENDING, CONFIRMED, CANCELLED, COMPLETED)
  - Support for different service types (REGULAR_CLEANING, DEEP_CLEANING, etc.)
  - Address validation with Google Maps API
  - Booking reference system for customer tracking
  - Multiple add-ons support with quantity tracking

- **Quotation System**
  - Real-time price calculations via pricing service
  - Support for multiple instances of the same add-on
  - Dynamic pricing based on service type and add-ons
  - Integration with pricing service via REST API

- **User Integration**
  - User event publishing to user service
  - Email-based booking retrieval
  - Role-based access control (Admin/User)
  - Reference-based booking management

## Tech Stack
- Java 17
- Spring Boot 3.2.5
- PostgreSQL (Database)
- RabbitMQ (Message Broker)
- Google Maps API (Address Validation)
- Docker & Docker Compose
- Maven

## Prerequisites
- Java 17+
- Docker and Docker Compose
- Maven 3.x
- PostgreSQL
- RabbitMQ
- Google Maps API Key

## Configuration
### Environment Variables
```properties
# Database Configuration
SPRING_DATASOURCE_URL=your-database-url
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password

# RabbitMQ Configuration
SPRING_RABBITMQ_HOST=your-rabbitmq-host
SPRING_RABBITMQ_PORT=5671
SPRING_RABBITMQ_USERNAME=your-username
SPRING_RABBITMQ_PASSWORD=your-password
SPRING_RABBITMQ_VIRTUAL_HOST=your-vhost
SPRING_RABBITMQ_SSL_ENABLED=true

# Service Integration
PRICING_SERVICE_URL=your-pricing-service-url
PRICING_SERVICE_API_KEY=your-api-key

# Google Maps Configuration
GOOGLE_MAPS_API_KEY=your-api-key

# Security
JWT_SECRET=your-jwt-secret

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080
```

## API Endpoints

### Booking Endpoints

| Method     | Endpoint                                 | Description                                         | Access         |
|------------|-------------------------------------------|-----------------------------------------------------|---------------|
| POST       | `/api/bookings`                          | Create a new booking                                | Public        |
| GET        | `/api/bookings`                          | Get all bookings                                    | Admin         |
| GET        | `/api/bookings/{id}`                     | Get booking by ID                                   | Admin         |
| GET        | `/api/bookings/user/{email}`             | Get bookings by user email                          | Public        |
| GET        | `/api/bookings/status/{status}`          | Get bookings by status                              | Admin         |
| GET        | `/api/bookings/date-range?start=...&end=...` | Get bookings within a date range                 | Admin         |
| PUT        | `/api/bookings/{id}`                     | Update booking by ID                                | Admin         |
| PATCH      | `/api/bookings/{id}/status?status=...`   | Update booking status by ID                         | Admin         |
| DELETE     | `/api/bookings/{id}`                     | Delete booking by ID                                | Admin         |
| GET        | `/api/bookings/reference/{reference}`     | Get booking by reference                            | Public        |
| GET        | `/api/bookings/admin/lookup/{reference}`  | Admin lookup booking by reference                   | Admin         |
| PUT        | `/api/bookings/reference/{reference}`     | Update booking by reference (user)                  | Public        |
| GET/HEAD   | `/api/bookings/health`                   | Service health and uptime                           | Public        |

### Quotation Endpoints

| Method     | Endpoint                                 | Description                                         | Access         |
|------------|------------------------------------------|-----------------------------------------------------|---------------|
| POST       | `/api/quotations`                        | Create a new quotation and return details           | Public        |
| GET        | `/api/quotations/{quotationId}`          | Retrieve a quotation by its ID                      | Public        |

## Deployment

### Local Development
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Docker Deployment
```bash
# Build the Docker image
docker build -t booking-service -f DockerFile .

# Run independently
docker run -d \
  --name booking-service \
  -p 8082:8082 \
  --env-file .env \
  booking-service

# Or with Docker Compose
docker-compose -f docker-compose.dev.yml up -d
```

## Service Dependencies
- **User Service** - User management and authentication
- **Pricing Service** - Price calculations and quotations
- **RabbitMQ** - Message broker for service communication
- **PostgreSQL** - Primary database
- **Google Maps API** - Address validation

## Error Handling
The service includes comprehensive error handling for:
- Invalid bookings
- Address validation failures
- Pricing service communication errors
- Authentication/Authorization errors
- Database constraints
- CORS and security restrictions

## Monitoring and Maintenance
- **Service health endpoint:** `/api/bookings/health`
  - Supports both `GET` and `HEAD` requests.
  - `GET` returns a JSON body with service status, uptime, and memory info.
  - `HEAD` returns only headers (no body), as per HTTP specification.
  - This endpoint is public and can be used with monitoring tools like UptimeRobot to check service availability and prevent server spin-down.

## License
This project is licensed under the MIT License - see the LICENSE file for details
