# ComplexLab

Multi-module Maven project for deploying a Spring Boot web application with PostgreSQL, Flyway, Docker, and Render. Done by Max Sheludchenko

## Modules

- `core` — domain models and service contracts
- `persistence` — Spring Data JPA repositories, Flyway migrations, service implementations
- `web` — Spring MVC (Thymeleaf), Spring Security, REST API, Actuator, Dockerfile

## Local run

1. Start PostgreSQL (example)
   - Database: `complexlab`
   - User: `complexlab_user`
   - Password: `complexlab_pass`

2. Build and run
```bash
mvn clean package -pl web -am
java -jar web/target/web-*.jar
```

3. Open
- http://localhost:8080
- Health: http://localhost:8080/actuator/health

Default admin user is created on startup (if missing):
- username: `admin`
- password: `admin123`

You can override via env:
- `APP_ADMIN_USERNAME`
- `APP_ADMIN_PASSWORD`

## Environment variables

Web module reads configuration from environment variables:

- `SPRING_DATASOURCE_URL` (default `jdbc:postgresql://localhost:5432/complexlab`)
- `SPRING_DATASOURCE_USERNAME` (default `complexlab_user`)
- `SPRING_DATASOURCE_PASSWORD` (default `complexlab_pass`)
- `PORT` (default `8080`)

## Docker (web module)

From repository root:

```bash
mvn clean package -pl web -am
docker build -t complexlab-web -f web/Dockerfile .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/complexlab \
  -e SPRING_DATASOURCE_USERNAME=complexlab_user \
  -e SPRING_DATASOURCE_PASSWORD=complexlab_pass \
  complexlab-web
```

## Render deployment (summary)

1. Create Render PostgreSQL service and copy **Internal Database URL**, username, password.
2. Create Render Web Service:
   - Connect GitHub repo (branch for deploy)
   - Environment: **Docker**
   - Dockerfile path: `web/Dockerfile`
3. Set Environment variables in Render:
   - `SPRING_DATASOURCE_URL` = Internal DB URL
   - `SPRING_DATASOURCE_USERNAME` = DB username
   - `SPRING_DATASOURCE_PASSWORD` = DB password
   - Optional: `APP_ADMIN_PASSWORD` to change default admin password
4. Verify logs:
   - Flyway migrations applied
   - No Hibernate connection errors
5. Open public URL:
   - register user, login
   - admin can create/edit/delete books
   - authenticated users can add comments
