import scala.concurrent.ExecutionContext
import play.api._
import play.api.libs.json._
import reactivemongo.api._
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection

import models._

import play.api.Play.current
import se.digiplant.scalr.ScalrResAssets

object Global extends GlobalSettings with InitModels {

  override def onStart(app: Application) {
    initDB()
  }
}

trait InitModels {

  import ExecutionContext.Implicits.global

  def initDB() {
    val beachesFuture = Beach.collection.find(Json.obj()).cursor[JsObject].headOption
    beachesFuture.map { beach =>
      if (!beach.isDefined) {
        insertModels()
      }
    }
  }

  def insertModels() {
    // Create three beaches
    (1 to 3).map { i =>
      println(s"Saving beach $i")

      Beach.collection.insert(Beach(
        name = s"Beach $i",
        image = Image.fromPath(s"mockimages/beach$i.jpg", "default")
      ))
    }
  }
}
