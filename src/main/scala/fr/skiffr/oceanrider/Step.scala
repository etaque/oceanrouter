package fr.skiffr.oceanrider

import org.joda.time.DateTime

class Step(position: Coord, direction: Int, time: DateTime) {

  val wind = WeatherContext.current.windAt(position, time)

  lazy val angleToWind = wind.angleTo(direction)

  lazy val speed = Polar.current.speedFor(angleToWind, wind.speed)

//  def next(direction: Int, duration: Int): Step = {
//
//  }
}

