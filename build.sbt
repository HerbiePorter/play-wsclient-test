import sbt.Keys.{libraryDependencies, organization}

val appName = "play-wsclient-test"

val root = Project(appName, file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := appName,
    organization := "wsclient",

    scalaVersion := "2.12.8",

    libraryDependencies += guice,
    libraryDependencies += ws,

    libraryDependencies ++= Seq(
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % "test",
    ),

  )
