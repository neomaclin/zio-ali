package zio.ali.mq

import java.util.Properties

import com.aliyun.openservices.ons.api.{Consumer, MessageListener, MessageSelector, ONSFactory}
import zio.{IO, Managed, Task}
import zio.ali.{AliYun, ConnectionError}

final class RocketMQConsumer(consumer: Consumer) extends AliYun.RocketMQService.ConsumerService {
  override def subscribe(topic: String, selector: MessageSelector)(listener: MessageListener): Task[Unit] =
    Task.effect(consumer.subscribe(topic, selector, listener))
}

object RocketMQConsumer {
  def connect(properties: Properties): Managed[ConnectionError, AliYun.RocketMQService.ConsumerService] = {
    (for {
      consumer <- Task.effect(ONSFactory.createConsumer(properties))
      _ <- Task.effect(consumer.start())
    } yield {
      consumer
    }).toManaged(c => IO.succeed(c.shutdown()).unit).bimap(e => ConnectionError(e.getMessage, e.getCause), new RocketMQConsumer(_))
  }
}
