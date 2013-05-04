package models

import reactivemongo.api._
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json.Json

import play.api.Play.current
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

case class Beach(
	name: String,
	image: Image,
  _id: BSONObjectID = BSONObjectID.generate
) {
  val id = _id.stringify
}

object Beach {
  def collection = ReactiveMongoPlugin.db.collection[JSONCollection]("beaches")

  // Json Format
  implicit val format = Json.format[Beach]
}
