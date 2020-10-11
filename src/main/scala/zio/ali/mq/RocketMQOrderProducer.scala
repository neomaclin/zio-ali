package zio.ali.mq

import java.util.Properties

import com.aliyun.openservices.ons.api.{Message, ONSFactory, SendResult}
import com.aliyun.openservices.ons.api.exception.ONSClientException
import com.aliyun.openservices.ons.api.order.OrderProducer
import zio.{IO, Managed, Task, ZIO}
import zio.ali.{AliYun, ConnectionError}
import zio.blocking.{Blocking, blocking}

final class RocketMQOrderProducer(producer: OrderProducer) extends AliYun.RocketMQService.OrderProducerService {
  override def send(message: Message, shardingKey: String): ZIO[Blocking, ONSClientException, SendResult] =
    blocking(Task.effect(producer.send(message, shardingKey))).mapError(new ONSClientException(_))
}

object RocketMQOrderProducer{
  def connect(properties: Properties): Managed[ConnectionError,AliYun.RocketMQService.OrderProducerService] = {
    (for {
      producer <- Task.effect(ONSFactory.createOrderProducer(properties))
      _ <- Task.effect(producer.start())
    }yield {
      producer
    }).toManaged(p => IO.succeed(p.shutdown())).bimap(e => ConnectionError(e.getMessage,e.getCause),new RocketMQOrderProducer(_))
  }
}
