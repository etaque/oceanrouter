package fr.skiffr.oceanrouter.core

import org.joda.time.DateTime

object App {
  def main(args: Array[String]) {
    val at = new DateTime(2012, 11, 28, 3, 0)
    val p1 = new Position(-20.0, -11.0)
    val p2 = new Position(-33.0, -10.0)

    val j = new Journey(p1, p2, 60*60*3)
    val explorer = new Explorer(j, at)

    explorer.run(100) match {
      case (Some(winner), routes) => println("WIN!\n" + winner.positions.mkString(";"))
      case (None, routes) => println("FAIL")
    }
  }
}
