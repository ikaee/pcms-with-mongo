package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import subscriber.MongoDBTrials
import subscriber.epgmsubscriber.MongoDB

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("Lay Bhaari")
  }

}
