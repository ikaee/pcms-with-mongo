package controllers

import java.util.regex.Pattern

import javax.inject._
import org.mongodb.scala.{Document, MongoClient}
import org.mongodb.scala.model.Filters.{and, equal}
import play.api.mvc._
import subscriber.epgmsubscriber.{MongoDB, Record, SchemaFactory}
import subscriber.{collectionTyrion, databaseTheWall, mongoDbHost}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class GrowthMonitoringController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def addLogRecord() = Action { implicit request: Request[AnyContent] =>
    Ok(handleDelivery(request.body.asText.get))
  }

  def report(awCode: String) = Action { implicit request: Request[AnyContent] =>
    case class GMR(seq: Seq[String])
    val results =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", "log"), equal("aanganwadicode", awCode)))
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
          .find(and(equal("doctype", "dashboard"), equal("code", sCode)))
          .first()
          .toFuture(),
        Duration.Inf
      )
    Ok(results.toJson())
  }

  def handleDelivery(body: String): String = {
    val rawData = removeEscapeCharacters(body)
    SchemaFactory.type1.apply(rawData) match {
      case Some(log) => insertAndAggregate(log)
      case _ => "Raw FTP data schema validation failed:  "
    }
  }

  def removeEscapeCharacters(rawData: String): String = {
    val m = Pattern.compile("[0-9]+").matcher(rawData)
    if(m.find()) m.group() else rawData

  }

  def insertAndAggregate(record: Record): String = {
    try {
      MongoDB.insertRecordInDatabase(record)
      "Success"
    } catch {
      case e: Exception => {
        println("Error occured while inserting into database "+ e)
        e.printStackTrace()
        "Failure"
      }
    }
  }

}
