package model

import com.google.gson.Gson

case class DashboardEntity(
                            doctype: String,
                            code: String,
                            percentage: String,
                            presentcount: String,
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
                            currentdate: String
                          )

object DashboardEntity {

  def defaultStringFor  = (activity: Activity) => (date: String) =>  (code: String) => (totalCount: String) =>
    new Gson().toJson(defaultFor(activity)(date)(code)(totalCount))

  def defaultFor = (activity: Activity) => (date: String) =>  (code: String) => (totalCount: String) =>
    DashboardEntity(
      doctype = activity.dashboardKey,
      code = code,
      percentage = "0",
      presentcount = "0",
      totalcount = totalCount,
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
      currentdate = date
    )

  def fromJson(json: String): DashboardEntity = new Gson().fromJson(json, classOf[DashboardEntity])

}