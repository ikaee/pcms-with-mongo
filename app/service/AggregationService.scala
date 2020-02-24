package service

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, GregorianCalendar}

import com.google.gson.Gson
import model.{Activity, DashboardEntity, LogAndMasterEntity, LogEntity, MasterEntity}
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters.{and, equal}
import subscriber.{collectionTyrion, databaseTheWall, mongoDbHost}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

object AggregationService {

  def addLogRecord(body: String, activity: Activity) = {
    val logEntity = createLogEntity(body)
    val masterEntity = fetchMasterInfo(logEntity)
    val dashboardEntity = fetchDashboardEntity(logEntity)(activity)
    insertLogRecord(logEntity)(masterEntity)
    deleteFromDb(activity)(dashboardEntity)
    val updatedDashboardEntity = updateDashboardEntity(logEntity)(masterEntity)(dashboardEntity)
    saveToDb(updatedDashboardEntity)
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
              equal("aanganwadicode", ae.schoolcode),
              equal("childcode", ae.studentcode)))
          .first()
          .toFuture(),
        Duration.Inf
      ).toJson())
  }

  private val fetchDashboardEntity = (logEntity: LogEntity) => (activity: Activity) => {
    Try(DashboardEntity.fromJson(
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(
            and(
              equal("doctype", activity.dashboardKey),
              equal("code", logEntity.schoolcode.take(2)),
              equal("currentdate", logEntity.datestamp)))
          .first()
          .toFuture(),
        Duration.Inf
      ).toJson())).fold(_ => createDefaultDashboardEntity(logEntity)(activity), x => x)
  }

  private val createDefaultDashboardEntity = (logEntity: LogEntity) => (activity: Activity) => {
    DashboardEntity.defaultFor(activity)(logEntity.datestamp)(logEntity.schoolcode.take(2))(fetchMasterInfoCount(logEntity).toString)
  }

  private val fetchMasterInfoCount = (entity: LogEntity) => {
    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .find(
          and(
            equal("doctype", "child"),
            equal("aanganwadicode", entity.schoolcode)))
        .toFuture(),
      Duration.Inf
    ).size
  }

  private val updateDashboardEntity = (le: LogEntity) => (me: MasterEntity) => (de: DashboardEntity) => {
    (updatePresentCount andThen updateGenderCount(me) andThen updateAgeCount(me) andThen updateMonthCount(le)) (de)
  }

  private val updatePresentCount = (de: DashboardEntity) => {
    val updatedCount = (de.presentcount.toInt + 1).toString
    val percentage = Try(((updatedCount.toInt * 100) / de.totalcount.toInt).toString).fold(_ => "0", x => x)
    de.copy(presentcount = updatedCount, percentage = percentage)
  }

  private val updateGenderCount = (me: MasterEntity) => (de: DashboardEntity) => {
    if (me.sex.equals("M")) de.copy(malecount = (de.malecount.toInt + 1).toString)
    else de.copy(femalecount = (de.femalecount.toInt + 1).toString)
  }

  private val updateAgeCount = (me: MasterEntity) => (de: DashboardEntity) => {
    val ageInMonths = getAgeInMonths(s"${me.dayofbirth}-${me.monthofbirth}-${me.yearofbirth}")
    if (ageInMonths <= 12) de.copy(zerotoonecount = (de.zerotoonecount.toInt + 1).toString)
    else if (ageInMonths > 12 && ageInMonths <= 24) de.copy(onetotwocount = (de.onetotwocount.toInt + 1).toString)
    else if (ageInMonths > 24 && ageInMonths <= 36) de.copy(twotothreecount = (de.twotothreecount.toInt + 1).toString)
    else if (ageInMonths > 36 && ageInMonths <= 48) de.copy(threetofourcount = (de.threetofourcount.toInt + 1).toString)
    else if (ageInMonths > 48 && ageInMonths <= 60) de.copy(fourtofivecount = (de.fourtofivecount.toInt + 1).toString)
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

  private val insertLogRecord = (logEntity: LogEntity) => (masterEntity: MasterEntity) => {
    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .insertOne(Document(createLogRecord(logEntity)(masterEntity)))
        .toFuture(),
      Duration.Inf
    ).toString()
  }

  private val createLogRecord = (logEntity: LogEntity) => (masterEntity: MasterEntity) =>
    new Gson().toJson(LogAndMasterEntity.createFrom(logEntity)(masterEntity))

  def report(awCode: String, date: String, activity: Activity) = {
    val results =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", activity.reportingKey), equal("schoolcode", awCode), equal("datestamp", date)))
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
        .find(and(equal("doctype", activity.dashboardKey), equal("code", sCode), equal("currentdate", date)))
        .first()
        .toFuture(),
      Duration.Inf).toJson()
  }

}
