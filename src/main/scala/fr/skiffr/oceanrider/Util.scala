package fr.skiffr.oceanrider

object Util {
  def knotToMps(knot: Double): Double = knot * 1.852 * 1000 / 3600

}
