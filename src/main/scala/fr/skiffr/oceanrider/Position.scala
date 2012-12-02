package fr.skiffr.oceanrider

case class Position(lon: Double, lat: Double) {
  def this(coords: Array[Double]) = this(coords(0), coords(1))
}

object Position