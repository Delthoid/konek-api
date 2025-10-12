# Konek-API Project Progress Summary

**Last Updated:** October 11, 2025

**Current State:**
*   Spring Boot Version: `3.5.6` (user's choice, causing persistent test context issues)
*   Build Status: Compiles successfully, but `UserControllerTest` fails due to Spring Boot version issues. `KonekApiApplicationTests` is commented out.
*   User Preference: Strict "suggest changes first" policy. **I will strictly adhere to this.**

---

## Implemented/Approved Changes (by User or Agent):

### 1. Project Setup & Dependencies:
*   `src/test/resources/application.properties`: Configured for H2 in-memory database for tests.
*   `build.gradle`:
    *   H2 database dependency (`com.h2database:h2`) added.
    *   Lombok `annotationProcessor` added.
    *   JWT dependencies (`io.jsonwebtoken:jjwt-api`, `jjwt-impl`, `jjwt-jackson`) added (User confirmed they added this).
*   `settings.gradle`: Gradle `foojay-resolver-convention` plugin added for JDK auto-download.
*   `KonekApiApplicationTests.java`: `contextLoads()` test temporarily commented out.

### 2. `signUp` (Registration) Feature:
*   **`User.java` (Entity):**
    *   `@Data` annotation for Lombok getters/setters.
    *   `id`, `userName`, `email`, `password`, `avatarUrl`, `createdAt`, `deletedAt` fields.
*   **`UserRepository.java` (Interface):** (User confirmed they updated this)
    *   Extends `JpaRepository<User, UUID>`.
    *   `Optional<User> findByUserName(String userName);`
    *   `Optional<User> findByEmail(String email);`
*   **`UserSignUpRequest.java` (DTO):** (User confirmed they created this)
    *   Fields: `userName`, `email`, `password`.
    *   Validation: `@NotBlank`, `@Size` on all fields.
*   **`UserResponseDto.java` (DTO):** (Suggested, not yet created)
    *   Fields: `id`, `userName`, `email`, `avatarUrl`, `createdAt` (password excluded).
*   **`UserService.java` (Interface):** (Approved, pending application)
    *   `User signUp(UserSignUpRequest request);`
    *   `LoginResponse signIn(LoginRequest request);`
*   **`UserServiceImpl.java` (Implementation):** (Created, pending update for `signUp` and `signIn` logic)
    *   Currently has placeholder `signUp` method.
    *   Needs to be updated to:
        *   Inject `UserRepository` and `PasswordEncoder`.
        *   Implement `signUp` to convert DTO to entity, hash password, save user, check for existing users (will throw `UserAlreadyExistsException`).
*   **`UserController.java` (Controller):** (Approved, pending application)
    *   `@PostMapping("/signup")` method updated to accept `@Valid @RequestBody UserSignUpRequest`.
    *   Calls `userService.signUp()`.
*   **`SecurityConfig.java` (Configuration):** (Created, pending updates)
    *   `@Bean public PasswordEncoder passwordEncoder()` (BCrypt).
    *   Needs to be updated with `SecurityFilterChain`, `AuthenticationManager`, `AuthenticationProvider` for JWT.

### 3. Authentication (Login) Feature:
*   **`LoginRequest.java` (DTO):** (User confirmed they created this)
    *   Fields: `username`, `password`.
*   **`LoginResponse.java` (DTO):** (Suggested, not yet created)
    *   Field: `jwt`.
*   **`JwtUtil.java` (Utility):** (Suggested, not yet created)
    *   Handles JWT generation, validation, extraction.
*   **`application.properties`:**
    *   `jwt.secret` and `jwt.expiration` added (User confirmed they added this).
*   **`CustomUserDetailsService.java` (Service):** (Suggested, not yet created)
    *   Implements `UserDetailsService` to load user details from `UserRepository`.

### 4. Exception Handling:
*   **`ErrorResponse.java` (DTO):** (Suggested, not yet created)
    *   Fields: `timestamp`, `status`, `error`, `message`, `path`, `fieldErrors`.
*   **`GlobalExceptionHandler.java` (Advice):** (Suggested, not yet created)
    *   `@RestControllerAdvice` with `@ExceptionHandler` for `MethodArgumentNotValidException` and `Exception.class`.
*   **`UserAlreadyExistsException.java` (Custom Exception):** (Suggested, not yet created)
    *   `@ResponseStatus(HttpStatus.CONFLICT)`.

---

## Pending Actions (Next Steps):

1.  **Apply `UserService` interface changes.**
2.  **Apply `SecurityConfig` changes.**
3.  **Create `LoginResponse` DTO.**
4.  **Create `JwtUtil` class.**
5.  **Create `CustomUserDetailsService` class.**
6.  **Implement `UserServiceImpl` `signUp` and `signIn` methods.**
7.  **Implement `UserController` `signIn` method.
8.  **Implement JWT filters (Authentication and Authorization).**
9.  **Implement Global Exception Handler (DTO and Advice).**

---

## October 12, 2025 Update

**Summary:** Completed the implementation of the authentication flow (`signUp` and `signIn`). The application should now be ready for testing.

**Key Changes:**

*   **`UserServiceImpl.java`:**
    *   The `signUp` method is implemented to handle user registration, password hashing, and saving the user.
    *   The `login` method is implemented to use `AuthenticationManager` for credential validation and `JwtUtil` to generate a token upon successful authentication.
*   **`JwtAuthFilter.java`:**
    *   A new filter was created to intercept incoming requests.
    *   It extracts the JWT from the `Authorization` header, validates it, and sets the `SecurityContextHolder` to authenticate the user for the duration of the request.
*   **`SecurityConfig.java`:**
    *   The `AuthenticationManager` bean was exposed.
    *   The `JwtAuthFilter` was added to the security filter chain before the `UsernamePasswordAuthenticationFilter`.
    *   The `/api/auth/signup` and `/api/auth/signin` endpoints were added to the permit list.
*   **`AuthController.java`:**
    *   Verified that the controller correctly maps the `/signup` and `/login` endpoints to the corresponding `UserService` methods.
    *   Suggested minor improvements for consistency (`/api/auth` mapping) and best practices (injecting `UserService` interface).
*   **`application.properties`:**
    *   The file is configured to use environment variables.
    *   **Suggested a temporary `application-dev.properties`** for easy local testing with an H2 in-memory database and a hardcoded JWT secret. The user opted to use their actual database instead.

**Next Steps:**

1.  The user will configure their `application.properties` with their database credentials.
2.  Run the application and test the `signUp` and `signIn` endpoints.
