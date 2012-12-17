package fr.skiffr.oceanrouter.core

import net.sourceforge.jgrib._
import java.util.Calendar
import org.joda.time.DateTime


case class UVRecords(u: GribRecord, v: GribRecord)

class WeatherContext(grib: GribFile) {

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

  def valueForCoord(r: GribRecord, c: Position): Double = {
    val i: Int = ((c.lon - r.getGDS.getGridLon1) / r.getGDS.getGridDX).toInt
    val j: Int = ((c.lat - r.getGDS.getGridLat1) / r.getGDS.getGridDY).toInt
    r.getValue(i, j)
  }

  def windAt(c: Position, at: DateTime): Wind = {
    val uv = uvRecordsAt(at.toGregorianCalendar)
    new Wind(valueForCoord(uv.u, c), valueForCoord(uv.v, c))
  }
}

object WeatherContext {
  private val gribFile = new GribFile("/Users/emilien/Downloads/zyGrib_mac-6.0.2/grib/20121127_213227_.grb")
  val current = new WeatherContext(gribFile)
}