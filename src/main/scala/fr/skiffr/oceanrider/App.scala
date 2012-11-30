package fr.skiffr.oceanrider

import net.sourceforge.jgrib.GribFile

object App {
  def main(args: Array[String]) {
    val gribFile = new GribFile("/Users/emilien/Downloads/zyGrib_mac-6.0.2/grib/20121127_213227_.grb")
    val gribReader = new GribReader(gribFile)
    print(gribReader.uRecords(1))
  }
}
