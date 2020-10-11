package zio.ali.mq

import java.util.Properties

import com.aliyun.openservices.ons.api.{MessageSelector, ONSFactory}
import com.aliyun.openservices.ons.api.order.{MessageOrderListener, OrderConsumer}
import zio.{IO, Managed, Task}
import zio.ali.{AliYun, ConnectionError}

final class RocketMQOrderConsumer(consumer: OrderConsumer) extends AliYun.RocketMQService.OrderConsumerService {
  override def subscribe(topic: String, selector: MessageSelector)(listener: MessageOrderListener): Task[Unit] =
    Task.effect(consumer.subscribe(topic, selector, listener))
}

object RocketMQOrderConsumer {
  def connect(properties: Properties): Managed[ConnectionError, AliYun.RocketMQService.OrderConsumerService] = {
    (for {
      consumer <- Task.effect(ONSFactory.createOrderedConsumer(properties))
      _ <- Task.effect(consumer.start())
    } yield {
      consumer
    }).toManaged(r => IO.succeed(r.shutdown()))
      .bimap(e => ConnectionError(e.getMessage, e.getCause), new RocketMQOrderConsumer(_))
  }
}
