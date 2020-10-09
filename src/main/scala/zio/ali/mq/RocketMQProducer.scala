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
    IO.effectAsync[OnExceptionContext, SendResult] { callback => {
      producer.sendAsync(message, new SendCallback {
        override def onSuccess(sendResult: SendResult): Unit = {
          callback(IO.succeed(sendResult))
        }

        override def onException(context: OnExceptionContext): Unit = callback(IO.fail(context))
      })
    }
    }

  def sendOneway(message: Message): Task[Unit] = {
    Task.effect(producer.sendOneway(message))
  }

}

object RocketMQProducer {
  def connect(properties: Properties): Managed[ConnectionError, AliYun.RocketMQService.ProducerService] = {
    Managed.makeEffect {
      val producer = ONSFactory.createProducer(properties)
      producer.start()
      producer
    }(_.shutdown()).map(new RocketMQProducer(_)).mapError(e => ConnectionError(e.getMessage, e.getCause))
  }
}
