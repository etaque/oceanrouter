package fr.skiffr.oceanrider

import org.supercsv.io.{CsvMapReader, CsvListReader}
import org.supercsv.prefs.CsvPreference
import java.io.FileReader
import scala.collection.JavaConversions._
import annotation.tailrec

class Polar(csvPath: String) {
  type Matrix = Seq[Seq[String]]

  val csv = new CsvListReader(new FileReader(csvPath), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)
  val matrix = readLines(csv, Seq[Seq[String]]())

  val windSpeeds: Seq[Int] = matrix.head.tail.map(_.toInt)
  val windAngles: Seq[Int] = matrix.tail.map(_.head).map(_.toInt)
  val speeds: Seq[Seq[Double]] = matrix.tail.map(_.tail.map(_.toDouble))

  csv.close()

  @tailrec
  private def readLines(csv: CsvListReader, acc: Matrix): Matrix = {
    val line = Option(csv.read())
    if (line.isDefined) readLines(csv, acc :+ line.get.toSeq)
    else acc
  }

  def windSpeedIndex(speed: Double): Int = windSpeeds.indexWhere(s => s > speed) - 1

  def windAngleIndex(angle: Double): Int = windAngles.indexWhere(a => a > angle) - 1

  def speedFor(windAngle: Double, windSpeed: Double): Double =
    speeds(windAngleIndex(windAngle))(windSpeedIndex(windSpeed))
}
