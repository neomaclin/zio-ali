package zio.ali.mq

import java.util.Properties

import com.aliyun.openservices.ons.api.exception.ONSClientException
import com.aliyun.openservices.ons.api.transaction.{LocalTransactionChecker, TransactionProducer}
import com.aliyun.openservices.ons.api.{Message, ONSFactory, SendResult}
import zio.ali.{AliYun, ConnectionError}
import zio.blocking.{Blocking, blocking}
import zio.{IO, Managed, Task, ZIO}

final class RocketMQTransactionProducer(producer: TransactionProducer) extends AliYun.RocketMQService.TransactionProducerService {
  def send[T](message: Message, executor: MQLocalTransactionExecutor[T], arg: T): ZIO[Blocking,ONSClientException, SendResult] =
    blocking(Task.effect(producer.send(message, executor, arg)))
      .mapError(new ONSClientException(_))
}

object RocketMQTransactionProducer {
  def connect(properties: Properties, checker: LocalTransactionChecker): Managed[ConnectionError, AliYun.RocketMQService.TransactionProducerService] =
    (for {
      producer <- Task.effect(ONSFactory.createTransactionProducer(properties, checker))
      _ <- Task.effect(producer.start())
    } yield {
      producer
    }).toManaged(p => IO.succeed(p.shutdown())).bimap(e => ConnectionError(e.getMessage, e.getCause), new RocketMQTransactionProducer(_))
}