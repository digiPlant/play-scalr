package se.digiplant.scalr.api

import play.api._
import java.io.{OutputStream, File}
import org.imgscalr.Scalr
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import util.Random
import se.digiplant.resource.api.Resource

class ScalrPlugin(implicit app: Application) extends Plugin {

  lazy val configuration = app.configuration.getConfig("scalr").getOrElse(Configuration.empty)

  override def enabled = !configuration.subKeys.isEmpty

  def get(file: String, source: String = "default", width: Int, height: Int, method: Scalr.Method = Scalr.Method.ULTRA_QUALITY, mode: Scalr.Mode = Scalr.Mode.AUTOMATIC): Option[File] = {

    Resource.get(file, source).flatMap { res =>

      val cacheSource = configuration.getString("cache").getOrElse("scalrcache")

      val cachedImage = Resource.get(
        fileuid = res.getName,
        meta = Seq(width.toString, height.toString, mode.toString),
        source = cacheSource
      )

      cachedImage.map { cachedImage =>
        Some(cachedImage)
      }.getOrElse {
        val resizedImage = resize(res, width, height, method, mode)
        val maybeFileUID = Resource.put(
          resizedImage,
          cacheSource,
          filename = res.getName,
          meta = Seq(width.toString, height.toString, mode.toString)
        )
        maybeFileUID.flatMap(uid => Resource.get(uid, cacheSource))
      }
    }
  }

  def resize(file: File, width: Int, height: Int, method: Scalr.Method = Scalr.Method.ULTRA_QUALITY, mode: Scalr.Mode = Scalr.Mode.AUTOMATIC): File = {
    val image = ImageIO.read(file)
    val resized = Scalr.resize(image, method, mode, width, height, Scalr.OP_ANTIALIAS)
    val ext = if (image.getType == BufferedImage.TYPE_INT_RGB) "jpg" else "png"
    val tmp = File.createTempFile(Random.nextString(20), ext)
    ImageIO.write(resized, ext.toUpperCase, tmp)
    tmp
  }

  def resizeToStream(file: File, width: Int, height: Int, method: Scalr.Method = Scalr.Method.ULTRA_QUALITY, mode: Scalr.Mode = Scalr.Mode.AUTOMATIC): OutputStream = {
    val image = ImageIO.read(file)
    val resized = Scalr.resize(image, method, mode, width, height, Scalr.OP_ANTIALIAS)
    val ext = if (image.getType == BufferedImage.TYPE_INT_RGB) "jpg" else "png"
    val out: OutputStream = null
    ImageIO.write(resized, ext.toUpperCase, out)
    out
  }
}
