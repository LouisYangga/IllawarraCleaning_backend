# Booking Service

## Overview
The Booking Service is a microservice component of the Illawarra Cleaning Project, responsible for managing cleaning service bookings and quotations. It handles the entire booking lifecycle, from initial quotation to booking completion, while communicating with other services.

## Features
- **Quotation Management**
  - Get price quotes for cleaning services
  - Cache quotations for 30 minutes
  - Convert quotations to bookings
- **Booking Management**
  - Create new cleaning service bookings
  - Retrieve and search bookings
  - Update booking information
  - Manage booking statuses
- **Integration Features**
  - Price calculation via Price Service
  - User management via RabbitMQ
  - Cached quotation system

## Technologies
- Java 17
- Spring Boot 3.2.5
- Spring Cloud OpenFeign
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Caffeine Cache
- Docker
- Maven

## Prerequisites
- Docker and Docker Compose
- Java 17 or higher
- Maven 3.x
- PostgreSQL
- RabbitMQ

## API Endpoints

### Quotation Endpoints
```
POST   /api/quotations         - Create a new quotation
GET    /api/quotations/{id}    - Get quotation by ID
```

### Booking Endpoints
```
POST   /api/bookings          - Create a new booking
GET    /api/bookings          - Get all bookings
GET    /api/bookings/{id}     - Get booking by ID
GET    /api/bookings/user/{email} - Get bookings by user email
GET    /api/bookings/status/{status} - Get bookings by status
GET    /api/bookings/date-range - Get bookings within date range
PUT    /api/bookings/{id}     - Update a booking
PATCH  /api/bookings/{id}/status - Update booking status
DELETE /api/bookings/{id}     - Delete a booking
```

## Sample Requests

### Create Quotation
```json
{
  "serviceType": "REGULAR_CLEANING",
  "duration": 2.5,
  "addons": ["WINDOW_CLEANING", "CARPET_CLEANING"]
}
```

### Create Booking with Quotation
```json
{
  "userEmail": "test@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": 61412345678,
  "scheduledAt": "2025-05-22T10:00:00",
  "quotationId": "uuid-from-quotation",
  "notes": "Please bring eco-friendly cleaning products",
  "address": {
    "street": "123 Main Street",
    "city": "Wollongong",
    "state": "NSW",
    "postalCode": "2500",
    "country": "Australia"
  }
}
```

## Configuration
Configuration is managed through:
- `application.properties` - Main application settings
- `.env` - Environment variables
- `docker-compose.yml` - Container configuration

### Key Properties
```properties
# Application
spring.application.name=booking-service
server.port=8082

# Database
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

# RabbitMQ
spring.rabbitmq.host=${SPRING_RABBITMQ_HOST}
spring.rabbitmq.port=${SPRING_RABBITMQ_PORT}
```

## Development

### Building the Service
```bash
mvn clean package
```

### Running Tests
```bash
mvn test
```

### Docker Build
```bash
docker build -t booking-service -f DockerFile.dev .
```

### Docker Compose
```bash
docker-compose up -d
```

## Troubleshooting

### Common Issues
1. **Quotation Not Found**
   - Quotations expire after 30 minutes
   - Verify quotationId is correct
   - Create a new quotation if expired

2. **RabbitMQ Connection**
   - Check RabbitMQ is running
   - Verify credentials in .env
   - Ensure ports are accessible

3. **Database Connection**
   - Verify PostgreSQL is running
   - Check database credentials
   - Ensure schema exists

## Contributing
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License.
