package model

import com.google.gson.Gson

case class GrowthMonitoringDashboardEntity(
                                            doctype: String,
                                            code: String,
                                            suwcount: String,
                                            muwcount: String,
                                            normalcount: String,
                                            totalcount: String,
                                            malecount: String,
                                            femalecount: String,
                                            zerotoonecount: String,
                                            onetotwocount: String,
                                            twotothreecount: String,
                                            threetofourcount: String,
                                            fourtofivecount: String,
                                            fivetosixcount: String,
                                            januarycount: String,
                                            februarycount: String,
                                            marchcount: String,
                                            aprilcount: String,
                                            maycount: String,
                                            junecount: String,
                                            julycount: String,
                                            augustcount: String,
                                            septembercount: String,
                                            octobercount: String,
                                            novembercount: String,
                                            decembercount: String,
                                            currentmonth: String,
                                            currentyear: String
                                          )

object GrowthMonitoringDashboardEntity {

  def defaultStringFor = (date: String) => (code: String) =>
    new Gson().toJson(defaultFor(date)(code))

  def defaultFor = (date: String) => (code: String) =>
    GrowthMonitoringDashboardEntity(
      doctype = GrowthMonitoring.dashboardKey,
      code = code,
      suwcount = "0",
      muwcount = "0",
      normalcount = "0",
      totalcount = "0",
      malecount = "0",
      femalecount = "0",
      zerotoonecount = "0",
      onetotwocount = "0",
      twotothreecount = "0",
      threetofourcount = "0",
      fourtofivecount = "0",
      fivetosixcount = "0",
      januarycount = "0",
      februarycount = "0",
      marchcount = "0",
      aprilcount = "0",
      maycount = "0",
      junecount = "0",
      julycount = "0",
      augustcount = "0",
      septembercount = "0",
      octobercount = "0",
      novembercount = "0",
      decembercount = "0",
      currentmonth = date.split("-")(1),
      currentyear = date.split("-")(2).takeRight(2)
    )

}