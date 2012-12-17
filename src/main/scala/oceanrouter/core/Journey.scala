package fr.skiffr.oceanrouter.core

import org.joda.time.DateTime

class Journey(val origin: Position, val dest: Position, val stepDuration: Int) {

  def this(origin: Position, dest: Position) = this(origin, dest, 60*60*1)

  val (heading, distance) = origin.angleAndDistanceTo(dest)
  val (backHeading, _) = dest.angleAndDistanceTo(origin)

  val polar = Polar.current

  def start(at: DateTime): Route = new Route(this, List[Log](), origin, at)
}
