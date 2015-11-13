package model

import akka.actor.ActorRef
import domain._
import play.api.Play.current
import play.api.libs.json.JsString
import play.api.libs.ws._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

trait Updates {
  def instagram(mediaUpdate: Seq[UpdateResponse], clientID: String, actors: List[ActorRef]): Unit
}
object UpdatesImpl extends Updates {
  var counter:Int = 0
  var startTime:Long = System.currentTimeMillis
  var currentTime:Long = 0

  def instagram(mediaUpdate: Seq[UpdateResponse], clientID: String, actors: List[ActorRef]): Unit = {

    val noOfPosts = mediaUpdate.length

    def getPost(mediaUpdate: Seq[UpdateResponse], clientID: String, index: Int, actors: List[ActorRef]): Unit = {

      if(index > 0) {
        getMedia(mediaUpdate.head.objectId, clientID, index, actors)
        getPost(mediaUpdate, clientID, index - 1, actors)
      }
    }
    getPost(mediaUpdate, clientID, noOfPosts, actors)
  }

  private def getMedia(objectID: String, clientID: String, index: Int, actors: List[ActorRef]): Unit = {
      val url = s"https://api.instagram.com/v1/tags/netaporter/media/recent?client_id=$clientID"

      val response = for {
        response <- WS.url(url).get()
      } yield {
          (response.json \\ "data").flatMap(_ \\ "images").flatMap(_ \\ "standard_resolution").flatMap(_ \\ "url")
        }

      response.onComplete {
        case Success(images) =>
          images.take(index) foreach {
            case JsString(url) =>
              actors foreach { actor =>
                actor ! url
              }
            case _ => println("NO")
          }

        case _ => println("NO SUBSCRIPTION MADE")
      }

  }

}