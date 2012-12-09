package fr.skiffr.oceanrider

import org.joda.time.DateTime

object App {
  def main(args: Array[String]) {
    val at = new DateTime(2012, 11, 28, 3, 0)
    val p1 = new Position(-31.0, -9.0)
    val p2 = new Position(-33.0, -11.0)

    val j = new Journey(p1, p2, 60*60*3)
    val explorer = new Explorer(j, at)

    explorer.run match {
      case Some(winner) => println("WIN!\n" + winner.positions.mkString(";"))
      case None => println("FAIL")
    }
  }
}
