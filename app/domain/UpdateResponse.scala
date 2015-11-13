package domain

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}

case class UpdateResponse(subscription_id: String, updateObject: String, objectId: String, changedAspect: String, time: Int)
object UpdateResponse {
  implicit val updateFormat: Format[UpdateResponse] = (
    (JsPath \ "subscription_id").format[String] and
    (JsPath \ "object").format[String] and
    (JsPath \ "object_id").format[String] and
    (JsPath \ "changed_aspect").format[String] and
    (JsPath \ "time").format[Int]
   )(UpdateResponse.apply, unlift(UpdateResponse.unapply))
}