package zio.ali

import com.aliyuncs.{CommonRequest, DefaultAcsClient}
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.http.MethodType
import com.aliyuncs.profile.DefaultProfile
import zio.{ZIO, _}
import zio.blocking.{Blocking, blocking}

final class Live(unsafeClient: DefaultAcsClient) extends AliYun.Service {

  private def buildSMSRequest(smsRequest: SMS.Request, templateParamValue: String) = {
    val request = new CommonRequest
    //request.setProtocol(ProtocolType.HTTPS);
    request.setSysMethod(MethodType.POST)
    request.setSysDomain("dysmsapi.aliyuncs.com")
    request.setSysVersion("2017-05-25")
    request.setSysAction(SMS.action)
    request.putQueryParameter("RegionId", "cn-hangzhou")
    request.putQueryParameter("PhoneNumbers", smsRequest.phoneNumber)
    request.putQueryParameter("SignName", smsRequest.signName)
    request.putQueryParameter("TemplateCode", smsRequest.templateCode)
    request.putQueryParameter("TemplateParam", "{\"code\":\"" + templateParamValue + "\"}")
    request
  }

  def execute[T](f: DefaultAcsClient => Task[T]): ZIO[Blocking, ClientException, T] =
    blocking(f(unsafeClient)).mapError(e => new ClientException(e))

  def sendSMS(request: SMS.Request, templateParamValue: String): ZIO[Blocking, ClientException, SMS.Response] = {
    import io.circe.generic.auto._, io.circe.parser._
    for {
      response <- execute(client => Task.effect(client.getCommonResponse(buildSMSRequest(request, templateParamValue))))
      result <-
        if (response.getHttpResponse.isSuccess) {
          for {
            value <- ZIO.fromEither(parse(response.getData).flatMap(_.as[SMS.Response])).mapError(e => FailureToParseResponse(e.getMessage))
            success <- if (value.Code.equals("OK")) ZIO.succeed(value) else ZIO.fail(FailureToSendSMS("发送短信失败"))
          } yield {
            success
          }
        } else {
          ZIO.fail(FailureToSendSMS("发送短信失败，请稍后重试"))
        }
    } yield {
      result
    }
  }
}

object Live {

  def connect(region: String,
              credentials: AliYunCredentials): Managed[ConnectionError, AliYun.Service] =
    AliYunSettings
      .from(region, credentials)
      .toManaged_
      .mapError(e => ConnectionError(e.getMessage, e.getCause))
      .flatMap(connect)

  def connect(settings: AliYunSettings): Managed[ConnectionError, AliYun.Service] =
    Managed.makeEffect {
      val profile = DefaultProfile.getProfile(settings.region.region, settings.credentials.accessKeyId, settings.credentials.secret)
      new DefaultAcsClient(profile)
    }(_.shutdown())
      .map(new Live(_))
      .mapError(e => ConnectionError(e.getMessage, e.getCause))

}

