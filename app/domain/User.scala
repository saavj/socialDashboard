package domain

import play.api.libs.json.Json

case class User(id: String, username: String, full_name: String, profile_picture: String)

object User {
	implicit val format = Json.format[User]
}