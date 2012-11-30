package fr.skiffr.oceanrider

import net.sourceforge.jgrib._
import scalaz.Scalaz._
import scalaz.geo._
import Geo._
import java.util.Calendar
import org.joda.time.DateTime

//case class Wind(u: Float, v: Float)

class GribReader(grib: GribFile) {

  type SortedRecords = List[(Calendar, GribRecord)]

  val uRecords: SortedRecords = sort(recordsForDim("ugrd"))
  val vRecords: SortedRecords = sort(recordsForDim("vgrd"))

  def recordsForDim(dim: String): List[GribRecord] = {
    for {
      i <- (1 to grib.getRecordCount).toList
      record = grib.getRecord(i)
      if record.getPDS.getType == dim
    } yield record
  }

  def sort(records: List[GribRecord]): SortedRecords = {
    records.map(r => (r.getPDS.getGMTForecastTime, r)).sortBy(_._1)
  }

  def recordAt(records: SortedRecords, at: DateTime): GribRecord = {

  }

}
