package oceanrouter.core

case class Wind(u: Double, v: Double) {
  lazy val direction = (u, v) match {
    case (0, _) => if (v < 0) 360 else 180
    case _ =>
      (if (u < 0) 270 else 90) - 57.29578 * math.atan(v / u)
  }
  lazy val speed = math.sqrt(math.pow(u, 2) + math.pow(v, 2))

  def angleTo(otherDirection: Double) = {
    val delta = otherDirection - direction
    math.abs(
      if (delta > 180) delta - 360
      else if (delta <= -180) delta + 360
      else delta
    )
  }
}

object Wind