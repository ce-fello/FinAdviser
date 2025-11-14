package util

import scala.io.Source

case class Config(
                   telegramToken: String,
                   cacheTTL: Int = 60,
                   exchangeApiKey: String
                 )

object Config:
  def load(): Config =
    val env = Source.fromFile(".env").getLines()
      .filter(_.contains("="))
      .map { line =>
        val Array(key, value) = line.split("=", 2)
        key.trim -> value.trim
      }.toMap

    val token = env.getOrElse("TELEGRAM_BOT_TOKEN", {
      println("Error: TELEGRAM_BOT_TOKEN not set!")
      sys.exit(1)
    })

    val ttl = env.get("CACHE_TTL_SECONDS").map(_.toInt).getOrElse(60)

    val exchangeKey = env.getOrElse("EXCHANGE_API_KEY", {
      println("Error: EXCHANGE_API_KEY not set!")
      sys.exit(1)
    })

    Config(token, ttl, exchangeKey)
