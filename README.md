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
- `scripts` — optional scripts for seeding contacts and notifications

## Seed sample data

With the API running, install the script dependency and create sample records:

```bash
python3 -m pip install -r scripts/requirements.txt
./scripts/seeder.sh 20
./scripts/notification-seeder.sh 20
```
