# Pricing Service

## Overview
The Pricing Service is a dedicated microservice within the Illawarra Cleaning Project responsible for handling price calculations for cleaning services. It processes pricing requests through RabbitMQ and maintains standardized pricing rules for different service types and add-ons.

## Features
- **Dynamic Price Calculation**
  - Base price calculation for different service types
  - Add-on pricing support
  - Duration-based cost calculation
  - Service type specific pricing

- **Service Type Support**
  - Regular cleaning
  - Deep cleaning
  - Move in/out cleaning
  - Office cleaning

- **Add-on Services**
  - Fridge cleaning
  - Oven cleaning
  - Window cleaning
  - Cabinet cleaning

## Tech Stack
- Java 17
- Spring Boot 3.2.5
- PostgreSQL (Database)
- RabbitMQ (Message Broker)
- Maven
- Docker & Docker Compose

## Prerequisites
- Java 17+
- Docker and Docker Compose
- Maven 3.x
- PostgreSQL
- RabbitMQ

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

# Server Configuration
SERVER_PORT=8084
```

## API Endpoints

### Price Calculation
| Method | Endpoint                        | Description                                      | Access |
|--------|---------------------------------|--------------------------------------------------|------------------|
| POST   | `/api/prices/calculate`         | Calculate price for a service                    | USER/ADMIN             |
| GET    | `/api/prices/service-types`     | Get all service types with base prices           | USER/ADMIN             |
| GET    | `/api/prices/addons`            | Get all available add-ons with prices            | USER/ADMIN             |
| POST   | `/api/prices/service-types`     | Add a new service type price to the database     | ADMIN            |
| POST   | `/api/prices/addons`            | Add a new add-on price to the database           | ADMIN            |

## Message Queue Topics
- `quotation.calculation` - Receive quotation calculation requests
- `quotation.response` - Send quotation calculation responses

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
docker build -t pricing-service -f DockerFile.dev .

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

## Database Schema

### Service Prices Table
```sql
CREATE TABLE service_prices (
    id BIGSERIAL PRIMARY KEY,
    service_type VARCHAR(50) NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    hourly_rate DECIMAL(10,2) NOT NULL,
    description TEXT
);
```

### Addon Prices Table
```sql
CREATE TABLE addon_prices (
    id BIGSERIAL PRIMARY KEY,
    addon_type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT
);
```

## Price Calculation Logic
The service calculates prices based on:
1. Base price for service type
2. Duration-based cost (hourly rate × duration)
3. Square footage adjustments
4. Add-on costs
5. Any applicable discounts

## Error Handling
- Invalid service type errors
- Add-on validation errors
- Price calculation errors
- Database errors
- Message queue errors

## Service Integration
- **Booking Service** - Receives quotation requests and price calculations
- **RabbitMQ** - Handles asynchronous communication
- **PostgreSQL** - Stores pricing data and rules

## Monitoring and Logging
- Service health endpoints
- RabbitMQ connection status
- Price calculation metrics
- Error rate monitoring
- Request/Response timing

## License
This project is licensed under the MIT License - see the LICENSE file for details
