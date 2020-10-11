package zio.ali.mq

import java.util.Properties

import com.aliyun.openservices.ons.api.exception.ONSClientException
import com.aliyun.openservices.ons.api._
import zio.ali.{AliYun, ConnectionError}
import zio.blocking.{Blocking, blocking}
import zio.{IO, Managed, Task, ZIO}

final class RocketMQProducer(producer: Producer) extends AliYun.RocketMQService.ProducerService {
  override def send(message: Message): ZIO[Blocking, ONSClientException, SendResult] =
    blocking(Task.effect(producer.send(message))).mapError(new ONSClientException(_))

  def sendAsync(message: Message): IO[OnExceptionContext, SendResult] =
    IO.effectAsync[OnExceptionContext, SendResult] { callback =>
      producer.sendAsync(message, new SendCallback {
        override def onSuccess(sendResult: SendResult): Unit = callback(IO.succeed(sendResult))
        override def onException(context: OnExceptionContext): Unit = callback(IO.fail(context))
      })
    }

  def sendOneway(message: Message): Task[Unit] = {
    Task.effect(producer.sendOneway(message))
  }

}

object RocketMQProducer {
  def connect(properties: Properties): Managed[ConnectionError, AliYun.RocketMQService.ProducerService] = {
    (for {
      producer <- Task.effect(ONSFactory.createProducer(properties))
      _ <- Task.effect(producer.start())
    } yield {
      producer
    }).toManaged(p => IO.succeed(p.shutdown())).bimap(e => ConnectionError(e.getMessage, e.getCause), new RocketMQProducer(_))
  }
}
