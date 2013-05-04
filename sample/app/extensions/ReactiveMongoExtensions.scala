package extensions

import reactivemongo.api.Cursor
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.bson.BSONObjectID

object ReactiveMongoExtensions {

  implicit class ListExtensions[T](val futureList: Future[List[T]]) extends AnyVal {
    def toJsArray(implicit ec: ExecutionContext, writes: Writes[T]): Future[JsArray] = {
      futureList.map { futureList =>
        futureList.foldLeft(JsArray(List()))( (obj, item) => obj ++ Json.arr(item))
      }
    }
  }

  implicit class CursorExtensions[T](val cursor: Cursor[T]) extends AnyVal {
    def toJsArray(implicit ec: ExecutionContext, writes: Writes[T]): Future[JsArray] = {
      cursor.toList.toJsArray
    }
  }

  implicit class BSONObjectIdExtensions(val string: String) extends AnyVal {
    def toObjectID: BSONObjectID = BSONObjectID(string)
  }

}
