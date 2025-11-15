import org.scalatest.funsuite.AnyFunSuite
import service.{CacheService, CurrencyService}
import util.{Config, HttpClient}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.*

class CurrencyServiceSpec extends AnyFunSuite:
  test("Fetch USD rate successfully") {
    val cfg = Config.load()
    val cache = new CacheService(1)
    val http = new HttpClient
    val svc = new CurrencyService(http, cache, cfg.exchangeApiKey)
    val rate = Await.result(svc.getRate("USD"), 10.seconds)
    assert(rate.rate > 0)
  }
