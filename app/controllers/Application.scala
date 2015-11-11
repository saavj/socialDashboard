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

  val clientID     = "d75a625dcf704a33af9f099dcf94cb3f"
  val clientSecret = "afcb2989bd914e6d8a90715754db7eae"
  val grantType    = "authorization_code"
  val redirectURI  = "http://allsocialdashboard.herokuapp.com/instagramauth"

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
