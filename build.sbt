import BuildHelper._

//inThisBuild(
//  List(
//    organization := "dev.zio",
//    homepage := Some(url("https://zio.github.io/")),
//    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
//    developers := List(
//      Developer("", "Neo", "neomac.lin@gmail.com", url("https://github.com/neomaclin"))
//    ),
//    Test / fork := true,
//    parallelExecution in Test := false,
//    publishMavenStyle := true,
//    pgpPassphrase := sys.env.get("PGP_PASSWORD").map(_.toArray),
//    pgpPublicRing := file("/tmp/public.asc"),
//    pgpSecretRing := file("/tmp/secret.asc"),
//    scmInfo := Some(
//      ScmInfo(url("https://github.com/zio/"), "scm:git:git@github.com:")
//    )
//  )
//)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

val zioVersion = "1.0.1"
val circeVersion = "0.13.0"

lazy val `zio-ali` = project
  .in(file("."))
  .settings(stdSettings("zio-ali"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio"                %% "zio"                         % zioVersion,
      "org.scala-lang.modules" %% "scala-collection-compat"     % "2.2.0",
      "com.aliyun"              % "aliyun-java-sdk-core" % "4.0.3",
      "dev.zio"                %% "zio-test"                    % zioVersion % Test,
      "dev.zio"                %% "zio-test-sbt"                % zioVersion % Test
    ) ++ Seq(
       "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

