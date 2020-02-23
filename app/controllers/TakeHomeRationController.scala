package controllers

import javax.inject._
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.{Document, MongoClient}
import play.api.mvc._
import subscriber.{collectionTyrion, databaseTheWall, mongoDbHost}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Singleton
class TakeHomeRationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def report(awCode: String) = Action { implicit request: Request[AnyContent] =>
    case class GMR(seq: Seq[String])
    val results =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", "thr"), equal("aanganwadicode", awCode)))
          .toFuture(),
        Duration.Inf
      ).map(_.toJson())
    Ok("[" + results.mkString(", ") + "]")
  }

  def dashboard(sCode: String) = Action { implicit request: Request[AnyContent] =>
    val results: Document =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", "take-home-ration-dashboard"), equal("code", sCode)))
          .first()
          .toFuture(),
        Duration.Inf
      )
    Ok(results.toJson())
  }

}
