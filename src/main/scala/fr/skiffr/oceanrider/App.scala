package fr.skiffr.oceanrider

import net.sourceforge.jgrib.GribFile
import org.joda.time.DateTime

object App {
  def main(args: Array[String]) {
    val at = new DateTime(2012, 11, 28, 3, 0)
    val c = new Coord(-32.0, -10.0)
    val s1 = new Step(c, 180, at)

    println(s1.speed)
  }
}
