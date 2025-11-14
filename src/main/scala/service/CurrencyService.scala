package service

import model.CurrencyRate
import util.HttpClient
import io.circe.parser.*
import scala.concurrent.{ExecutionContext, Future}

class CurrencyService(http: HttpClient, cache: CacheService, apiKey: String)(using ec: ExecutionContext):

  def getRate(from: String, to: String = "RUB"): Future[CurrencyRate] =
    val key = s"$from-$to"

    cache.get(key) match
      case Some(value) =>
        Future.successful(CurrencyRate(from, to, value))
      case None =>
        val url = s"https://api.exchangerate.host/live?access_key=$apiKey&source=$from&currencies=$to"

        http.get(url).map { body =>

          val parsed = parse(body).getOrElse(
            throw Exception(s"Failed to parse JSON: $body")
          )
          val quotes = parsed.hcursor.downField("quotes").as[Map[String, Double]].getOrElse(Map.empty)
          val rateKey = s"$from$to"
          val rate = quotes.getOrElse(rateKey, throw new Exception(s"No rate for $rateKey"))

          cache.put(key, rate)
          CurrencyRate(from, to, rate)
        }.recover { case e =>
          CurrencyRate(from, to, 0.0)
        }
