# Assessment Reporting Service

## Architecture

This application follows a **layered architecture**:

1. **Controller Layer** - Handles HTTP requests and responses
2. **Exception Handler Layer** - Global exception handling and error responses
3. **Service Layer** - Contains business logic
4. **Mapper Layer** - Logic Mapping/Transform data from Entity/Model to DTO
5. **Repository Layer** - Data access using Spring Data JPA
6. **Entity Layer** - JPA entities mapping to database tables
7. **Config Layer** - Contain Configurations (Security, Web)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/xphr/reporting/ms/
│   │       ├── Application.java                    # Main application entry point
│   │       ├── config/                             # Configuration layer
│   │       │   ├── SecurityConfig.java            # Spring Security configuration
│   │       │   └── WebConfig.java                 # Web MVC configuration
│   │       ├── controller/                         # Controller layer
│   │       │   ├── dto/                           # Data Transfer Objects
│   │       │   │   ├── PagingListResponseDto.java
│   │       │   │   └── TimeRecordReportDto.java
│   │       │   ├── interceptor/                   # Request interceptors
│   │       │   │   └── TrackingRequestInterceptor.java
│   │       │   ├── LoginController.java           # Login/authentication controller
│   │       │   └── TimeRecordController.java      # Time record report controller
│   │       ├── exception/                          # Exception handling layer
│   │       │   └── GlobalExceptionHandler.java    # Global exception handler
│   │       ├── mapper/                             # Mapper layer
│   │       │   └── ReportMapper.java              # MapStruct mapper (Entity/Model → DTO)
│   │       ├── repository/                         # Repository layer
│   │       │   ├── entity/                        # JPA entities
│   │       │   │   ├── EmployeeEntity.java
│   │       │   │   ├── ProjectEntity.java
│   │       │   │   └── TimeRecordEntity.java
│   │       │   ├── model/                         # Repository models
│   │       │   │   └── TimeRecordReportModel.java
│   │       │   └── TimeRecordRepository.java      # Spring Data JPA repository
│   │       ├── service/                            # Service layer
│   │       │   └── TimeRecordReadService.java     # Business logic for time records
│   │       └── util/                               # Utility classes
│   │           └── DateTimeUtils.java             # Date/time utilities
│   ├── resources/
│   │   ├── application.yml                        # Main application configuration
│   │   ├── application-dev.yml                    # Development profile configuration
│   │   ├── application-prod.yml                   # Production profile configuration
│   │   ├── db/
│   │   │   └── migration/                         # Flyway database migrations
│   │   │       ├── V1__create_table.sql
│   │   │       ├── V2__insert_sample_data.sql
│   │   │       └── V3__insert_large_sample_data.sql
│   │   ├── evidence/                              # Performance evidence images
│   │   │   ├── before_optimize.png
│   │   │   └── after_optimize.png
│   │   ├── static/                                # Static resources (CSS, JS, images)
│   │   └── templates/                             # Template files
│   └── webapp/
│       └── WEB-INF/
│           └── jsp/                               # JSP view templates
│               ├── error.jsp
│               ├── login.jsp
│               └── work_hours_report.jsp
└── test/
    ├── java/
    │   ├── com/xphr/reporting/ms/
    │   │   └── ApplicationTests.java              # Application context tests
    │   └── component/com/xphr/reporting/ms/       # Component/integration tests
    │       ├── ComponentTest.java                 # Component test annotation
    │       ├── controller/
    │       │   └── TimeRecordControllerTest.java
    │       ├── exception/
    │       │   └── GlobalExceptionHandlerTest.java
    │       ├── mapper/
    │       │   └── ReportMapperTest.java
    │       ├── repository/
    │       │   └── TimeRecordRepositoryTest.java
    │       └── service/
    │           └── TimeRecordReadServiceTest.java
    └── resources/
        └── application-test.yml                   # Test profile configuration
```

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

## Run Component Test

### 1. Make sure No Docker Container Runs
```bash
./gradlew dockerDown
```

### 2. Run Component Test
```bash
./gradlew componentTest
```

### Next Improvement
- Replace the In-memory Security by An Authentication Server (such as: Keycloak)
- Integrate with Hashicorp Vault to Store/Secure Database Credential
- Implement Cucumber Test as Blackbox Test


### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.0/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.0/gradle-plugin/packaging-oci-image.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.0.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/4.0.0/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [Spring Security](https://docs.spring.io/spring-boot/4.0.0/reference/web/spring-security.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.0/reference/web/servlet.html)

