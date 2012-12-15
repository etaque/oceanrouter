package fr.skiffr.oceanrouter.api

import akka.actor.Actor
import akka.pattern.ask
import spray.routing.HttpService
import fr.skiffr.oceanrouter.Boot
import akka.util.Timeout
import akka.util.duration._
import fr.skiffr.oceanrouter.core._
import org.joda.time.DateTime
import fr.skiffr.oceanrouter.core.RoutingRequest
import spray.http.MediaTypes.`application/json`
import spray.httpx.SprayJsonSupport._
import fr.skiffr.oceanrouter.api.JsonProtocol._
import spray.httpx.marshalling._
import spray.json._

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
  implicit val timeout = Timeout(60 seconds)

  val route = {
    respondWithMediaType(`application/json`) {
      path("routings") {
        get {
          parameters('fromLon.as[Double], 'fromLat.as[Double], 'toLon.as[Double], 'toLat.as[Double], 'date ?).as(RoutingRequest) {
            (routingRequest: RoutingRequest) =>
              complete {
                Boot.routingService.ask(routingRequest).mapTo[RoutingResult]
              }
          }
        }
      } ~
      path("races" / Rest) { path =>
        complete {
          path
        }
      }
    }
  }
}