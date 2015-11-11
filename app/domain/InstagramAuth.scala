package domain

import play.api.libs.json.Json

case class InstagramAuth(accessToken: String, user: User)

object InstagramAuth {
  implicit val format = Json.format[InstagramAuth]
}