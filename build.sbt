import Dependencies._

ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.10"
Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .settings(
    name := "scala-pb-sample",
    libraryDependencies ++= Seq(
      Libraries.grpcNetty,
      Libraries.scalapbRuntimeGrpc
    ),
    Compile / PB.targets := Seq(
      scalapb.gen(grpc = true) -> (Compile / sourceManaged).value
    )
  )
