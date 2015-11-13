package domain

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}

case class UpdateResponse(changedAspect: String, updateObject: String, objectId: String, time: Int, subscription_id: Int, data: Seq[String])
object UpdateResponse {
  implicit val updateFormat: Format[UpdateResponse] = (
    (JsPath \ "changed_aspect") .format[String] and
    (JsPath \ "object")         .format[String] and
    (JsPath \ "object_id")      .format[String] and
    (JsPath \ "time")           .format[Int]    and
    (JsPath \ "subscription_id").format[Int] and
    (JsPath \ "data")           .format[Seq[String]]
  )(UpdateResponse.apply, unlift(UpdateResponse.unapply))
}
