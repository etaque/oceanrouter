package oceanrouter.core

import org.geotools.referencing.GeodeticCalculator
import oceanrouter.conv._
import org.joda.time.DateTime

case class Position(val lon: Double, val lat: Double) {
  val calc = new GeodeticCalculator()
  calc.setStartingGeographicPoint(lon, lat)

  def this(coords: Array[Double]) = this(coords(0), coords(1))

  def angleAndDistanceTo(that: Position): (Double, Double) = {
    calc.setDestinationGeographicPoint(that.lon, that.lat)
    (azimuthToDegrees(calc.getAzimuth), calc.getOrthodromicDistance)
  }

  def move(heading: Double, distance: Double): Position = {
    calc.setDirection(degreesToAzimuth(heading), distance)
    new Position(calc.getDestinationPosition.getCoordinate)
  }

  override def toString: String = lon + "," + lat
}

case class Log(position: Position, time: DateTime,
          speed: Double, heading: Double,
          windSpeed: Double, windHeading: Double)
