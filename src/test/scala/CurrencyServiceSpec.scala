import org.scalatest.funsuite.AnyFunSuite
import service.{CurrencyService, CacheService}
import util.HttpClient
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

class CurrencyServiceSpec extends AnyFunSuite:
  test("Fetch USD rate successfully") {
    val cache = new CacheService(1)
    val http = new HttpClient
    val svc = new CurrencyService(http, cache)
    val rate = Await.result(svc.getRate("USD"), 10.seconds)
    assert(rate.rate > 0)
  }
