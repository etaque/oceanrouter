package fr.skiffr.oceanrider

import net.sourceforge.jgrib.GribFile
import org.joda.time.DateTime

object App {
  def main(args: Array[String]) {
    val gribFile = new GribFile("/Users/emilien/Downloads/zyGrib_mac-6.0.2/grib/20121127_213227_.grb")
    val gribReader = new GribReader(gribFile)
    val at = new DateTime(2012, 11, 28, 3, 0).toGregorianCalendar
    val c = new Coord(-32.0, -10.0)
    print(gribReader.windAt(c, at))
  }
}
