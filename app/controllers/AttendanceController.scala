package controllers

import com.google.gson.Gson
import javax.inject._
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.{Document, MongoClient}
import play.api.mvc._
import subscriber.{collectionTyrion, databaseTheWall, mongoDbHost}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Singleton
class AttendanceController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def addLogRecord() = Action { implicit request: Request[AnyContent] =>
    val results =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .insertOne(BsonDocument(new Gson().toJson(request.body.asText.get)))
          .toFuture(),
        Duration.Inf
      )
    Ok(results.toString())
  }

  def report(awCode: String) = Action { implicit request: Request[AnyContent] =>
    case class GMR(seq: Seq[String])
    val results =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", "attendance"), equal("aanganwadicode", awCode)))
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
          .find(and(equal("doctype", "attendance-dashboard"), equal("code", sCode)))
          .first()
          .toFuture(),
        Duration.Inf
      )
    Ok(results.toJson())
  }

}
