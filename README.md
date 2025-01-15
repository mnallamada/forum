# forum Spring Boot Application

A simple Spring Boot Kotlin application.

## Testing

### Run locally

Package the jar file

```bash
mvn clean install
```

Execute the app

```bash
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

Open http://localhost:8081/ to connect to the application or http://localhost:8081/swagger-ui.html for API docs

Run tests including the integration tests.

```bash
mvn clean verify
```

Run unit and component tests.

```bash
mvn clean verify -DskipITs
```
