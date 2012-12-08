package fr.skiffr.oceanrider

import org.joda.time.DateTime
import org.geotools.referencing.GeodeticCalculator

case class Step(route: Route, positions: List[Position], time: DateTime, heading: Double) {

  val position = positions.head
  val (angleFromOrigin, distanceFromOrigin) = route.origin.angleAndDistanceTo(position)
  val (angleFromDest, distanceFromDest) = route.dest.angleAndDistanceTo(position)

  lazy val wind = WeatherContext.current.windAt(position, time)

  def speedOn(heading: Double) =
    Polar.current.speedFor(wind.angleTo(heading), wind.speed)

  def next(newHeading: Double): Step = {
    val nextTime = time.plusSeconds(route.stepDuration)
    val distance = speedOn(newHeading) * route.stepDuration

    new Step(route, position.move(newHeading, distance) +: positions, nextTime, newHeading)
  }
}

