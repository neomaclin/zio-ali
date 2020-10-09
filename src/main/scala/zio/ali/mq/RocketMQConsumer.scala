package zio.ali.mq

import java.util.Properties

import com.aliyun.openservices.ons.api.{Consumer, MessageListener, MessageSelector, ONSFactory}
import zio.{Managed, Task}
import zio.ali.{AliYun, ConnectionError}

final class RocketMQConsumer(consumer: Consumer) extends AliYun.RocketMQService.ConsumerService {
  override def subscribe(topic: String, selector: MessageSelector)(listener: MessageListener): Task[Unit] =
    Task.effect(consumer.subscribe(topic, selector, listener))
}

object RocketMQConsumer {
  def connect(properties: Properties): Managed[ConnectionError, AliYun.RocketMQService.ConsumerService] = {
    Managed.makeEffect {
      val consumer = ONSFactory.createConsumer(properties)
      consumer.start()
      consumer
    }(_.shutdown()).map(new RocketMQConsumer(_)).mapError(e => new ConnectionError(e.getMessage, e.getCause))
  }
}
