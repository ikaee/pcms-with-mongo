package controllers

import java.util.regex.Pattern

import javax.inject._
import play.api._
import play.api.mvc._
import subscriber.epgmsubscriber.{MongoDB, Record, SchemaFactory}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class GrowthMonitoringEntry @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def entry() = Action { implicit request: Request[AnyContent] =>
    Ok(handleDelivery(request.body.asText.get))
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
