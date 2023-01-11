import Dependencies._

ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.10"
Global / onChangedBuildSource := ReloadOnSourceChanges

val `contract-grpc-proto-interface` =
  (project in file("contracts/contract-grpc-proto-interface"))
    .settings(
      name := "contract-grpc-proto-interface",
      libraryDependencies ++= Dependencies.`contract-grpc-proto-interface`,
      Compile / PB.targets := Seq(
        scalapb.gen(grpc = true) -> (Compile / sourceManaged).value
      )
    )

lazy val openTelemetryVersion = "1.20.1"
lazy val root = (project in file("."))
  .enablePlugins(JavaAgent)
  .settings(
    name := "scala-pb-sample",
    libraryDependencies ++= Seq(
      Libraries.grpcNetty
    ),
    javaAgents += JavaAgent(
      "io.opentelemetry.javaagent" % "opentelemetry-javaagent" % openTelemetryVersion % "runtime"
    ),
    run / javaOptions ++= Seq(
      "-Dotel.service.name=scala-pb-sample",
      "-Dotel.exporter.otlp.endpoint=http://localhost:4317",
      "-Dotel.traces.exporter=otlp",
      "-Dotel.metrics.exporter=none",
      "-Dotel.traces.sampler=always_on"
    )
  )
  .dependsOn(`contract-grpc-proto-interface`)
  .aggregate(`contract-grpc-proto-interface`)
