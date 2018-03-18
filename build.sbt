name := """akka-backoff-supervisor-sample"""

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.9",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.9" % "test",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.9",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
