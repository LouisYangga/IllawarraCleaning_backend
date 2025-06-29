# User Service

## Overview
The User Service is a microservice component of the Illawarra Cleaning Project that handles user management and works in conjunction with the Booking Service. It manages user profiles, tracks booking counts, and processes user-related events received through RabbitMQ.

## Features
- User profile management (CRUD operations)
- JWT-based authentication and authorization
- API key validation for service-to-service communication
- Role-based access control (ADMIN/USER)
- Integration with Booking Service via RabbitMQ
- Automatic user creation from booking events
- Booking count tracking per user
- Email-based user lookup

## Tech Stack
- Java 17
- Spring Boot 3.2.5
- Spring Security
- Spring Data JPA
- PostgreSQL (Database)
- RabbitMQ (Message Queue)
- JWT (Authentication)
- Docker & Docker Compose
- Maven

## Base URL
```
/api
```

## Authentication & Authorization

All API endpoints (except specifically excluded ones) require:
- API Key authentication via interceptor for all `/api/**` paths
- JWT authentication for protected routes

## Configuration

### Environment Variables (.env)
```properties
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host/user-service
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password

# RabbitMQ Configuration
SPRING_RABBITMQ_HOST=your-rabbitmq-host
SPRING_RABBITMQ_PORT=5671
SPRING_RABBITMQ_USERNAME=your-username
SPRING_RABBITMQ_PASSWORD=your-password
SPRING_RABBITMQ_VIRTUAL_HOST=your-vhost
SPRING_RABBITMQ_SSL_ENABLED=true

# Security Configuration
JWT_SECRET=your-jwt-secret-key
API_KEY=your-api-key

# Server Configuration
SERVER_PORT=8080
```

### Application Properties
Key configuration in `application.properties`:
```properties
# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Security Configuration
spring.security.user.name=none
spring.security.user.password=none
```

## Message Queue Integration
### RabbitMQ Configuration
```properties
rabbitmq.queue.user.name=user-creation-queue
rabbitmq.exchange.name=booking-exchange
rabbitmq.routing.user.key=user.creation
```

### Event Handling
The service processes the following events:
- User creation events from Booking Service
  - Creates new users automatically
  - Updates existing user information if needed
  - Manages user roles and permissions

## API Documentation

### User Management Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------------|
| GET | `/users` | Get list of all users | ADMIN |
| GET | `/users/{id}` | Get user by ID | USER/ADMIN |
| GET | `/users/email/{email}` | Get user by email address | USER/ADMIN |
| POST | `/users` | Create a new user | USER/ADMIN |
| PUT | `/users/update` | Update user information | USER/ADMIN |
| DELETE | `/users/{id}` | Delete user by ID | ADMIN |
| GET/HEAD | `/users/health` | Service health and uptime | Public |

### Admin Management Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/admin/create` | Create a new admin | ADMIN |
| POST | `/admin/login` | Admin login (returns JWT) | ADMIN |
| GET | `/admin` | Get all admins | ADMIN |
| GET | `/admin/email/{email}` | Get admin by email | ADMIN |
| DELETE | `/admin/delete/{email}` | Delete admin by email | ADMIN |


### User Payload Examples

#### Create User Request
```json
{
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": 1234567890
}
```

#### Update User Request
```json
{
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Smith",
  "phoneNumber": 9876543210
}
```

## Error Responses

The API returns appropriate HTTP status codes:

- `200 OK`: Request succeeded
- `400 Bad Request`: Invalid input
- `401 Unauthorized`: Authentication failed
- `403 Forbidden`: Authorization failed
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource already exists

Error responses follow this format:
```json
{
  "message": "Description of the error"
}
```

## Security Features

- Password hashing with salt
- JWT token authentication
- API key verification
- Role-based authorization

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
docker build -t user-service -f DockerFile.dev .
```

## Event Processing
The service processes events in the following way:
1. Receives user creation event from Booking Service
2. Checks if user exists
3. Creates new user if not found
4. Updates booking count for existing users
5. Maintains user profile information

## Troubleshooting
1. RabbitMQ Connection Issues:
   - Verify RabbitMQ service is running
   - Check connection credentials in .env file
   - Ensure RabbitMQ ports are accessible

2. Database Issues:
   - Verify PostgreSQL is running
   - Check database credentials
   - Ensure database schema exists

3. Common Errors:
   - User already exists
   - Invalid email format
   - Missing required fields

## Monitoring and Maintenance

- **Service health endpoint:** `/api/users/health`
  - Supports both `GET` and `HEAD` requests.
  - `GET` returns a JSON body with service status, uptime, and memory info.
  - `HEAD` returns only headers (no body), as per HTTP specification.
  - This endpoint is public and can be used with monitoring tools like UptimeRobot to check service availability and prevent server spin-down.
- RabbitMQ queues status: Access RabbitMQ management UI (port 15672)
- Database monitoring through PostgreSQL tools

## Related Services
- [Booking Service](../booking-service/README.md) - Handles booking creation and management
