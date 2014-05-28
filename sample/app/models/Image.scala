package models

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import play.api.libs.Files
import play.api.libs.json.Json
import play.api.Play
import se.digiplant.res.api.Res
import org.apache.commons.io.FileUtils

import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection

case class Image(
  id: String,
  source: String,
  width: Int = 0,
  height: Int = 0,
  mime: Option[String] = None
) {

  def getImageSizeFromResource: Image = {
    val f = Res.get(id, source).getOrElse(throw new Exception(s"Resource with id: $id not found."))
    var bufferedImage: BufferedImage = null
    try {
      bufferedImage = ImageIO.read(f)
      return this.copy(width = bufferedImage.getWidth, height = bufferedImage.getHeight)
    } catch {
      case _: Exception => this
    } finally {
      bufferedImage = null
    }
  }
}

object Image {
  // Json Format
  implicit val format = Json.format[Image]

  // Collection for image (Usually you wouldn't save the images directly to it's own collection...)
  def collection = ReactiveMongoPlugin.db.collection[JSONCollection]("profileimages")

  def fromFilePart(part: play.api.mvc.MultipartFormData.FilePart[Files.TemporaryFile], source: String) = {
    val imageTypes = List("image/jpeg", "image/png", "image/gif")
    assert(imageTypes.contains(part.contentType.getOrElse("")), "File must be of the following type: " + imageTypes.mkString(", "))

    new Image(
      id = Res.put(part, source, Seq.empty),
      source = source,
      mime = part.contentType
    ).getImageSizeFromResource
  }

  def fromPath(path: String, source: String) = {
    val maybeFile = Play.current.getExistingFile(path)
    maybeFile.map { file =>

      // We need to make a copy of the file, because Res.put moves the file.
      val tmpFile = Play.getFile(s".tmp/$file.getName")
      FileUtils.copyFile(file, tmpFile)

      new Image(
        id = Res.put(tmpFile, source),
        source = source,
        mime = Some(detectMimeType(file))
      ).getImageSizeFromResource
    } getOrElse {
      throw new IllegalArgumentException(s"File must exist: $path")
    }
  }

  private def detectMimeType(f: File): String = {
    val nme = f.getName
    val dot = nme.lastIndexOf('.')
    var (base, ext) = if(dot < 0) (nme, "") else (nme.substring(0, dot), nme.substring(dot+1))
    ext match {
      case "jpg" =>
        "image/jpeg"
      case "jpeg" =>
        "image/jpeg"
      case "png" =>
        "image/png"
      case "gif" =>
        "image/gif"
      case _ => ""
    }
  }
}
