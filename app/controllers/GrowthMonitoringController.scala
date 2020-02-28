package controllers

import java.util.regex.Pattern

import javax.inject._
import model.{DashboardEntity, GrowthMonitoring, GrowthMonitoringDashboardEntity, GrowthMonitoringLogEntity, GrowthMonitoringUILogEntity}
import org.mongodb.scala.{Document, MongoClient}
import org.mongodb.scala.model.Filters.{and, equal}
import play.api.mvc._
import subscriber.epgmsubscriber.{MongoDB, Record, SchemaFactory}
import subscriber.{collectionTyrion, databaseTheWall, mongoDbHost}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

@Singleton
class GrowthMonitoringController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def addLogRecord() = Action { implicit request: Request[AnyContent] =>
    Ok(handleDelivery(request.body.asText.get))
  }

  def report(awCode: String, date: String) = Action { implicit request: Request[AnyContent] =>
    case class GMR(seq: Seq[String])
    val currentMonth = date.split("-")(1)
    val currentYear = date.split("-")(2).takeRight(2)
    val results =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", "log"), equal("aanganwadicode", awCode), equal("month", currentMonth), equal("year", currentYear)))
          .toFuture(),
        Duration.Inf
      ).map(x => GrowthMonitoringUILogEntity.from(GrowthMonitoringLogEntity.fromJson(x.toJson())).toJson())
    Ok("[" + results.mkString(", ") + "]")
  }

  def dashboard(sCode: String, date: String) = Action { implicit request: Request[AnyContent] =>
    Try{val currentMonth = date.split("-")(1)
    val currentYear = date.split("-")(2).takeRight(2)
    val results: Document =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", "dashboard"), equal("code", sCode), equal("currentmonth", currentMonth), equal("currentyear", currentYear)))
          .first()
          .toFuture(),
        Duration.Inf
      )
    Ok(results.toJson())}
      .fold(_ => Ok(GrowthMonitoringDashboardEntity.defaultStringFor(date)(sCode)), x => x)
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
