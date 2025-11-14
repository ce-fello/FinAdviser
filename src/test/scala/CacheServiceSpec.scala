import org.scalatest.funsuite.AnyFunSuite
import service.CacheService

class CacheServiceSpec extends AnyFunSuite:
  test("Cache stores and retrieves values within TTL") {
    val cache = new CacheService(2)
    cache.put("test", 42.0)
    assert(cache.get("test").contains(42.0))
  }
