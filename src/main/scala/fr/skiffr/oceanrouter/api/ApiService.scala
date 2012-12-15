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
      get {
        path("") {
          parameters('fromLon.as[Double], 'fromLat.as[Double], 'toLon.as[Double], 'toLat.as[Double], 'date.as[String]) {
            (fromLon, fromLat, toLon, toLat, date) =>

              val at = DateTime.parse(date)
//              val at = new DateTime(2012, 11, 28, 3, 0)
              val p1 = new Position(fromLon, fromLat)
              val p2 = new Position(toLon, toLat)

              complete {
                Boot.explorerService.ask(RoutingRequest(p1, p2, at)).mapTo[RoutingResult]
              }
          }
        }
      }
    }
  }
}