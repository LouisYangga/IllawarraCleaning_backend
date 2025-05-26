# Booking Service

## Overview
The Booking Service is a microservice component of the Illawarra Cleaning Project, responsible for managing cleaning service bookings. It handles booking creation, updates, and management while communicating with the User Service through RabbitMQ for user-related operations.

## Features
- Create new cleaning service bookings
- Retrieve booking details
- Update booking information
- Delete bookings
- Manage booking statuses
- Integration with User Service via RabbitMQ

## Technologies
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Docker
- Maven

## Prerequisites
- Docker and Docker Compose
- Java 17 or higher
- Maven
- PostgreSQL
- RabbitMQ

## Configuration
The service can be configured through the following files:
- `src/main/resources/application.properties` - Main application configuration
- `.env` - Environment variables
- `docker-compose.dev.yml` - Docker development configuration

## API Endpoints

### Booking Management
```
POST   /api/bookings           - Create a new booking
GET    /api/bookings           - Get all bookings
GET    /api/bookings/{id}      - Get booking by ID
GET    /api/bookings/user/{email} - Get bookings by user email
PUT    /api/bookings/{id}      - Update a booking
DELETE /api/bookings/{id}      - Delete a booking
PUT    /api/bookings/{id}/status - Update booking status
```

## Running the Service

### Development Environment
1. Start the service using Docker Compose:
```bash
docker-compose -f docker-compose.dev.yml up
```

2. The service will be available at:
```
http://localhost:8082
```

### Sample Booking Request
```json
{
  "userEmail": "test@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": 61412345678,
  "scheduledAt": "2025-05-22T10:00:00",
  "serviceType": "REGULAR_CLEANING",
  "duration": 2.5,
  "price": 150.00,
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

## Integration with User Service
The Booking Service communicates with the User Service through RabbitMQ:
- Creates/updates user information when bookings are made
- Sends user creation events via RabbitMQ
- Maintains consistency between booking and user data

## Development

### Building the Service
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```

### Docker Build
```bash
docker build -t booking-service -f DockerFile.dev .
```

## Troubleshooting
1. If RabbitMQ connection fails:
   - Verify RabbitMQ is running
   - Check connection credentials in .env file
   - Ensure RabbitMQ ports are accessible

2. If database connection fails:
   - Verify PostgreSQL is running
   - Check database credentials in .env file
   - Ensure database schema exists

## Contributing
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
