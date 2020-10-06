package zio.ali

import zio.{ExitCode, URIO}
import zio.blocking.Blocking
import zio.console._


object OSSTest extends zio.App {

  val layer = oss("", AliYunCredentials("", ""))

  val fullLayer = layer ++ Blocking.live

  val result = ossBucketInfo("shadow-iris")

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = (for {
    r <- result
    _ <- putStrLn(r.getBucket.getIntranetEndpoint)
  } yield ()).provideLayer(fullLayer++Console.live).exitCode
}
