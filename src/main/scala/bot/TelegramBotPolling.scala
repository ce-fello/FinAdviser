package bot

import io.circe.parser.*
import io.circe.generic.auto.*
import util.HttpClient

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class TelegramBotPolling(
                          token: String,
                          http: HttpClient,
                          handler: CommandHandler
                        )(using ec: ExecutionContext) {

  private val api = s"https://api.telegram.org/bot$token"

  def start(): Unit =
    println("CurrencyRateBot started! Polling for messages...")
    pollLoop(0L)

  private def pollLoop(offset: Long): Unit = {
    val url = s"$api/getUpdates?timeout=25&offset=$offset"
    http.get(url).onComplete {
      case Success(body) =>
        val updates: List[Update] =
          parse(body)
            .flatMap(_.as[UpdatesResult])
            .map(_.result)
            .getOrElse(Nil)

        if updates.nonEmpty then
          println(s"Received ${updates.size} updates")

        var nextOffset = offset

        updates.foreach { upd =>
          nextOffset = math.max(nextOffset, upd.update_id + 1)

          upd.message.foreach { msg =>
            msg.text.foreach { text =>
              println(s"Received message: $text")
              handler.handleCommand(text).onComplete {
                case Success(reply) =>
                  println(s"Sending reply: $reply")
                  http.postForm(
                    s"$api/sendMessage",
                    Map(
                      "chat_id" -> msg.chat.id.toString,
                      "text"    -> reply
                    )
                  ).recover { case e =>
                    println(s"Failed to send message: $e")
                  }
                case Failure(ex) =>
                  println(s"CommandHandler error: $ex")
              }
            }
          }
        }

        pollLoop(nextOffset)

      case Failure(ex) =>
        println(s"Failed to fetch updates: $ex")
        Future {
          Thread.sleep(5000)
          pollLoop(offset)
        }
    }
  }
}
