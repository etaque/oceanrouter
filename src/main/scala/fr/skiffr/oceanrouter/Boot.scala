package fr.skiffr.oceanrouter

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp

object Boot extends App with SprayCanHttpServerApp {
  val host = "0.0.0.0"
  val port = Option(System.getenv("PORT")).getOrElse("8080").toInt

  // create and start our service actor
  val apiService = system.actorOf(Props[api.ApiServiceActor], "api-service")

  val explorerService = system.actorOf(Props[core.ExplorerActor], "explorer-service")

  // create a new HttpServer using our handler and tell it where to bind to
  newHttpServer(apiService) ! Bind(host, port)

//  val httpService    = actorOf(new HttpService(mainModule.helloService))
//  val rootService    = actorOf(new SprayCanRootService(httpService))
//  val sprayCanServer = actorOf(new HttpServer(new ServerConfig(host = host, port = port)))
}
