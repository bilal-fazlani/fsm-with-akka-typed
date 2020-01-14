inThisBuild(
  List(
    organization := "com.example",
    scalaVersion := "2.13.1",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-Xlint:_,-missing-interpolator",
      "-Ywarn-dead-code"
    )
  )
)

lazy val akkaHttpVersion = "10.1.11"
lazy val akkaVersion     = "2.6.1"
val akkaOrg              = "com.typesafe.akka"

lazy val root = (project in file(".")).settings(
  name := "actor_reply",
  libraryDependencies ++= Seq(
    akkaOrg %% "akka-http"        % akkaHttpVersion,
    akkaOrg %% "akka-stream"      % akkaVersion,
    akkaOrg %% "akka-actor-typed" % akkaVersion
  )
)
