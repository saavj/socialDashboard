package domain

import play.api.libs.json.{Json, Format, JsPath}
import play.api.libs.functional.syntax._
import play.api.libs.concurrent.Execution.Implicits._

case class UpdateResponse(subscription_id: String, updateObject: String, objectId: String, changed_aspect: String, time: Int)
object UpdateResponse {
  implicit val updateFormat: Format[UpdateResponse] = (
    (JsPath \ "subscription_id").format[String] and
    (JsPath \ "object").format[String] and
    (JsPath \ "object_id").format[String] and
    (JsPath \ "changed_aspect").format[String] and
    (JsPath \ "time").format[Int]
   )(UpdateResponse.apply, unlift(UpdateResponse.unapply))
}