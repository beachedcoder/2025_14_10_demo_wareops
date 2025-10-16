# Copilot Instructions for Java Spring Microservices

## Purpose
Provide standard developer and automated-agent guidance for building, testing, packaging, and operating Java Spring microservices.

## Project Metadata
- **Service name**: warehouse-management-service
- **Description**: Service for managing warehouse operations, employees and reciept of inventory deliveries.
- **Group Id**: com.training.callum
- **Artifact Id**: whoms
- **Spring Boot version**: 3.5.6
- **Java version**: 25
- **Build tool**: Gradle 9.1.0 or higher
- **Packaging type**: executable jar
- **Repository type**: single-module

## Code Style and Formatting
- **Formatter**: google-java-format v1.15.0 applied via gradle plugin with convenience tasks:
  - `./gradlew googleJavaFormat` to format code.
  - `./gradlew googleJavaFormatCheck` to check formatting in CI.
- **Style guide**: Follow Google Java Style with these overrides:
  - 4-space indent.
  - Prefer final for immutable fields.
  - Use standard Java naming: PascalCase for types, camelCase for variables and methods, UPPER_SNAKE for constants.
  - use Java idiomatics: try-with-resources, Optional, Streams, etc.
  - Avoid wildcard imports.
- **Logging**: SLF4J API simple configuration. Avoid System.out/err. use fluent logging for multi-arg logs.
- **Comments and documentation**
  - Javadoc for public APIs and complex logic.
  - Use TODO comments with GitHub issue references for technical debt.

## Tests and Test Requirements
- **General test requirements**
    - Location: src/test/java mirroring main package structure.
    - Profiles: use `test` Spring profile for tests. use application-test.properties for test-specific config. 
    - Isolation: tests must be isolated and independent. use mocks for external dependencies.
    - Coverage: Minimum baseline 80% line coverage
    - Fast, deterministic, single-threaded by default.
    - Naming: use descriptive naming conventions. GivenWhenThen or shouldDoX_whenY convention.
    - Data: use small in-memory databases (H2) or mocks for unit tests. use small, deterministic test fixtures for integration tests. avoid large datasets.
    - CI: all tests must pass in CI before merge.
- **Unit tests**
  - Framework: JUnit Jupiter
  - Mocking: Mockito or Mockito-inline.
  - Structure: Arrange-Act-Assert pattern.
- **Integration tests**
  - Use Spring Boot test slices and @SpringBootTest for full-context tests.
  - Use weblayer test for controller tests.
  - Integration tests must run in CI and pass before merge.

## Build, Quality Gates and CI
- **Gradle goals**: `./gradlew clean build` to build, test, and package.
- **Gradle wrapper**: Commit gradle wrapper files to repo for consistent builds.
- **Quality checks in CI**
  - Spotless check or formatter check.
  - Static analysis: SpotBugs, ErrorProne or SonarCloud scanner.
  - Dependency checks: OWASP dependency-check or Snyk.
  - Security scanning for known CVEs.
  - Unit + integration tests, jacoco coverage report.
  - Fail fast on test failures and high-severity Scan findings.
- **Artifact packaging**
  - Produce an executable jar (Spring Boot) with layered jars enabled.
  - Publish to internal artifact registry with semantic versioning tags.
- **Release process**
  - Semantic versioning for service releases: MAJOR.MINOR.PATCH.
  - CI builds on tags create release artifacts and Docker images.

## Libraries and Dependency Standards
- **Core frameworks**
  - Spring Boot starter parents and starters for Web, WebFlux, Data JPA, Security, Actuator as needed.
  - Spring Cloud components only when explicitly required
- **BOM and dependency management**
  - Use Spring Boot BOM for dependency versions.
  - Use dependency constraints in Gradle for shared libraries.
  - No direct version numbers in service build files except for plugins.
- **JSON and serialization**
  - Jackson is the canonical JSON library. Configure strict deserialization for unknown fields off by default.
- **Reactive vs Blocking**
  - Use reactive stack (WebFlux, Reactor) only when requirements justify it. Otherwise prefer blocking MVC controllers.
- **Database access**
  - Use Spring Data JPA for ORMs.
- **Security**
  - Spring Security for authentication and authorization.
  - Token-based auth (OAuth2 / JWT) preferred for services exposing APIs.
- **Forbidden libraries**
  - Avoid deprecated libraries and copies of transitive dependencies. Avoid reflection-heavy libs unless necessary.

## Directory and Module Standards
- **Gradle project structure**
  - Root project with settings.gradle and build.gradle
  - Standard src layout:
    - src/main/java
    - src/main/resources
    - src/test/java
    - src/test/resources
- **Package structure**
  - com.training.service.domain
  - com.training.service.web
  - com.training.service.config
  - com.training.service.service
  - com.training.service.repository
  - Keep packages by feature, not layer.
- **Module guidance**
  - Single runtime module named `service` or root artifact for simple services.
  - For multi-module projects:
    - `service-api` for DTOs and REST contracts.
    - `service-core` for business logic.
    - `service-adapter-<system>` for external system integrations.
    - `service-app` or `service-runtime` for Spring Boot application.
  - Keep module boundaries small and explicit; enforce with build and review.
- **Resources management**
  - Keep application.properties in src/main/resources.
  - Secrets must never be checked into the repo.

## Application Specific Operational Instructions
- **Configuration**
  - Use Spring profiles: default and test
- **Health and readiness**
  - Expose Actuator endpoints for /health and /ready; configure readiness probe based on readiness endpoint.
- **Metrics and tracing**
  - Expose Prometheus metrics at /actuator/prometheus.
  - Emit structured logs with JSON output in production format.
- **API Documentation**
  - Maintain OpenAPI annotations and generated OpenAPI YAML in build artifacts.
- **Deployment**
  - Dockerfile should use non-root user and minimal base image; follow org Dockerfile best practices.
  - Layered jar to speed image builds and caching.
- **Backward compatibility**
  - Maintain backward compatible REST changes; use API versioning when needed.

## Code Review and PR Guidance
- **PR checklist**
  - CI green across all checks.
  - Unit and integration tests
  - No secrets in diff.
  - Change log entry if behavior changes.
  - Update OpenAPI contract if endpoint changes.
- **Review focus**
  - Correctness, test coverage, failure modes, logging, observability, and security review.

## Automation and Agents
- **Machine actionable cues**
  - Use conventional commit messages for release tooling.
  - Provide structured metadata in repository root: service-metadata.yml containing health endpoints, port, and dependencies for automation.
- **Copilot or automated bot instructions**
  - Suggest tests and sample test fixtures when creating new endpoints.
