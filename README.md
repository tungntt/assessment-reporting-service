# Assessment Reporting Service

## Architecture

This application follows a **layered architecture**:

1. **Controller Layer** - Handles HTTP requests and responses
2. **Service Layer** - Contains business logic
3. **Mapper Layer** - Logic Mapping/Transform data from Entity/Model to DTO
4. **Repository Layer** - Data access using Spring Data JPA
5. **Entity Layer** - JPA entities mapping to database tables
6. **Config Layer** - Contain Configurations (Security, Web)

## Security Implementation

- Spring Security with form-based authentication
- In-memory user store with BCrypt password encoding
- Role-based access control (EMPLOYEE, ADMIN)
- Authentication events logged

## Database

- PostgreSQL 16 running in Docker
- Flyway for migrations



## Tech Stack

- Java 17
- Spring Boot 4.0.0
- Spring Data JPA
- Spring Security
- Database: PostgreSQL
- Database Migration: Flyway
- MapStruct 1.6.3
- Lombok
- Gradle
- Springboot Test (Component Test)
- Docker
- JSP with JSTL
- Bootstrap 5


## Guides

### 1. Start PostgreSQL Database

```bash
./gradlew dockerUp
```

Or manually:

```bash
docker-compose up -d
```

### 2. Run the Application

```bash
./gradlew bootRun
```

The application will start at http://localhost:8080

### 3. Access the Application

Open your browser and navigate to http://localhost:8080

**Test Credentials:**

| Username | Password  | Role     | Access                     |
|----------|-----------|----------|----------------------------|
| tom      | password  | EMPLOYEE | Can view own records only  |
| jerry    | password  | EMPLOYEE | Can view own records only  |
| admin    | admin123  | ADMIN    | Can view all records       |

### 4. Stop the Database

```bash
./gradlew dockerDown
```

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.0/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.0/gradle-plugin/packaging-oci-image.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.0.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/4.0.0/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [Spring Security](https://docs.spring.io/spring-boot/4.0.0/reference/web/spring-security.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.0/reference/web/servlet.html)

