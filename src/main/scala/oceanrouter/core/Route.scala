package fr.skiffr.oceanrouter.core

import org.joda.time.DateTime

case class Route(journey: Journey, path: List[Log], position: Position,
                 time: DateTime, finished: Boolean = false) {

  lazy val (headingFromOrigin, distanceFromOrigin) = journey.origin.angleAndDistanceTo(position)
  lazy val (headingToDest, distanceToDest) = position.angleAndDistanceTo(journey.dest)
  lazy val (headingFromDest, _) = journey.dest.angleAndDistanceTo(position)

  lazy val convergence = distanceToDest < distanceFromOrigin

  lazy val wind = WeatherContext.current.windAt(position, time)

  def speedOn(heading: Double) =
    journey.polar.speedFor(wind.angleTo(heading), wind.speed)

  def next(heading: Double): Route = {
    val nextTime = time.plusSeconds(journey.stepDuration)
    val speed = speedOn(heading)
    val distance = speed * journey.stepDuration

    val log = new Log(position, time, speed, heading, wind.speed, wind.direction)

    if (heading == headingToDest && distance >= distanceToDest) {
      // success
      val destTime = time.plusSeconds((distanceToDest / speed).toInt)
      new Route(journey, path :+ log, position.move(heading, distanceToDest), destTime, true)
    }
    else new Route(journey, path :+ log, position.move(heading, distance), nextTime)
  }
}

