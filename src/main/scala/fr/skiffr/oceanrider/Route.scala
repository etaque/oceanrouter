package fr.skiffr.oceanrider

import org.joda.time.DateTime

case class Route(journey: Journey, positions: List[Position], time: DateTime, heading: Double, finished: Boolean = false) {

  val position = positions.head

  lazy val (headingFromOrigin, distanceFromOrigin) = journey.origin.angleAndDistanceTo(position)
  lazy val (headingToDest, distanceToDest) = position.angleAndDistanceTo(journey.dest)
  lazy val (headingFromDest, _) = journey.dest.angleAndDistanceTo(position)

  lazy val convergence = distanceToDest < distanceFromOrigin

  lazy val wind = WeatherContext.current.windAt(position, time)

  def speedOn(heading: Double) =
    Polar.current.speedFor(wind.angleTo(heading), wind.speed)

  def next(newHeading: Double): Route = {
    val nextTime = time.plusSeconds(journey.stepDuration)
    val speed = speedOn(newHeading)
    val distance = speed * journey.stepDuration

    if (newHeading == headingToDest && distance >= distanceToDest) {
      val destTime = time.plusSeconds((distanceToDest / speed).toInt)
      new Route(journey, position.move(newHeading, distanceToDest) +: positions, destTime, newHeading, true)
    }
    else new Route(journey, position.move(newHeading, distance) +: positions, nextTime, newHeading)
  }
}

