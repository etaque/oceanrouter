package fr.skiffr.oceanrouter

import spray.can.server.HttpServer
import spray.routing.HttpService
import spray.io.IOBridge.Bind
import akka.actor.Props

object Boot extends App {
  val host = "0.0.0.0"
  val port = Option(System.getenv("PORT")).getOrElse("8080").toInt

  // create and start our service actor
  val service = system.actorOf(Props[ApiServiceActor], "api-service")

  // create a new HttpServer using our handler and tell it where to bind to
  newHttpServer(service) ! Bind(host, port)

//  val httpService    = actorOf(new HttpService(mainModule.helloService))
//  val rootService    = actorOf(new SprayCanRootService(httpService))
//  val sprayCanServer = actorOf(new HttpServer(new ServerConfig(host = host, port = port)))
}
