package controllers

import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import model._
import play.api.db._
import domain._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.concurrent.Future
import play.api.libs.ws.WSClient
import play.api.libs.functional.syntax._
import scala.concurrent.duration.Duration
import java.io.File
import javax.inject.Inject
import akka.actor.{Actor, ActorRef, PoisonPill, Props, ActorSystem}
import play.api.libs.concurrent.Promise
import play.api.libs.iteratee.{Concurrent, Enumerator, Iteratee}

class Application @Inject()(ws: WSClient, system: ActorSystem) extends Controller {

  var actors:List[ActorRef] = Nil

  val clientID     = "d75a625dcf704a33af9f099dcf94cb3f"
  val clientSecret = "afcb2989bd914e6d8a90715754db7eae"
  val grantType    = "authorization_code"
  val redirectURI  = "http://allsocialdashboard.herokuapp.com/instagramauth"

  val authURL      = s"https://api.instagram.com/oauth/authorize/?client_id=$clientID&redirect_uri=$redirectURI&response_type=code"

  def index = Action {
    Redirect(authURL)
  }

  def test = Action {
    Ok(views.html.index("HEY"))
  }

  def instagramAuthentication(code: String) = Action.async { request =>
    for {
      r <- InstagramImpl.instagramAuth(clientID, clientSecret, grantType, redirectURI, code)
    } yield Ok(views.html.index(r))
  }

  def instagramUpdates() = Action(parse.json) { request =>
    val mediaUpdate: Seq[UpdateResponse] = request.body.as[Seq[UpdateResponse]]
    Updates.instagram(mediaUpdate, clientID, actors)
    Ok("")

  }

  object EchoWebSocketActor {
    def props(out: ActorRef) = Props(new EchoWebSocketActor(out))
  }

  class EchoWebSocketActor(out: ActorRef) extends Actor {
    def receive = {
      case msg: String =>
        Logger.info(s"actor, received message: $msg")
        if (msg == "goodbye") self ! PoisonPill
        else out ! ("I received your message: " + msg)
    }
  }

  def wsWithActor = WebSocket.acceptWithActor[String, String] {
    request =>
      out => {
        actors = actors :+ out
        Logger.info("wsWithActor, client connected")
        EchoWebSocketActor.props(out)
      }
  }
}
