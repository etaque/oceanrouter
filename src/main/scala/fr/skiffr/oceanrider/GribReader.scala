package fr.skiffr.oceanrider

import net.sourceforge.jgrib._
import java.util.Calendar

case class Wind(u: Double, v: Double)
case class UVRecords(u: GribRecord, v: GribRecord)
case class Coord(x: Double, y: Double)

class GribReader(grib: GribFile) {

  val recordsByDate: Map[Calendar, List[GribRecord]] = listRecords groupBy(_.getPDS.getGMTForecastTime)

  def listRecords(): List[GribRecord] = {
    for {
      i <- (1 to grib.getRecordCount).toList
      record = grib.getRecord(i)
    } yield record
  }

  def forecastAt(at: Calendar): Calendar =
    recordsByDate.keys.toList.filter(_.before(at)).sortWith((a, b) => a.after(b)).head

  def uvRecordsAt(at: Calendar): UVRecords = {
    val records = recordsByDate(forecastAt(at))
    new UVRecords(records.find(_.getPDS.getType == "ugrd").get,
      records.find(_.getPDS.getType == "vgrd").get)
  }

  def valueForCoord(r: GribRecord, c: Coord): Double = {
    val i: Int = ((c.x - r.getGDS.getGridLon1) / r.getGDS.getGridDX).toInt
    val j: Int = ((c.y - r.getGDS.getGridLat1) / r.getGDS.getGridDY).toInt
    r.getValue(i, j)
  }

  def windAt(c: Coord, at: Calendar): Wind = {
    val uv = uvRecordsAt(at)
    new Wind(valueForCoord(uv.u, c), valueForCoord(uv.v, c))
  }
}
