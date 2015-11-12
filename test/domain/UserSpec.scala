package domain

import play.api.libs.json.Json
import org.specs2.mutable._

class UserSpec extends Specification {

	"UserSpec" >> {
		"parse json into case class" >> {
			val json = Json.obj(
									"id" 			  			-> "testid",
									"username"  			-> "testusername",
									"full_name" 		  -> "testfullname",
									"profile_picture" -> "testprofilepicture"
									)
							
			val caseClass = json.as[User]

			json mustEqual Json.toJson(caseClass)
		}
	}

}