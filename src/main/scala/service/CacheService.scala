package service

import java.time.Instant
import scala.collection.mutable

class CacheService(ttlSeconds: Int):
  private val cache = mutable.Map.empty[String, (Double, Instant)]

  def get(key: String): Option[Double] =
    cache.get(key).flatMap { case (value, ts) =>
      if Instant.now().isBefore(ts.plusSeconds(ttlSeconds)) then Some(value)
      else
        cache.remove(key)
        None
    }

  def put(key: String, value: Double): Unit =
    cache.update(key, (value, Instant.now()))
