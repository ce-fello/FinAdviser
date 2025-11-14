package bot

import service.CurrencyService
import scala.concurrent.{ExecutionContext, Future}

class CommandHandler(currency: CurrencyService)(using ec: ExecutionContext):
  def handleCommand(text: String): Future[String] = text.trim.toLowerCase match
    case "/start" | "/help" =>
      Future.successful("Доступные команды:\n/usd — курс доллара к рублю\n/eur — курс евро к рублю")
    case "/usd" => currency.getRate("USD").map(r => f"Курс USD → RUB: ${r.rate}%.2f")
    case "/eur" => currency.getRate("EUR").map(r => f"Курс EUR → RUB: ${r.rate}%.2f")
    case other => Future.successful(s"Неизвестная команда: $other. Введите /help")
