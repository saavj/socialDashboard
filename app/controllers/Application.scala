package controllers

import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import model.Instagram
import play.api.db._
import domain._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  val clientID     = "5936a1918c2a4292ac852970f46becb2"
  val clientSecret = "e175cb7bb3db4a0ea352ab1cb50556a5"
  val grantType    = "authorization_code"
  val redirectURI  = "http://ynapdashboard.herokuapp.com/instagramauth"

  val authURL      = s"https://api.instagram.com/oauth/authorize/?client_id=$clientID&redirect_uri=$redirectURI&response_type=code"

  def index = Action {
    Redirect(authURL)
  }

  def instagramAuth(code: String) = Action.async(parse.json) { request =>
    val insta = Instagram.instagramAuth(clientID, clientSecret, grantType, redirectURI, code)
    insta.map{ actualJson => 
      Ok(Json.toJson(actualJson))
    }
  }
}
