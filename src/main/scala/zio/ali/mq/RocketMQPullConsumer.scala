package zio.ali.mq

import java.util.Properties

import com.aliyun.openservices.ons.api.{Message, ONSFactory, PullConsumer}
import zio.{IO, Managed, Task, UIO}
import zio.ali.{AliYun, ConnectionError}
import zio.duration.Duration
import zio.stream._

import scala.collection.JavaConverters._

// TODO: cause poll is used by advanced rocketmq in ali yun service this consumer is not tested, do not use it!!!!
final class RocketMQPullConsumer(consumer: PullConsumer) extends AliYun.RocketMQService.PullConsumerService {
  def poll(topic: String, duration: Duration): ZStream[Any, Throwable, Message] = {
    val pulling = for {
      _ <- Task.effect(consumer.assign(consumer.topicPartitions(topic)))
      messages <- Task.effect(consumer.poll(duration.toMillis))
    } yield {
      messages.iterator()
    }

    Stream.fromEffect(pulling) >>= Stream.fromJavaIterator

  }
}

  object RocketMQPullConsumer {
    def connect(properties: Properties): Managed[ConnectionError, AliYun.RocketMQService.PullConsumerService] =
      (for {
        consumer <- Task.effect(ONSFactory.createPullConsumer(properties))
        _ <- Task.effect(consumer.start())
      } yield {
        consumer
      }).toManaged(c => {
        IO.succeed(c.shutdown())
      }).bimap(e => ConnectionError(e.getMessage, e.getCause), new RocketMQPullConsumer(_))
  }
