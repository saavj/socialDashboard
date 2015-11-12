package domain

import play.api.libs.json.Json
import org.specs2.mutable._

class InstagramAuthSpec extends Specification {

	"InstagramAuth" >> {
		"parse json into case class" >> {
			val json = Json.obj(
									"accessToken" -> "test123",
									"user" 				-> Json.obj(
													"id" 			  			-> "testid",
													"username"  			-> "testusername",
													"full_name" 		  -> "testfullname",
													"profile_picture" -> "testprofilepicture"
													)
									)
			val caseClass = json.as[InstagramAuth]

			json mustEqual Json.toJson(caseClass)
		} 
	}
}