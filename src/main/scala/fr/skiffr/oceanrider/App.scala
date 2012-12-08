package fr.skiffr.oceanrider

import org.joda.time.DateTime

object App {
  def main(args: Array[String]) {
    val at = new DateTime(2012, 11, 28, 3, 0)
    val p1 = new Position(-32.0, -10.0)
    val p2 = new Position(-30.0, -9.0)

    val route = new Route(p1, p2, 60*60*3)
    val start = route.start(at)

//    println(route.newHeading + " / " + route.distance)

    val delta = 60
    val firstIteration: IndexedSeq[Step] = -delta to delta by 10 map(d =>
      start.next((route.heading + d) % 360))

    val secondIteration = firstIteration.map(step => step.next(step.heading))

    println(secondIteration.map(_.positions.head).mkString("\n"))

//    println(secondIteration.head.positions.mkString("\n"))
//    println(Polar.current.lowVmg(10))
  }
}
