package se.digiplant.resource.api

import org.specs2.specification.{Scope, Around}
import play.api._
import play.api.test._
import play.api.test.Helpers._
import java.io.File
import org.apache.commons.io.FileUtils
import util.Random
import se.digiplant.scalr.api.ScalrPlugin

trait FakeApp extends Around with Scope with FileSystemScope {

  val plugins = Seq(
    "se.digiplant.resource.api.ResourcePlugin",
    "se.digiplant.scalr.api.ScalrPlugin"
  )

  val configuration = Map(
    ("res.default" -> "tmp/default"),
    ("res.scalrcache" -> "tmp/scalrcache"),
    ("scalr.cache" -> "scalrcache")
  )

  lazy val res = app.plugin[ResourcePlugin].get

  lazy val scalr = app.plugin[ScalrPlugin].get

  implicit object app extends FakeApplication(
    additionalPlugins = plugins,
    additionalConfiguration = configuration
  )

  def around[T <% org.specs2.execute.Result](test: => T) = running(app) {
    test
  }
}

trait FileSystemScope extends Scope {
  val tmp = new File("tmp")
  val logo = new File("src/test/resources/digiPlant.jpg")

  def getTestFile(): File = {
    tmp.delete()
    tmp.mkdir()
    val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('1' to '9')
    val rand = (1 to 20).map(x => chars(Random.nextInt(chars.length))).mkString
    val tmpFile = new File("tmp", rand + ".jpg")
    FileUtils.copyFile(logo, tmpFile)
    tmpFile
  }
}

object Files extends FileSystemScope