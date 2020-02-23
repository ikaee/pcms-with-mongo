package service

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, GregorianCalendar}

import com.google.gson.Gson
import model.{Activity, DashboardEntity, LogEntity, MasterEntity}
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Filters.{and, equal}
import subscriber.{collectionTyrion, databaseTheWall, mongoDbHost}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

object AggregationService {

  def addLogRecord(body: String, activity: Activity) = {
    insertLogRecord(body)
    val logEntity = createLogEntity(body)
    val masterEntity = fetchMasterInfo(logEntity)
    val dashboardEntity = fetchDashboardEntity(logEntity.schoolcode.take(2))(activity)(logEntity.datestamp)
    deleteFromDb(activity)(dashboardEntity)
    val updateDashboardEntity = updateDashboardEntity(logEntity)(masterEntity)(dashboardEntity)
    saveToDb(updateDashboardEntity)
  }

  private val createLogEntity = (body: String) => {
    LogEntity.fromJson(body)
  }

  private val fetchMasterInfo = (ae: LogEntity) => {
    MasterEntity.fromJson(
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(
            and(
              equal("doctype", "child"),
              equal("schoolcode", ae.schoolcode),
              equal("studentcode", ae.studentcode),
              equal("datestamp", ae.datestamp),
              equal("timestamp", ae.timestamp)))
          .first()
          .toFuture(),
        Duration.Inf
      ).toJson())
  }

  private val fetchDashboardEntity = (stateCode: String) => (activity: Activity) => (date: String) => {
    Try(DashboardEntity.fromJson(
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(
            and(
              equal("doctype", activity.dashboardKey),
              equal("code", stateCode),
              equal("currentdate", date)))
          .first()
          .toFuture(),
        Duration.Inf
      ).toJson())).fold(_ => DashboardEntity.defaultFor(activity)(date), x => x)
  }

  private val updateDashboardEntity = (le: LogEntity) => (me: MasterEntity) => (de: DashboardEntity) => {
    (updatePresentCount andThen updateGenderCount(me) andThen updateAgeCount(me) andThen updateMonthCount(le)) (de)
  }

  private val updatePresentCount = (de: DashboardEntity) => {
    val updatedCount = (de.presentcount.toInt + 1).toString
    de.copy(presentcount = updatedCount, percentage = ((updatedCount.toInt / de.totalcount.toInt) * 100).toString)
  }

  private val updateGenderCount = (me: MasterEntity) => (de: DashboardEntity) => {
    if (me.sex.equals("M")) de.copy(malecount = (de.malecount.toInt + 1).toString)
    else de.copy(femalecount = (de.femalecount.toInt + 1).toString)
  }

  private val updateAgeCount = (me: MasterEntity) => (de: DashboardEntity) => {
    val ageInMonths = getAgeInMonths(s"${me.dayofbirth}-${me.monthofbirth}-${me.yearofbirth}")
    if (ageInMonths <= 12) de.copy(zerotoonecount = (de.zerotoonecount.toInt + 1).toString)
    if (ageInMonths > 12 && ageInMonths <= 24) de.copy(onetotwocount = (de.onetotwocount.toInt + 1).toString)
    if (ageInMonths > 24 && ageInMonths <= 36) de.copy(twotothreecount = (de.twotothreecount.toInt + 1).toString)
    if (ageInMonths > 36 && ageInMonths <= 48) de.copy(threetofourcount = (de.threetofourcount.toInt + 1).toString)
    if (ageInMonths > 48 && ageInMonths <= 60) de.copy(fourtofivecount = (de.fourtofivecount.toInt + 1).toString)
    else de.copy(fivetosixcount = (de.fivetosixcount.toInt + 1).toString)
  }

  private val updateMonthCount = (le: LogEntity) => (de: DashboardEntity) => {
    le.datestamp.split("""-""").toList(1) match {
      case "01" => de.copy(januarycount = (de.januarycount.toInt + 1).toString)
      case "02" => de.copy(februarycount = (de.februarycount.toInt + 1).toString)
      case "03" => de.copy(marchcount = (de.marchcount.toInt + 1).toString)
      case "04" => de.copy(aprilcount = (de.aprilcount.toInt + 1).toString)
      case "05" => de.copy(maycount = (de.maycount.toInt + 1).toString)
      case "06" => de.copy(junecount = (de.junecount.toInt + 1).toString)
      case "07" => de.copy(julycount = (de.julycount.toInt + 1).toString)
      case "08" => de.copy(augustcount = (de.augustcount.toInt + 1).toString)
      case "09" => de.copy(septembercount = (de.septembercount.toInt + 1).toString)
      case "10" => de.copy(octobercount = (de.octobercount.toInt + 1).toString)
      case "11" => de.copy(novembercount = (de.novembercount.toInt + 1).toString)
      case _ => de.copy(decembercount = (de.decembercount.toInt + 1).toString)
    }
  }

  private val getAgeInMonths = (dob: String) => {
    val startCalendar = new GregorianCalendar
    startCalendar.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(dob))
    val endCalendar = new GregorianCalendar
    endCalendar.setTime(new Date())

    val diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)
    diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
  }

  private val deleteFromDb = (activity: Activity) => (de: DashboardEntity) => {
    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .deleteOne(and(equal("doctype", activity.dashboardKey), equal("code", de.code), equal("currentdate", de.currentdate)))
        .toFuture(),
      Duration.Inf
    ).toString
  }

  private val saveToDb = (de: DashboardEntity) => {
    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .insertOne(BsonDocument(new Gson().toJson(de)))
        .toFuture(),
      Duration.Inf
    ).toString()
  }

  private val insertLogRecord = (body: String) =>
    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .insertOne(BsonDocument(new Gson().toJson(body)))
        .toFuture(),
      Duration.Inf
    ).toString()

  def report(awCode: String, date: String, activity: Activity) = {
    val results =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", activity.reportingKey), equal("aanganwadicode", awCode), equal("datestamp", date)))
          .toFuture(),
        Duration.Inf
      ).map(_.toJson())
    "[" + results.mkString(", ") + "]"
  }

  def dashboard(sCode: String, date: String, activity: Activity) = {
    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .find(and(equal("doctype", activity.dashboardKey), equal("code", sCode), equal("datestamp", date)))
        .first()
        .toFuture(),
      Duration.Inf
    ).toJson()
  }

}
