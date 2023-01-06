import sbt._

object Dependencies {

  object Versions {
    val kamon = "2.5.11"
  }
  object Libraries {
    val grpcNetty =
      "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion

    val kamonBundle = "io.kamon" %% "kamon-bundle" % Versions.kamon
    val kamonJaeger = "io.kamon" %% "kamon-jaeger" % Versions.kamon
  }

  lazy val `contract-grpc-proto-interface` = Seq(
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
    "com.google.api.grpc" % "proto-google-common-protos" % "2.11.0" % "protobuf-src"
  )
}
