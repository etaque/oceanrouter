package oceanrouter.api

import spray.json._
import oceanrouter.core._
import org.joda.time.DateTime

object JsonProtocol extends DefaultJsonProtocol {

  implicit object DateTimeFormat extends JsonFormat[DateTime] {
    def write(res: DateTime) = JsString(res.toString())
    def read(value: JsValue) = value match {
      case JsString(s) => new DateTime(s)
      case _ => deserializationError("DateTime expected")
    }
  }

  implicit object PositionFormat extends RootJsonFormat[Position] {
    def write(res: Position) =
      JsArray(JsNumber(res.lon), JsNumber(res.lat))

    def read(value: JsValue) = value match {
      case JsArray(JsNumber(lon) :: JsNumber(lat) :: Nil) => new Position(lon.toDouble, lat.toDouble)
      case _ => deserializationError("Position expected")
    }
  }

  implicit object LogFormat extends RootJsonFormat[Log] {
    def write(res: Log) = JsObject(
      "position" -> res.position.toJson,
      "time" -> res.time.toJson,
      "speed" -> JsNumber(res.speed),
      "heading" -> JsNumber(res.heading),
      "windSpeed" -> JsNumber(res.windSpeed),
      "windHeading" -> JsNumber(res.windHeading)
    )

    def read(value: JsValue) = value.asJsObject.getFields(
      "position", "time", "speed", "heading", "windSpeed", "windHeading") match {

      case Seq(position, time, JsNumber(speed), JsNumber(heading), JsNumber(windSpeed), JsNumber(windHeading)) =>
        new Log(PositionFormat.read(position), DateTimeFormat.read(time), speed.toDouble, heading.toDouble, windSpeed.toDouble, windHeading.toDouble)
      case _ => deserializationError("Log expected")
    }
  }

  implicit object RoutingResultFormat extends RootJsonFormat[RoutingResult] {
    def write(res: RoutingResult) = res match {
      case RoutingResult(Some(route), _) => JsObject(
        "success" -> JsTrue,
        "bestRoute" -> JsObject(
          "time" -> route.time.toJson,
          "path" -> route.path.toJson
        )
      )
      case RoutingResult(None, _) => JsObject("success" -> JsFalse)
    }
    def read(value: JsValue) = RoutingResult(None, List[Route]())
  }
}
