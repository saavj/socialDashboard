package model

import domain._
import akka.actor.{Actor, ActorRef, PoisonPill, Props, ActorSystem}
import play.api.libs.json.{JsValue, JsString, JsDefined}
import play.api.libs.ws._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success


object Updates {
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

  def getMedia(objectID: String, clientID: String, index: Int, actors: List[ActorRef]): Unit = {
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