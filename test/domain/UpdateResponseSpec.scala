package domain

import org.specs2.mutable.Specification
import play.api.libs.json.Json

/**
 * Created by s.cole on 13/11/2015.
 */
class UpdateResponseSpec extends Specification {

  "UpdateResponse" >> {
    "help parse instagram json into case class" >> {

      val json = Json.parse(
        s"""
           |
           |[
           |{
           |"changed_aspect":"media",
           |"object":"tag",
           |"object_id":"nofilter",
           |"time":1447446332,
           |"subscription_id":20795686,
           |"data":{}
           |}
           |]
           |
         """.stripMargin)

      val caseClass = json.as[UpdateResponse]

      json                       mustEqual Json.toJson(caseClass)

      caseClass.subscription_id  mustEqual "1"
      caseClass.updateObject     mustEqual "user"
      caseClass.objectId         mustEqual "1234"
      caseClass.changedAspect    mustEqual "media"
      caseClass.time             mustEqual 1297286541

    }
  }

}
