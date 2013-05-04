package controllers

import play.api._
import libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import models._
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._
import extensions.ReactiveMongoExtensions._
import concurrent.Future

object Application extends Controller with MongoController {
  
  def index = Action {
    Async {
      val beachesFuture = Beach.collection.find(Json.obj()).cursor[Beach].toList
      beachesFuture.map { beaches =>
        Ok(views.html.index(beaches, beaches.head))
      }
    }
  }

  def view(id: String) = Action {
  	Async {
  		val beachFuture = Beach.collection.find(Json.obj("_id" -> id.toObjectID)).cursor[Beach].headOption
      beachFuture.map { beachOpt =>
        beachOpt.map { beach =>
          Ok(views.html.view(beach))
        } getOrElse {
          NotFound(s"Beach with name: $id not found")
        }
      }
  	}
  }

  def images() = Action {
    Async {
      val imagesFuture = Image.collection.find(Json.obj()).cursor[Image].toList
      imagesFuture.map { images =>
        Ok(views.html.images(images))
      }
    }
  }

  /**
   * Upload for multiple files.
   * You could rewrite this to only support one image easily by not iterating over request.body.files but use request.body.file("file")
   * @return
   */
  def upload = Action(parse.multipartFormData) { request =>

    val supportedMimeTypes = Seq("image/jpeg", "image/png", "image/gif")

    if (request.body.files.filter(f => !supportedMimeTypes.contains(f.contentType.getOrElse(""))).length > 0) {
      BadRequest("Supported file types are: " + supportedMimeTypes.mkString(", ") + ", the uploaded files had: " + request.body.files.map(_.contentType.getOrElse("unknown")).mkString(", "))
    } else {
      val uploadAsync = Future {
        request.body.files.map { file =>
          val image = Image.fromFilePart(file, "profileimage")
          Image.collection.save(image)
          image
        }
      }

      Async {
        uploadAsync.map { images =>
          if (images.length == 0)
            BadRequest("No Images uploaded")

          Redirect(routes.Application.images)
        }
      }
    }
  }
  
}
