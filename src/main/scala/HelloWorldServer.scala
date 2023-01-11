import io.grpc.{
  ForwardingServerCall,
  Metadata,
  Server,
  ServerBuilder,
  ServerCall,
  ServerCallHandler,
  ServerInterceptor,
  Status
}
import kamon.Kamon
import scala.concurrent.{ExecutionContext, Future}
import sample.helloworld.{
  GoodbyeReply,
  GoodbyeRequest,
  GreeterGrpc,
  HelloReply,
  HelloRequest
}

object HelloWorldServer {
  def main(args: Array[String]): Unit = {
    Kamon.init()
    new HelloWorldServer(ExecutionContext.global).start().awaitTermination()
  }

  private val port = 50051
}

class HelloWorldServer(executionContext: ExecutionContext) { self =>
  private def start(): Server = {
    ServerBuilder
      .forPort(HelloWorldServer.port)
      .addService(GreeterGrpc.bindService(new GreeterImpl, executionContext))
      .intercept(new KamonInterceptor())
      .build()
      .start
  }

  private class GreeterImpl extends GreeterGrpc.Greeter {
    override def sayHello(req: HelloRequest): Future[HelloReply] = {
      val reply = HelloReply(message = "Hello " + req.name)
      println(reply)
      Future.successful(reply)
    }

    override def sayGoodbye(req: GoodbyeRequest): Future[GoodbyeReply] = {
      val reply = GoodbyeReply(message = "Goodbye " + req.name)
      println(reply)
      Future.successful(reply)
    }
  }

  private class KamonInterceptor extends ServerInterceptor {

    override def interceptCall[ReqT, RespT](
        call: ServerCall[ReqT, RespT],
        headers: Metadata,
        next: ServerCallHandler[ReqT, RespT]
    ): ServerCall.Listener[ReqT] = {
      val span =
        Kamon.spanBuilder(call.getMethodDescriptor.getBareMethodName).start()

      def wrapperCall: ServerCall[ReqT, RespT] =
        new ForwardingServerCall.SimpleForwardingServerCall[ReqT, RespT](
          call
        ) {
          override def request(numMessages: Int): Unit = {
            println("pre request")
            super.request(numMessages)
          }

          override def close(status: Status, trailers: Metadata): Unit = {
            println("close request")
            span.finish()
            super.close(status, trailers)
          }
        }
      next.startCall(wrapperCall, headers)
    }
  }

}
