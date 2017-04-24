package project.examples.util

/**
  * Created by Cubean Liu on 27/3/17.
  */

import java.time.format.DateTimeFormatter
import java.time.{Duration, LocalDateTime, ZoneId}
//import java.sql.Timestamp

object ProjectDateTime {

  val timeZone = "Australia/Sydney"

  def nowStr: String = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    val dt = LocalDateTime.now(ZoneId.of(timeZone))
    dt.format(formatter)
  }

  def now: LocalDateTime = {
    LocalDateTime.now(ZoneId.of(timeZone))
  }

  def timeSpan(startTime: LocalDateTime, endTime: LocalDateTime): String = {
    val span = Duration.between(startTime, endTime)
    s"${span.toHours}:${span.toMinutes}:${span.toMillis / 1000}:${span.toMillis % 1000}"
  }

  //  def toLocalDT(timestamp: Timestamp): LocalDateTime =
  //    timestamp.toLocalDateTime
  //
  //
  //  def toUtilDate(timestamp: Timestamp): java.util.Date = {
  //    new java.util.Date(timestamp.getTime)
  //  }
  //
  //
  //  def toTimestamp(date: java.util.Date): Timestamp = {
  //    new Timestamp(date.getTime)
  //  }

}