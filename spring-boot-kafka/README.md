Spring Boot Kafka
=================

Overview
--------
This repository is a small Spring Boot microservices application that demonstrates producing and consuming events via Apache Kafka. It contains two services and a shared `common` module:

- `location-update-service` — a producer service that emits location update events to Kafka.
- `live-tracking-service` — a consumer service that consumes location update events and exposes any tracking-related APIs.
- `common/` — shared DTOs and utilities used by both services.

A docker-compose.yml is included for bringing up Kafka locally for development.

Quick start (Docker Compose)
---------------------------
1. Start Kafka:
   docker-compose up -d

2. Build services (from repository root):
   mvn clean package -DskipTests

3. Run services (either via Docker images you build, or locally):
   mvn -pl location-update-service -am spring-boot:run
   mvn -pl live-tracking-service -am spring-boot:run

Running locally (Maven)
-----------------------
- To run a single service with Maven (from repo root):
  mvn -pl <service-dir> -am spring-boot:run
  Example: mvn -pl live-tracking-service -am spring-boot:run

Configuration
-------------
Both services use Spring Boot properties. Common properties to set (application.yml / environment variables):

- spring.kafka.bootstrap-servers — Kafka broker address (default: localhost:9092 when using docker-compose)
- spring.application.name — service name
- server.port — HTTP port for the service

Topics
------
- location-updates — used for publishing location update events (producer: location-update-service, consumer: live-tracking-service)

Development notes
-----------------
- The `common` module contains shared DTOs and must be on the classpath for both services.
- Use the provided docker-compose.yml during development to run Kafka locally; it simplifies bootstrap configuration.
- Tests can be executed with `mvn test` per module or across the repo.

Troubleshooting
---------------
- If a service cannot connect to Kafka, verify docker-compose is running and that `spring.kafka.bootstrap-servers` is set to the broker address.
- Inspect logs: `docker-compose logs -f` or check the Spring Boot logs for stack traces.

Extending
---------
- Add more event types and consumers for richer examples (e.g., geofencing, aggregation).
- Containerize the services and add Kubernetes manifests for deployment testing.