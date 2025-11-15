# FinAdviser -- Telegram Currency Rate Bot

FinAdviser is a simple educational pet project written in **Scala**,
implementing a Telegram bot that provides real‑time currency exchange
rates (USD → RUB, EUR → RUB).\
The bot communicates with the Telegram API via long polling and
retrieves exchange rates from an external currency API with built‑in
caching.

------------------------------------------------------------------------

## Features

-   Fetches **USD→RUB** and **EUR→RUB** exchange rates
-   Simple command handler for processing Telegram messages
-   Long‑polling implementation without external frameworks
-   In‑memory caching layer to decrease API load
-   Config loader using `.env` file
-   Minimal and clean service‑based architecture
-   Basic unit tests for core components

------------------------------------------------------------------------

## Project Structure

    src/
     ├─ main/scala/
     │   ├─ bot/               # Telegram bot logic (polling, commands)
     │   ├─ service/           # Currency service + caching layer
     │   ├─ util/              # HTTP client and config loader
     │   ├─ model/             # Domain models
     │   └─ Main.scala         # Entry point
     └─ test/scala/            # Unit tests

------------------------------------------------------------------------

## Run the Bot

### 1. Create a `.env` file:

    TELEGRAM_BOT_TOKEN=your_bot_token_here
    EXCHANGE_API_KEY=your_exchange_api_key
    CACHE_TTL_SECONDS=60

### 2. Run:

``` bash
sbt run
```

------------------------------------------------------------------------

## Available Commands

Command    Description
  ---------- -----------------------------
`/start`   Shows help message

`/help`    List available commands

`/usd`     Get USD → RUB exchange rate

`/eur`     Get EUR → RUB exchange rate

------------------------------------------------------------------------

## Technologies Used

-   **Scala 3**
-   **STTP HTTP client**
-   **Circe** for JSON parsing
-   **Telegram Bot API**
-   **sbt** build system
-   **ScalaTest** for unit tests

------------------------------------------------------------------------

## Tests

The project includes basic tests for:

-   `CacheService`
-   `CommandHandler`
-   `CurrencyService` (requires network access)

Run tests:

``` bash
sbt test
```

------------------------------------------------------------------------

## Future Improvements

-   Dockerization
-   Support for more currencies
-   Kubernetes deployment
-   Inline keyboard support
-   Asynchronous streaming (Akka / ZIO)
-   More detailed error handling

