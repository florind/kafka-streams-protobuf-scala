name := "skala"

version := "0.1"

scalaVersion := "2.13.3"
val logback = "1.2.3"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka-streams-scala" % "2.6.0",
  "ch.qos.logback" % "logback-core" % logback,
  "ch.qos.logback" % "logback-classic" % logback
)

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-streams-test-utils" % "2.6.0" % "test",
  "org.scalatest" %% "scalatest" % "3.2.1" % "test",
  "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.38.3" % "test",
  "com.dimafeng" %% "testcontainers-scala-kafka" % "0.38.3" % "test",
  "org.awaitility" % "awaitility-scala" % "4.0.3" % Test,
)

PB.targets in Compile := Seq(scalapb.gen() -> (sourceManaged in Compile).value)

Test / fork := true