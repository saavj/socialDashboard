package model

import akka.actor.ActorRef
import domain._
import play.api.libs.json.{JsString, JsValue}
import play.api.libs.ws.WS

import scala.concurrent.Future
import scala.util.Success

object Updates {
  var counter:Int = 0
  var startTime:Long = System.currentTimeMillis
  var currentTime:Long = 0

  def instagram(mediaUpdate: Seq[UpdateResponse], clientID: String, actors: List[ActorRef]): Unit = {
    val objectID = mediaUpdate.head.objectId
    val url = s"https://api.instagram.com/v1/tags/$objectID/media/recent?client_id=$clientID"

    val response: Future[Option[JsValue]] = for {
      response <- WS.url(url).get()
    } yield {
        (response.json \\ "data").flatMap(_ \\ "images").flatMap(_ \\ "standard_resolution").flatMap(_ \\ "url").headOption
      }

    response.onComplete {
      case Success(Some(JsString(image))) => {
        println(
          s"""
             |+++++++++++++++++++++++++++++++
             |$image
              |
              |
             """.stripMargin)
        actors foreach { actor =>
          actor ! image
        }
      }
      case _ => println("NO")
    }
  }
}