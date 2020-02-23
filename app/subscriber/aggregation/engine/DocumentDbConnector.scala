package subscriber.aggregation.engine


import com.google.gson.Gson
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.collection.Document
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Updates._
import subscriber._
import subscriber.aggregation.dataobjects._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class DocumentDbConnector(host: String, masterKey: String, databaseId: String, collectionId: String) {

  def getConsolidatedRecord(code: String) = {
    val results =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", "dashboard"), equal("code", code)))
          .toFuture(),
        Duration.Inf)
        .maxBy(d => s"""${d.get("currentyear").get.asString().getValue}${d.get("currentmonth").get.asString().getValue}""".toInt)

    val records =
      results
        .toMap
        .map(tuple => (tuple._1, tuple._2.asString().getValue))

    (Some(records), Some(results))
  }

  def updateConsolidatedRecord(old: Document, tyrionEntity: TyrionEntity): Unit = {
    val allSets =
      Seq(set("doctype", tyrionEntity.doctype),
        set("code", tyrionEntity.code),
        set("suwcount", tyrionEntity.suwcount),
        set("muwcount", tyrionEntity.muwcount),
        set("normalcount", tyrionEntity.normalcount),
        set("totalcount", tyrionEntity.totalcount),
        set("malecount", tyrionEntity.malecount),
        set("femalecount", tyrionEntity.femalecount),
        set("zerotoonecount", tyrionEntity.zerotoonecount),
        set("onetotwocount", tyrionEntity.onetotwocount),
        set("twotothreecount", tyrionEntity.twotothreecount),
        set("threetofourcount", tyrionEntity.threetofourcount),
        set("fourtofivecount", tyrionEntity.fourtofivecount),
        set("fivetosixcount", tyrionEntity.fivetosixcount),
        set("januarycount", tyrionEntity.januarycount),
        set("februarycount", tyrionEntity.februarycount),
        set("marchcount", tyrionEntity.marchcount),
        set("aprilcount", tyrionEntity.aprilcount),
        set("maycount", tyrionEntity.maycount),
        set("junecount", tyrionEntity.junecount),
        set("julycount", tyrionEntity.julycount),
        set("augustcount", tyrionEntity.augustcount),
        set("septembercount", tyrionEntity.septembercount),
        set("octobercount", tyrionEntity.octobercount),
        set("novembercount", tyrionEntity.novembercount),
        set("decembercount", tyrionEntity.decembercount),
        set("currentmonth", tyrionEntity.currentmonth),
        set("currentyear", tyrionEntity.currentyear))

    val findQuery =
      and(
        equal("currentyear", bsonValueToString(old.get("currentyear"))),
        equal("currentmonth", bsonValueToString(old.get("currentmonth"))),
        equal("code", bsonValueToString(old.get("code"))),
        equal("doctype", bsonValueToString(old.get("doctype"))))

    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .updateOne(findQuery, allSets)
        .toFuture(),
      Duration.Inf)
  }

  def insertConsolidatedRecord(tyrionEntity: TyrionEntity): Unit = {
    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .insertOne(BsonDocument(new Gson().toJson(tyrionEntity)))
        .toFuture(),
      Duration.Inf)
  }

}
