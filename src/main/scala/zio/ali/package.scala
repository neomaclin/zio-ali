package zio

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.exceptions.ClientException
import zio.blocking.Blocking

package object ali {
  type AliYun          = Has[AliYun.Service]

  object AliYun {

    trait Service {
      def sendSMS(request: SMS.Request, templateParamValue: String): ZIO[Blocking, ClientException, SMS.Response]
      def execute[T](f: DefaultAcsClient => Task[T]): ZIO[Blocking, ClientException, T]
    }
  }

  def live(region: String, credentials: AliYunCredentials): Layer[ConnectionError, AliYun] =
    ZLayer.fromManaged(Live.connect(region, credentials))

  val live: ZLayer[AliYunSettings, ConnectionError, AliYun] = ZLayer.fromFunctionManaged(Live.connect)

  def execute[T](f: DefaultAcsClient => Task[T]): ZIO[Blocking with AliYun, ClientException, T] =
    ZIO.accessM(_.get[AliYun.Service].execute(f))
}
