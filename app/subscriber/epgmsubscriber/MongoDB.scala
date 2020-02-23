package subscriber.epgmsubscriber

import java.time.LocalDate
import java.time.temporal.ChronoUnit

import org.joda.time.DateTime
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.collection.Document
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._
import subscriber._
import subscriber.aggregation.dataobjects.UpdateEntity
import subscriber.aggregation.engine.ProcessingEngine
import subscriber.child.ChildRecord
import subscriber.convertors.conversions._
import subscriber.record.validation.validation.Validator

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoDB {

  val LOG_DOC_TYPE = "log"

  def insertRecordInDatabase(record: Record) = {
    val results =
      Await.result(
        MongoClient(mongoDbHost)
          .getDatabase(databaseTheWall)
          .getCollection(collectionTyrion)
          .find(and(equal("doctype", "log"), equal("aanganwadicode", record("aanganwadicode")), equal("childcode", record("childcode"))))
          .sort(descending("recordnumber"))
          .toFuture(),
        Duration.Inf
      )

    if (results.isEmpty) insertFirstRecord(record)
    else insertNewRecord(record, results.head)
  }

  private def insertNewRecord(logRecord: Record, masterDoc: Document) = {
    val rcordCount: Int = Integer.parseInt(masterDoc.get("recordnumber").get.asString().getValue) + 1
    val currentAge = getAgeInMonths(Integer.parseInt( masterDoc.get("yearofbirth").get.asString().getValue), Integer.parseInt( masterDoc.get("monthofbirth").get.asString().getValue), Integer.parseInt( masterDoc.get("dayofbirth").get.asString().getValue))
    val masterRecord = Map("doctype" -> "log", "age" -> currentAge.toString, "recordnumber" -> (rcordCount).toString, "dayofbirth" ->  masterDoc.get("dayofbirth").get.asString().getValue, "monthofbirth" ->  masterDoc.get("monthofbirth").get.asString().getValue, "yearofbirth" ->  masterDoc.get("yearofbirth").get.asString().getValue, "address" ->  masterDoc.get("address").get.asString().getValue, "sex" ->  masterDoc.get("sex").get.asString().getValue, "name" ->  masterDoc.get("name").get.asString().getValue, "fathername" ->  masterDoc.get("fathername").get.asString().getValue, "category" ->  masterDoc.get("category").get.asString().getValue)

    val combinedRecord = logRecord ++ masterRecord

    Validator.apply(combinedRecord("statecode"), combinedRecord, masterDoc) match {
      case Left(x) => {
        insert(combinedRecord, LOG_DOC_TYPE, rcordCount.toString)
        val upd = new UpdateEntity(logRecord("statecode"), logRecord("districtcode"),
          logRecord("projectcode"), logRecord("sectorcode"), logRecord("aanganwadicode"),
          logRecord("whounderweight"),  masterDoc.get("sex").get.asString().getValue, currentAge.toString,
          logRecord("month"), logRecord("year"))
        new ProcessingEngine().updateDB(upd)
      }
      case Right(x) => println(DateTime.now() + " Error : Malfarmed record of type " + x._2 + " record " + logRecord.values.mkString(""))
    }

  }

  def insert(childRecord: ChildRecord, docType: String, recordNumber: String) = {
    Await.result(
      MongoClient(mongoDbHost)
        .getDatabase(databaseTheWall)
        .getCollection(collectionTyrion)
        .insertOne(Marshaller.toBsonDocument(childRecord, docType, recordNumber))
        .toFuture(),
      Duration.Inf
    )
  }

  private def getAgeInMonths(year: Int, month: Int, day: Int): Long = {
    val start = LocalDate.of(year, month, day)
    val end = LocalDate.now()
    Math.abs(ChronoUnit.MONTHS.between(start, end))
  }

  private def insertFirstRecord(record: Record) = {

    def getRecordFromMasterData(): Record = {
      val results =
        Await.result(
          MongoClient(mongoDbHost)
            .getDatabase(databaseTheWall)
            .getCollection(collectionTyrion)
            .find(and(equal("doctype", "child"), equal("aanganwadicode", record("aanganwadicode")), equal("childcode", record("childcode"))))
            .toFuture(),
          Duration.Inf
        )

      if (results.nonEmpty) {
        val currentDoc = results.head
        val age = getAgeInMonths(currentDoc.get("yearofbirth"), currentDoc.get("monthofbirth"), currentDoc.get("dayofbirth"))
        Map("doctype" -> "log", "age" -> age.toString, "dayofbirth" -> currentDoc.get("dayofbirth"), "monthofbirth" -> currentDoc.get("monthofbirth"), "yearofbirth" -> currentDoc.get("yearofbirth"), "recordnumber" -> "1", "address" -> currentDoc.get("address"), "category" -> currentDoc.get("category"), "sex" -> currentDoc.get("sex"), "name" -> currentDoc.get("name"), "fathername" -> currentDoc.get("fathername"))
      } else {
        Map()
      }
    }

    val recordFromMaster = getRecordFromMasterData()
    if (recordFromMaster.nonEmpty) {
      val combinedRecord = record ++ recordFromMaster
      insert(combinedRecord, LOG_DOC_TYPE, "1")

      val combinedRecordAsMap = combinedRecord
      val upd = UpdateEntity(combinedRecordAsMap("statecode"),
        combinedRecordAsMap("districtcode"),
        combinedRecordAsMap("projectcode"),
        combinedRecordAsMap("sectorcode"),
        combinedRecordAsMap("aanganwadicode"),
        combinedRecordAsMap("whounderweight"),
        combinedRecordAsMap("sex"),
        combinedRecordAsMap("age"),
        combinedRecordAsMap("month"),
        combinedRecordAsMap("year"))

      new ProcessingEngine().updateDB(upd)
    } else
      println(DateTime.now() + " Error : Master data not found for record :" + record.values.mkString(", "))
  }

}
