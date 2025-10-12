# Konek-API Project Summary

**Last Updated:** October 12, 2025

**User Preference:** Strict "suggest changes first" policy.

---

## Completed Features

### 1. User Authentication (`/api/auth`)
*   **Status:** Done.
*   **Endpoints:**
    *   `POST /api/auth/signup`: For user registration.
    *   `POST /api/auth/signin`: For user login.
*   **Functionality:**
    *   Handles user registration with password hashing (BCrypt).
    *   Authenticates users based on username/email and password.
    *   Generates a JWT token upon successful login.
    *   Includes a `JwtAuthFilter` to validate tokens on incoming requests and manage the security context.
*   **Core Components:**
    *   `AuthController`
    *   `UserService` / `UserServiceImpl`
    *   `CustomUserDetailsService`
    *   `UserRepository`
    *   `JwtUtil`
    *   `SecurityConfig` (with `AuthenticationManager` and `JwtAuthFilter`)
    *   `SignUpRequest`, `SignInRequest`, `UserDtoResponse`, `SignInResponse` DTOs.

---

## Configuration

*   **`build.gradle`:** Configured with Spring Boot, Spring Security, JPA, JWT, and Lombok dependencies.
*   **`application.properties`:** Set up to use environment variables for database credentials and JWT settings.
*   **Local Development:** User has opted to use their own database for development. A suggestion to use a temporary `application-dev.properties` with an H2 in-memory database was noted but not adopted.

---

## Next Steps

*   **Integration Testing:** The user plans to write integration tests for the authentication flow to simulate the full user journey (e.g., sign up -> login -> access a protected resource).