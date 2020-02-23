package controllers

import javax.inject._
import model.SewikaAttendance
import play.api.mvc._
import service.AggregationService

@Singleton
class SewikaAttendanceController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def addLogRecord() = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.addLogRecord(request.body.asText.get, SewikaAttendance))
  }

  def report(awCode: String, date: String) = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.report(awCode, date, SewikaAttendance))
  }

}
