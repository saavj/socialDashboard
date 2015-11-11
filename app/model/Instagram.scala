package model

import javax.inject.Inject
import play.api.libs.json.Json
import scala.concurrent.Future
import play.api.Play.current
import domain._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.concurrent.Execution.Implicits._
import scala.util.Success

object Instagram {

  def instagramAuth(clientID: String, clientSecret: String, grantType: String, redirectURI: String, code: String) = {

    val url = "https://api.instagram.com/oauth/access_token"

    for {
      response <- WS.url(url).post(Map[String, Seq[String]](
        "client_id" -> Seq(clientID),
        "client_secret" -> Seq(clientSecret),
        "grant_type" -> Seq(grantType),
        "redirect_uri" -> Seq(redirectURI),
        "code" -> Seq(code)
      ))
    } yield {
      response.json.as[InstagramAuth]
    }

    // response.onSuccess {
    //   case auth => createSubscription(clientID, clientSecret, auth)
    // }

  }

}