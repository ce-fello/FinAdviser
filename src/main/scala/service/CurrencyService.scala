package service

import model.CurrencyRate
import util.HttpClient
import io.circe.parser._
import io.circe.generic.auto._
import scala.concurrent.{ExecutionContext, Future}

case class LatestResponse(success: Boolean, base: String, rates: Map[String, Double])

class CurrencyService(http: HttpClient, cache: CacheService)(using ec: ExecutionContext):
  def getRate(from: String, to: String = "RUB"): Future[CurrencyRate] =
    val key = s"$from-$to"
    cache.get(key) match
      case Some(value) => Future.successful(CurrencyRate(from, to, value))
      case None =>
        val url = s"https://api.exchangerate.host/latest?base=$from&symbols=$to"
        http.get(url).map { body =>
          val parsed = parse(body).flatMap(_.as[LatestResponse]).getOrElse(throw new Exception("Failed to parse JSON"))
          val rate = parsed.rates.getOrElse(to, throw new Exception(s"No rate for $to"))
          cache.put(key, rate)
          CurrencyRate(from, to, rate)
        }
