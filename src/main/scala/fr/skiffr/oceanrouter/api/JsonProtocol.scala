package fr.skiffr.oceanrouter.api

import spray.json._
import fr.skiffr.oceanrouter.core.{Position, Route, RoutingResult}


object JsonProtocol extends DefaultJsonProtocol {

  implicit object PositionFormat extends RootJsonFormat[Position] {
    def write(res: Position) =
      JsArray(JsNumber(res.lon), JsNumber(res.lat))

    def read(value: JsValue) = value match {
      case JsArray(JsNumber(lon) :: JsNumber(lat) :: Nil) => new Position(lon.toDouble, lat.toDouble)
      case _ => deserializationError("Position expected")
    }
  }

  implicit object RoutingResultFormat extends RootJsonFormat[RoutingResult] {
    def write(res: RoutingResult) = res match {
      case RoutingResult(Some(route), _) => JsObject(
        "status" -> JsString("WIN"),
        "route" -> route.positions.toJson
      )
      case RoutingResult(None, _) => JsObject("status" -> JsString("FAIL"))
    }
    def read(value: JsValue) = RoutingResult(None, List[Route]())
  }


}
