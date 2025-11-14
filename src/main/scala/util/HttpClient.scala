package util

import sttp.client3._
import sttp.client3.asynchttpclient.future.AsyncHttpClientFutureBackend
import scala.concurrent.{ExecutionContext, Future}

class HttpClient(using ec: ExecutionContext):
  private val backend = AsyncHttpClientFutureBackend()

  def get(url: String): Future[String] =
    basicRequest.get(uri"$url").send(backend).map(_.body).map {
      case Right(body) => body
      case Left(err) => throw new Exception(err)
    }

  def postForm(url: String, data: Map[String, String]): Future[String] =
    basicRequest.post(uri"$url").body(data).send(backend).map(_.body).map {
      case Right(body) => body
      case Left(err) => throw new Exception(err)
    }

  def close(): Unit = backend.close()
