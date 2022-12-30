import sbt._

object Dependencies {

  object Libraries {
    val grpcNetty =
      "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion
  }

  lazy val `contract-grpc-proto-interface` = Seq(
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
    "com.google.api.grpc" % "proto-google-common-protos" % "2.11.0" % "protobuf-src"
  )
}
