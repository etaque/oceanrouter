package fr.skiffr.oceanrider

import org.supercsv.io.CsvListReader
import org.supercsv.prefs.CsvPreference
import java.io.FileReader
import scala.collection.JavaConversions._
import annotation.tailrec

class Polar(csvPath: String) {
  type Matrix = Seq[Seq[String]]

  val csv = new CsvListReader(new FileReader(csvPath), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)
  val matrix = readLines(csv, Seq[Seq[String]]())

  val speedParser = (s: String) => Util.knotToMps(s.toDouble)

  val windSpeeds: List[Double] = matrix.head.tail.map(speedParser).toList
  val windAngles: List[Int] = matrix.tail.map(_.head).map(_.toInt).toList
  val speeds: Seq[Seq[Double]] = matrix.tail.map(_.tail.map(speedParser))

  csv.close()

  @tailrec
  private def readLines(csv: CsvListReader, acc: Matrix): Matrix = {
    val line = Option(csv.read())
    if (line.isDefined) readLines(csv, acc :+ line.get.toSeq)
    else acc
  }

  def windSpeedIndex(speed: Double): Int =
    if (speed > windSpeeds.last) windSpeeds.size - 1
    else windSpeeds.indexWhere(s => s > speed) - 1

  def windAngleIndex(angle: Double): Int = windAngles.indexWhere(a => a > angle) - 1

  def speedFor(windAngle: Double, windSpeed: Double): Double = {
    require(windAngle >= 0 && windAngle < 360)
    require(windSpeed >= 0)
    speeds(windAngleIndex(windAngle))(windSpeedIndex(windSpeed))
  }
}

object Polar {
  val current = new Polar("/Users/emilien/Downloads/qtvlm_app-3.3.3-patch1_full/polar/boat_imoca60.csv")
}