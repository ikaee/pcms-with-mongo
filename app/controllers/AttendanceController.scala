package controllers

import javax.inject._
import model.Attendance
import play.api.mvc._
import service.AggregationService

@Singleton
class AttendanceController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def addLogRecord() = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.addLogRecord(request.body.asJson.get.toString(), Attendance))
  }

  def report(awCode: String, date: String) = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.report(awCode, date, Attendance))
  }

  def dashboard(sCode: String, date: String) = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.dashboard(sCode, date, Attendance))
  }

}
