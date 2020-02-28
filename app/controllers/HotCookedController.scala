package controllers

import javax.inject._
import model.HotCookedMeal
import play.api.mvc._
import service.AggregationService

@Singleton
class HotCookedController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def report(awCode: String, date: String) = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.report(awCode, date, HotCookedMeal))
  }

  def dashboard(sCode: String, date: String) = Action { implicit request: Request[AnyContent] =>
    Ok(AggregationService.dashboard(sCode, date, HotCookedMeal))
  }

}
