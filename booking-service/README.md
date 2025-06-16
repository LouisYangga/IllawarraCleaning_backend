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

- **Quotation System**
  - Real-time price calculations via pricing service
  - Integration with RabbitMQ for service communication
  - Support for cleaning service add-ons

- **User Integration**
  - User event publishing to user service
  - Email-based booking retrieval
  - Role-based access control (Admin/User)

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

# Google Maps Configuration
GOOGLE_MAPS_API_KEY=your-api-key

# Security
JWT_SECRET=your-jwt-secret
```

## API Endpoints

### Booking Endpoints

| Method | Endpoint                                 | Description                        | Access         |
|--------|------------------------------------------|------------------------------------|----------------|
| POST   | `/api/bookings`                          | Create new booking                 | User/Admin     |
| GET    | `/api/bookings`                          | Get all bookings                   | Admin          |
| GET    | `/api/bookings/{id}`                     | Get booking by ID                  | Admin          |
| GET    | `/api/bookings/user/{email}`             | Get user's bookings                | User/Admin     |
| GET    | `/api/bookings/status/{status}`          | Get bookings by status             | Admin          |
| GET    | `/api/bookings/date-range`               | Get bookings in date range         | Admin          |
| GET    | `/api/bookings/admin/lookup/{reference}`          | Get bookings with ID by reference             | Admin          |
| PUT    | `/api/bookings/{id}`                     | Update booking                     | Admin          |
| PATCH  | `/api/bookings/{id}/status`              | Update booking status              | Admin          |
| DELETE | `/api/bookings/{id}`                     | Delete booking                     | Admin          |
| GET    | `/api/bookings/reference/{ref}`          | Get booking by reference           | User/Admin     |
| PUT    | `/api/bookings/reference/{ref}`          | Update booking by reference        | User/Admin     |

### Quotation Endpoints

| Method | Endpoint                  | Description           | Access     |
|--------|---------------------------|-----------------------|------------|
| POST   | `/api/quotations`         | Create quotation      | User/Admin |
| GET    | `/api/quotations/{id}`    | Get quotation by ID   | User/Admin |

## Running the Service

### Local Development
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Docker Development
```bash
# Build the Docker image
docker build -t booking-service -f DockerFile.dev .

# Run with Docker Compose
docker-compose -f docker-compose.dev.yml up
```

## Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify
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
- Quotation errors
- Authentication/Authorization errors
- Database constraints

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details
