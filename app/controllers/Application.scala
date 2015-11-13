package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import model._
import domain._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.ws.WSClient
import javax.inject.Inject
import akka.actor.{Actor, ActorRef, PoisonPill, Props, ActorSystem}

class Application@Inject()(ws: WSClient, system: ActorSystem) extends Controller {

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
      r <- Instagram.instagramAuth(clientID, clientSecret, grantType, redirectURI, code)
    } yield Ok(views.html.index(r))
  }

  def pubsubhubub() = Action { request =>
    request.getQueryString("hub.challenge") match {
      case Some(hubChallenge) => Ok(hubChallenge)
      case None               => BadRequest
    }
  }

  def instagramUpdates() = Action(parse.json) { request =>
    println(
      s"""
         |
         |response.body____________________
         |${request.body}
         |
       """.stripMargin)
    val mediaUpdate: Seq[UpdateResponse] = request.body.as[Seq[UpdateResponse]]
    println(
      s"""
         |
         |mediaUpdate_______________________
         |$mediaUpdate
         |
       """.stripMargin)
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
