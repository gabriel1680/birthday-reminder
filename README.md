# Birthday Reminder

A simple Java application for managing contacts, birthdays, and notification destinations. The project provides a REST API, a web interface, and a command-line client.

Data is currently stored in memory and is reset whenever the API restarts.

## Requirements

- Java 21
- No local Gradle installation is required; the Gradle Wrapper is included.

## Run locally

Start the API on port `8080`:

```bash
./gradlew :api:run
```

In another terminal, start the web application on port `9090`:

```bash
./gradlew :web:run
```

Open [http://localhost:9090](http://localhost:9090) in your browser.

To check that the API is running:

```bash
curl http://localhost:8080/status
```

## CLI

The CLI also connects to the API at `http://localhost:8080`.

```bash
./gradlew :cli:run --args="--help"
./gradlew :cli:run --args="create --name 'Ada Lovelace' --birthdate '1815-12-10T00:00:00Z'"
./gradlew :cli:run --args="search"
```

## Tests

Run the unit tests for all modules:

```bash
./gradlew test
```

Run the API integration tests:

```bash
./gradlew :api:integrationTest
```

## Project structure

- `core` — business rules and domain logic
- `api` — REST API on port `8080`
- `web` — browser interface on port `9090`
- `cli` — command-line client
- `shared` — code shared by the clients
- `api/src/seeder` — JVM-based sample data seeder

## Seed sample data

With the API running, create sample records using the JVM-based Gradle tasks:

```bash
./gradlew :api:seed
```

The task creates 20 contacts and 20 notifications by default. Use `-Pcount=<number>` to change the amount created for each resource:

```bash
./gradlew :api:seed -Pcount=50
```

Set `API_BASE_URL` to seed an API running at a different address.
