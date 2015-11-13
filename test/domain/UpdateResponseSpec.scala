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
           |"subscription_id": "1",
           |"object": "user",
           |"object_id": "1234",
           |"changed_aspect": "media",
           |"time": 1297286541
           |},
           |{
           |"subscription_id": "2",
           |"object": "tag",
           |"object_id": "nofilter",
           |"changed_aspect": "media",
           |"time": 1297286541
           |}
           |]
           |
         """.stripMargin)

      val caseClass = json.as[Seq[UpdateResponse]]
      val topSub = caseClass.head
      val botSub = caseClass.last

      json                    mustEqual Json.toJson(caseClass)

      topSub.subscription_id  mustEqual "1"
      topSub.updateObject     mustEqual "user"
      topSub.objectId         mustEqual "1234"
      topSub.changedAspect    mustEqual "media"
      topSub.time             mustEqual 1297286541

      botSub.subscription_id  mustEqual "2"
      botSub.updateObject     mustEqual "tag"
      botSub.objectId         mustEqual "nofilter"
      botSub.changedAspect    mustEqual "media"
      botSub.time             mustEqual 1297286541
    }
  }

}
