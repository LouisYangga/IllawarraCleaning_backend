# User Service API Documentation

The User Service provides authentication, authorization, and user management capabilities for the Illawarra Cleaning Project microservices architecture.

## Base URL

```
/api
```

## Authentication & Authorization

All API endpoints (except specifically excluded ones) require:
- API Key authentication via interceptor for all `/api/**` paths
- JWT authentication for protected routes

## Admin APIs

### Admin Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|--------------|
| POST | `/admin/create` | Create a new admin user | Yes |
| POST | `/admin/login` | Authenticate admin and get JWT token | No |
| GET | `/admin` | Get list of all admins | Yes |
| GET | `/admin/email/{email}` | Get admin by email address | Yes |
| DELETE | `/admin/delete/{email}` | Delete admin by email (requires authorized admin credentials) | Yes |

### Admin Payload Examples

#### Create Admin Request
```json
{
  "email": "admin@example.com",
  "password": "secure_password"
}
```

#### Login Response
```json
{
  "message": "Login successful",
  "token": "jwt_token_here",
  "user": {
    "id": 1,
    "email": "admin@example.com"
  }
}
```

## User APIs

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
