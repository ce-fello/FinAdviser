import util.{Config, HttpClient}
import service.{CurrencyService, CacheService}
import bot.CommandHandler
import bot.TelegramBotPolling

import scala.concurrent.ExecutionContext

@main def run(): Unit =
  given ec: ExecutionContext = ExecutionContext.global

  val cfg = Config.load()
  val http = new HttpClient()
  val cache = new CacheService(cfg.cacheTTL)
  val currency = new CurrencyService(http, cache, cfg.exchangeApiKey)
  val handler = new CommandHandler(currency)

  val bot = new TelegramBotPolling(cfg.telegramToken, http, handler)
  bot.start()

  Thread.currentThread().join()
