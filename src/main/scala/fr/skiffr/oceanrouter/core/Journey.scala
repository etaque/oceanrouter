package fr.skiffr.oceanrouter.core

import org.joda.time.DateTime
import fr.skiffr.oceanrouter.core.Route

class Journey(val origin: Position, val dest: Position, val stepDuration: Int) {

  val (heading, distance) = origin.angleAndDistanceTo(dest)
  val (backHeading, _) = dest.angleAndDistanceTo(origin)

  def start(at: DateTime): Route = new Route(this, List[Position](origin), at, heading)
}
