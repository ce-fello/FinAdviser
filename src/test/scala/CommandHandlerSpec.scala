import org.scalatest.funsuite.AnyFunSuite
import bot.CommandHandler
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

class CommandHandlerSpec extends AnyFunSuite:
  test("/help returns help text") {
    val handler = new CommandHandler(null)(using global)
    val result = Await.result(handler.handleCommand("/help"), 1.second)
    assert(result.contains("Доступные команды"))
  }
