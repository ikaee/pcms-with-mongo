package model

trait Activity {
  val reportingKey: String
  val dashboardKey: String
}

case object Attendance extends Activity {
  override val reportingKey: String = "attendance"
  override val dashboardKey: String = "attendance-dashboard"
}

case object HotCookedMeal extends Activity {
  override val reportingKey: String = "hot-cooked"
  override val dashboardKey: String = "hot-cooked-dashboard"
}

case object TakeHomeRation extends Activity {
  override val reportingKey: String = "thr"
  override val dashboardKey: String = "take-home-ration-dashboard"
}

case object SewikaAttendance extends Activity {
  override val reportingKey: String = "master-login"
  override val dashboardKey: String = "sewika-attendance-dashboard"
}

case object GrowthMonitoring extends Activity {
  override val reportingKey: String = "log"
  override val dashboardKey: String = "dashboard"
}