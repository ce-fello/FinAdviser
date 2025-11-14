ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "FinAdviser",

    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client3" %% "core" % "3.9.0",
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-future" % "3.9.0",

      "io.circe" %% "circe-core" % "0.14.6",
      "io.circe" %% "circe-generic" % "0.14.6",
      "io.circe" %% "circe-parser" % "0.14.6",

      "org.scalatest" %% "scalatest" % "3.2.18" % Test
    )
  ) 
