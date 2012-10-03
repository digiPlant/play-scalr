package se.digiplant.scalr.api

import play.api._
import java.io.File

object Scalr {

  private def scalrAPI(implicit app: Application): ScalrPlugin = {
    app.plugin[ScalrPlugin] match {
      case Some(plugin) => plugin
      case None => sys.error("The Scalr Plugin is not registered in conf/play.plugins")
    }
  }

  /**
   * Creates and caches image in local cache directory
   * @param path The path to where the images are stored
   * @param file The filePath relative to the path variable (the same as the play Assets Controller)
   * @param width The width of the frame that we want the image to fit within
   * @param height The height of the frame that we want the image to fit within
   * @param mode Any of the Scale modes such as AUTOMATIC, FIT_TO_WIDTH, FIT_TO_HEIGHT
   * @param method Any of the Scalr Methods, The standard is the highest possible
   * @return a File if everything when well
   */
  def get(path: String, file: String, width: Int, height: Int, mode: org.imgscalr.Scalr.Mode = org.imgscalr.Scalr.Mode.AUTOMATIC, method: org.imgscalr.Scalr.Method = org.imgscalr.Scalr.Method.ULTRA_QUALITY)(implicit app: Application): Option[File] = {
    scalrAPI.get(path, file, width, height, mode, method)
  }

  /**
   *
   * @param fileuid The unique play-res file identifier
   * @param source The play-res source name
   * @param width The width of the frame that we want the image to fit within
   * @param height The height of the frame that we want the image to fit within
   * @param mode Any of the Scale modes such as AUTOMATIC, FIT_TO_WIDTH, FIT_TO_HEIGHT
   * @param method Any of the Scalr Methods, The standard is the highest possible
   * @return a File if everything when well
   */
  def getRes(fileuid: String, source: String = "default", width: Int, height: Int, mode: org.imgscalr.Scalr.Mode = org.imgscalr.Scalr.Mode.AUTOMATIC, method: org.imgscalr.Scalr.Method = org.imgscalr.Scalr.Method.ULTRA_QUALITY)(implicit app: Application): Option[File] = {
    scalrAPI.getRes(fileuid, source, width, height, mode, method)
  }
}
