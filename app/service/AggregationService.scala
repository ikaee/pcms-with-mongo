package service

import com.google.gson.Gson
import model.Activity
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Filters.{and, equal}
import subscriber.{collectionTyrion, databaseTheWall, mongoDbHost}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object AggregationService {

  def addLogRecord(body: String) = {
    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .insertOne(BsonDocument(new Gson().toJson(body)))
        .toFuture(),
      Duration.Inf
    ).toString()
  }

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
