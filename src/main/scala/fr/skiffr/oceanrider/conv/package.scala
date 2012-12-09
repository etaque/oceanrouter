package fr.skiffr.oceanrider

package object conv {
  def knotToMps(knot: Double): Double = knot * 1.852 * 1000 / 3600

  def ensureDegrees(d: Double) = d % 360
  def degreesToAzimuth(d: Double) = if (d > 180) d - 360 else d
  def azimuthToDegrees(a: Double) = if (a < 0) a + 360 else a
}
