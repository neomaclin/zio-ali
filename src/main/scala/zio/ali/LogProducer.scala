package zio.ali

import com.aliyun.openservices.aliyun.log.producer.errors.ProducerException
import com.aliyun.openservices.aliyun.log.producer.{ProducerConfig, Result, LogProducer => Producer}
import zio.{Managed, ZIO}
import zio.ali.models.Log.LogServiceRequest
import zio.blocking.Blocking
import zio.duration.Duration

import scala.collection.JavaConverters._

final class LogProducer(producer: Producer) extends AliYun.LogService {

  def send(request: LogServiceRequest): ZIO[Blocking, ProducerException, Result] = {
    import request.{project, logStore, topic, source, shardHash, logItems}
    ZIO.fromFutureJava(producer.send(project, logStore, topic, source, shardHash.orNull, logItems.asJava)).mapError {
      case e: ProducerException => e
      case e => new ProducerException(e)
    }
  }

}

object LogProducer {
  def connect(config: ProducerConfig, timeOut: Duration): Managed[ConnectionError, AliYun.LogService] = Managed.makeEffect {
    new Producer(config)
  }(_.close(timeOut.toMillis)).bimap(e => ConnectionError(e.getMessage, e.getCause), new LogProducer(_))
}
