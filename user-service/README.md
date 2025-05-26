# User Service

## Overview
The User Service is a microservice component of the Illawarra Cleaning Project that handles user management and works in conjunction with the Booking Service. It manages user profiles, tracks booking counts, and processes user-related events received through RabbitMQ.

## Features
- User profile management
- Track user booking counts
- Process user creation events from Booking Service
- Automatic user creation on first booking
- User profile updates
- Booking count management

## Technologies
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- RabbitMQ (Message Consumer)
- Docker
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
The service can be configured through:
- `src/main/resources/application.properties` - Main application configuration
- `.env` - Environment variables
- `docker-compose.dev.yml` - Docker development configuration

## Message Queue Integration
The service listens for the following events from RabbitMQ:
- User creation events from Booking Service
- Booking count increment events

## API Documentation

### User Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|--------------|
| GET | `/users` | Get list of all users | Yes |
| GET | `/users/{id}` | Get user by ID | Yes |
| GET | `/users/email/{email}` | Get user by email address | No |
| POST | `/users` | Create a new user | Yes |
| PUT | `/users/update` | Update user information | No |
| DELETE | `/users/{id}` | Delete user by ID | Yes |

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
- Service health endpoint: `/actuator/health`
- RabbitMQ queues status: Access RabbitMQ management UI (port 15672)
- Database monitoring through PostgreSQL tools

## Related Services
- [Booking Service](../booking-service/README.md) - Handles booking creation and management
