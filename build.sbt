ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file(".")).settings(
  name := "CurrencyRateBot",
  version := "0.1.0",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % "2.8.1",
    "com.typesafe.akka" %% "akka-stream" % "2.8.1",
    "com.typesafe.akka" %% "akka-http" % "10.5.0",
    "com.softwaremill.sttp.client3" %% "core" % "3.9.0",
    "com.softwaremill.sttp.client3" %% "circe" % "3.9.0",
    "com.softwaremill.sttp.client3" %% "async-http-client-backend-future" % "3.9.0",
    "io.circe" %% "circe-core" % "0.14.6",
    "io.circe" %% "circe-generic" % "0.14.6",
    "io.circe" %% "circe-parser" % "0.14.6"
  )
)
