val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Distributed systems 1",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "com.softwaremill.sttp.client4" %% "core" % "4.0.0-M6",
    libraryDependencies += "org.json4s" %% "json4s-native" % "4.0.6"
  )
