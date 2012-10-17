package se.digiplant.scalr

import play.api._
import play.api.mvc._
import play.api.libs._
import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}
import org.joda.time.DateTimeZone
import collection.JavaConverters._
import java.io.File

import play.api.Play.current

object ScalrAssets extends Controller {

  private val timeZoneCode = "GMT"

  //Dateformatter is immutable and threadsafe
  private val df: DateTimeFormatter = DateTimeFormat
    .forPattern("EEE, dd MMM yyyy HH:mm:ss '" + timeZoneCode + "'")
    .withLocale(java.util.Locale.ENGLISH)
    .withZone(DateTimeZone.forID(timeZoneCode))

  //Dateformatter is immutable and threadsafe
  private val dfp: DateTimeFormatter = DateTimeFormat
    .forPattern("EEE, dd MMM yyyy HH:mm:ss")
    .withLocale(java.util.Locale.ENGLISH)
    .withZone(DateTimeZone.forID(timeZoneCode))

  private val parsableTimezoneCode = " " + timeZoneCode

  /**
   * Resize and cache images stored in play-res
   * @param path Path relative to play app
   * @param file Filepath of the file
   * @param width Width of resized image
   * @param height Height of resized image
   * @param mode AUTOMATIC, FIT_EXACT, FIT_TO_WIDTH, FIT_TO_HEIGHT
   * @return A resized image
   */
  def at(path: String, file: String, width: Int, height: Int = 0, mode: String = "automatic"): Action[AnyContent] = Action { request =>

    val modeEnum: org.imgscalr.Scalr.Mode = org.imgscalr.Scalr.Mode.valueOf(mode.toUpperCase)

    def parseDate(date: String): Option[java.util.Date] = {
      try {
        //jodatime does not parse timezones, so we handle that manually
        val d = dfp.parseDateTime(date.replace(parsableTimezoneCode, "")).toDate
        Some(d)
      } catch {
        case _: Exception => None
      }
    }

    api.Scalr.get(path, file, width, height, modeEnum, org.imgscalr.Scalr.Method.ULTRA_QUALITY).map { resizedImage =>
      request.headers.get(IF_NONE_MATCH).flatMap {
        ifNoneMatch => etagFor(resizedImage).filter(_ == ifNoneMatch)
      }.map(_ => NotModified).getOrElse {
        request.headers.get(IF_MODIFIED_SINCE).flatMap(parseDate).flatMap {
          ifModifiedSince => lastModifiedFor(resizedImage).flatMap(parseDate).filterNot(lastModified => lastModified.after(ifModifiedSince))
        }.map(_ => NotModified.withHeaders(
          DATE -> df.print({ new java.util.Date }.getTime)
        )).getOrElse {

          val response = Ok.sendFile(resizedImage, inline = true)

          // Add Etag if we are able to compute it
          val taggedResponse = etagFor(resizedImage).map(etag => response.withHeaders(ETAG -> etag)).getOrElse(response)
          val lastModifiedResponse = lastModifiedFor(resizedImage).map(lastModified => taggedResponse.withHeaders(LAST_MODIFIED -> lastModified)).getOrElse(taggedResponse)

          // Add Cache directive if configured

          /*val cachedResponse = lastModifiedResponse.withHeaders(CACHE_CONTROL -> {
            Play.mode match {
              case Mode.Prod => Play.configuration.getString("assets.defaultCache").getOrElse("max-age=3600")
              case _ => "no-cache"
            })
          })*/

          lastModifiedResponse

        }: Result
      }
    }.getOrElse {
      NotFound
    }
  }

  // Last modified
  private val lastModifieds = (new java.util.concurrent.ConcurrentHashMap[String, String]()).asScala

  private def lastModifiedFor(file: File): Option[String] = {
    lastModifieds.get(file.getName).filter(_ => Play.isProd).orElse {
      val lastModified = df.print({
        new java.util.Date(file.lastModified).getTime
      })
      lastModifieds.put(file.getName, lastModified)
      Some(lastModified)
    }
  }

  // Etags
  private val etags = (new java.util.concurrent.ConcurrentHashMap[String, String]()).asScala

  private def etagFor(file: File): Option[String] = {
    etags.get(file.getName).filter(_ => Play.isProd).orElse {
      val maybeEtag = lastModifiedFor(file).map(_ + " -> " + file.getName).map("\"" + Codecs.sha1(_) + "\"")
      maybeEtag.foreach(etags.put(file.getName, _))
      maybeEtag
    }
  }

}