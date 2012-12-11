package fr.skiffr.oceanrouter.api

import akka.actor.Actor
import akka.pattern.ask
import spray.routing.HttpService
import fr.skiffr.oceanrouter.Boot
import akka.util.Timeout
import akka.util.duration._

class ApiServiceActor extends Actor with ApiService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(route)
}

trait ApiService extends HttpService {
  val route = {
    get {
      path("") {
        implicit val timeout = Timeout(10 seconds)
        Boot.explorerService.ask("test")
        complete("WIN")
      }
    }
  }
}