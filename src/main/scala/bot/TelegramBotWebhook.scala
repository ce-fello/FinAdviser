package bot

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import util.HttpClient
import io.circe._, io.circe.parser._, io.circe.generic.auto._
import scala.concurrent.{ExecutionContext, Future}

case class Update(update_id: Long, message: Option[TelegramMessage])

case class TelegramMessage(message_id: Long, chat: TelegramChat, text: Option[String])

case class TelegramChat(id: Long)

class TelegramBotWebhook(
                          token: String,
                          http: HttpClient,
                          handler: CommandHandler
                        )(using ec: ExecutionContext, system: ActorSystem, mat: Materializer) {

  private val baseUrl = s"https://api.telegram.org/bot$token"

  def start(host: String = "0.0.0.0", port: Int = 8080): Unit = {
    val route =
      path("webhook") {
        post {
          entity(as[String]) { body =>
            val f: Future[HttpResponse] = Future {
              parse(body).flatMap(_.as[Update]) match
                case Right(update) =>
                  update.message.foreach { msg =>
                    msg.text.foreach { text =>
                      handler.handleCommand(text).foreach { reply =>
                        sendMessage(msg.chat.id, reply)
                      }
                    }
                  }
                case Left(err) =>
                  println(s"Failed to parse update: $err")
              HttpResponse(StatusCodes.OK)
            }(ec)

            complete(f)
          }
        }
      }

    val bindingFuture = Http().newServerAt(host, port).bind(route)
    println(s"Webhook bot started at http://$host:$port")
    println("Press ENTER to stop...")
    scala.io.StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }

  private def sendMessage(chatId: Long, text: String): Future[Unit] =
    http.postForm(s"$baseUrl/sendMessage", Map("chat_id" -> chatId.toString, "text" -> text)).map(_ => ())
}
