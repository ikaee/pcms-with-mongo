package controllers

import javax.inject._
import model.{Activity, Attendance, HotCookedMeal, SewikaAttendance, TakeHomeRation}
import play.api.mvc._
import service.AggregationService

@Singleton
class AttendanceController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def addLogRecord() = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.addLogRecord(request.body.asJson.get.toString(), getActivityType(request.body.asJson.get.toString())))
  }

  def report(awCode: String, date: String) = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.report(awCode, date, Attendance))
  }

  def dashboard(sCode: String, date: String) = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.dashboard(sCode, date, Attendance))
  }

  def getActivityType(body: String): Activity = {
    if (body.contains(""""doctype":"attendance"""")) Attendance
    else if (body.contains(""""doctype":"hot-cooked"""")) HotCookedMeal
    else if (body.contains(""""doctype":"master-login"""")) SewikaAttendance
    else TakeHomeRation
  }

}
