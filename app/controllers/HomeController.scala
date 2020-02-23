package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import subscriber.MongoDBTrials
import subscriber.epgmsubscriber.MongoDB

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    val some = MongoDBTrials.delete.toFuture()
    val document = Await.result(some, Duration.Inf)
//    val name = document.get("name").get.asString().getValue
//    val count = document.get("count").get.asInt32().getValue
//    println("document.get(\"name\")" + name)
//    println("document.get(\"count\")" + count)
    Ok("Result is " + document)
  }
}
