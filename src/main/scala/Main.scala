import util.{Config, HttpClient}
import service.{CurrencyService, CacheService}
import bot.{TelegramBotWebhook, CommandHandler}
import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.stream.Materializer

@main def run(): Unit =
  implicit val system: ActorSystem = ActorSystem("CurrencyRateBotSystem")
  implicit val mat: Materializer = Materializer(system)
  implicit val ec: ExecutionContext = system.dispatcher

  val cfg = Config.load()
  val http = new HttpClient()
  val cache = new CacheService(cfg.cacheTTL)
  val currency = new CurrencyService(http, cache)
  val handler = new CommandHandler(currency)
  
  val bot = new TelegramBotWebhook(cfg.telegramToken, http, handler)

  println("CurrencyRateBot запущен!")
  bot.start()

  // Ждём завершения приложения
  Thread.currentThread().join()
