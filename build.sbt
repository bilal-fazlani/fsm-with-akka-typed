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

lazy val root = (project in file(".")).settings(
  name := "actor_reply",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
    "com.typesafe.akka" %% "akka-actor-typed"     % akkaVersion,
    "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-testkit"         % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion % Test,
    "org.scalatest"     %% "scalatest"            % "3.0.8" % Test
  )
)
