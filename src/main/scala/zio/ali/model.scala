package zio.ali

object SMS {
  final val action: String = "SendSms"
  final case class Request(phoneNumber: String,
                           signName: String,
                           templateCode: String)

  final case class Response(BizId: Option[String],
                            Code: String,
                            Message: String,
                            RequestId: String)

}





