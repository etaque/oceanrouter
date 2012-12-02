package fr.skiffr.oceanrider

import org.joda.time.DateTime
import org.geotools.referencing.GeodeticCalculator

case class Step(position: Position, time: DateTime) {

  val wind = WeatherContext.current.windAt(position, time)

  def speedOn(heading: Double) =
    Polar.current.speedFor(wind.angleTo(heading), wind.speed)

  def nextPosition(heading: Double, distance: Double): Position = {
    val calc = new GeodeticCalculator()
    calc.setStartingGeographicPoint(position.lon, position.lat)
    calc.setDirection(heading, distance)
    new Position(calc.getDestinationPosition.getCoordinate)
  }

  def next(heading: Double, duration: Int): Step = {
    val nextTime = time.plusSeconds(duration)
    val distance = speedOn(heading) * duration

    new Step(nextPosition(heading, distance), nextTime)
  }
}

