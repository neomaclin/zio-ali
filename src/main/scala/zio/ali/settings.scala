package zio.ali

import zio.{IO, ZIO}

final case class AliYunCredentials(accessKeyId: String, secret: String)

sealed trait AliYunRegion {
  val region: String
}

object AliYunRegion {
  def fromString(value: String): Either[InvalidSettings, AliYunRegion] = Right(new AliYunRegion {
    val region: String = value
  })
}

sealed trait AliYunEndpoint {
  val endpoint: String
}

object AliYunEndpoint {
  def fromString(value: String): Either[InvalidSettings, AliYunEndpoint] = Right(new AliYunEndpoint {
    val endpoint: String = value
  })
}

final case class AliYunSettings(region: AliYunRegion, credentials: AliYunCredentials)

object AliYunSettings {

  def from(region: String, credentials: AliYunCredentials): IO[InvalidSettings, AliYunSettings] =
    ZIO.fromEither(AliYunRegion.fromString(region)).map(AliYunSettings(_, credentials))
}
