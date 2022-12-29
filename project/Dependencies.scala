import sbt._

object Dependencies {

  object Libraries {
    val grpcNetty =
      "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion
    val scalapbRuntimeGrpc =
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
  }
}
