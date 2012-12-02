package fr.skiffr.oceanrider

import org.joda.time.DateTime

object App {
  def main(args: Array[String]) {
    val at = new DateTime(2012, 11, 28, 3, 0)
    val p = new Position(-32.0, -10.0)
    val s1 = new Step(p, at)

    println(s1.next(90, 3600*3).position)
  }
}
