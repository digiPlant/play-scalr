package se.digiplant.scalr

import org.specs2.mutable.Around
import org.specs2.specification.Scope
import org.specs2.execute.{AsResult, Result}
import play.api.test._
import play.api.test.Helpers._
import java.io.File
import org.apache.commons.io.FileUtils
import util.Random

trait ScalrContext extends Around with TempFile {

  implicit val app: FakeApplication = new FakeApplication(
    additionalConfiguration = Map(
      "res.default" -> "tmp/default",
      "res.scalrcache" -> "tmp/scalrcache",
      "scalr.cache" -> "scalrcache",
      "scalr.cachedir" -> "tmp/scalrcachedir"
    )
  )

  def around[T : AsResult](t: =>T) = Helpers.running(app) {
    val result = AsResult.effectively(t)

    tmp.delete()

    result
  }
}

trait TempFile extends Scope {
  lazy val tmp = new File("tmp")
  lazy val logo = new File("test/resources/digiPlant.jpg")

  def testFile: File = {
    tmp.mkdir()
    val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('1' to '9')
    val rand = (1 to 20).map(x => chars(Random.nextInt(chars.length))).mkString
    val tmpFile = new File("tmp", rand + ".jpg")
    FileUtils.copyFile(logo, tmpFile)
    tmpFile
  }
}
