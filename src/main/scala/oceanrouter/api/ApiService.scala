package oceanrouter.api

import akka.actor.Actor
import akka.pattern.ask
import spray.routing.HttpService
import spray.http.HttpHeaders.RawHeader
import oceanrouter.Boot
import oceanrouter.core._
import oceanrouter.core.RoutingRequest
import oceanrouter.api.JsonProtocol._
import akka.util.Timeout
import akka.util.duration._
import org.joda.time.DateTime
import spray.http.MediaTypes.`application/json`
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling._
import spray.json._
import spray.http.HttpHeader

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

  case class `Access-Control-Allow-Origin`(origin: String) extends HttpHeader {
    def name = "Access-Control-Allow-Origin"
    def lowercaseName = "access-control-allow-origin"
    def value = origin
  }

  val route = {
    respondWithHeaders(`Access-Control-Allow-Origin`("http://oceanrouter-ui.dev")) {
      respondWithMediaType(`application/json`) {
        path("routings") {
          get {
            parameters('fromLon.as[Double], 'fromLat.as[Double], 'toLon.as[Double], 'toLat.as[Double]).as(RoutingRequest) {
              (routingRequest: RoutingRequest) =>
                complete {
                  Boot.routingService.ask(routingRequest).mapTo[RoutingResult]
                }
            }
          }
        }
      }
    }
  }
}