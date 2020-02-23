package subscriber.epgmsubscriber

import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.collection.immutable.Document
import subscriber.child.ChildRecord

object Marshaller {
  def apply(childRecord: ChildRecord, docType: String, recordNumber: String): LogEntry = {
    LogEntry(
      docType,
      childRecord.statecode,
      childRecord.districtcode,
      childRecord.projectcode,
      childRecord.sectorcode,
      childRecord.aanganwadicode,
      childRecord.childcode,
      childRecord.weight,
      childRecord.height,
      childRecord.bmi,
      childRecord.whounderweight,
      childRecord.iap,
      childRecord.day,
      childRecord.month,
      childRecord.year,
      childRecord.wasting,
      childRecord.stunting,
      childRecord.minutes,
      childRecord.hours,
      recordNumber,
      childRecord.dayofbirth,
      childRecord.monthofbirth,
      childRecord.yearofbirth,
      childRecord.address,
      childRecord.sex,
      childRecord.category,
      childRecord.fathername,
      childRecord.name,
      childRecord.age
    )
  }

  def toBsonDocument(childRecord: ChildRecord, docType: String, recordNumber: String): Document = {
    Document(
      "doctype" -> docType,
      "statecode" -> childRecord.statecode,
      "districtcode" -> childRecord.districtcode,
      "projectcode" -> childRecord.projectcode,
      "sectorcode" -> childRecord.sectorcode,
      "aanganwadicode" -> childRecord.aanganwadicode,
      "childcode" -> childRecord.childcode,
      "weight" -> childRecord.weight,
      "height" -> childRecord.height,
      "bmi" -> childRecord.bmi,
      "whounderweight" -> childRecord.whounderweight,
      "iap" -> childRecord.iap,
      "day" -> childRecord.day,
      "month" -> childRecord.month,
      "year" -> childRecord.year,
      "wasting" -> childRecord.wasting,
      "stunting" -> childRecord.stunting,
      "minutes" -> childRecord.minutes,
      "hours" -> childRecord.hours,
      "recordnumber" -> recordNumber,
      "dayofbirth" -> childRecord.dayofbirth,
      "monthofbirth" -> childRecord.monthofbirth,
      "yearofbirth" -> childRecord.yearofbirth,
      "address" -> childRecord.address,
      "sex" -> childRecord.sex,
      "category" -> childRecord.category,
      "fathername" -> childRecord.fathername,
      "name" -> childRecord.name,
      "age" -> childRecord.age
    )
  }

}


case class LogEntry(
                     doctype: String,
                     statecode: String,
                     districtcode: String,
                     projectcode: String,
                     sectorcode: String,
                     aanganwadicode: String,
                     childcode: String,
                     weight: String,
                     height: String,
                     bmi: String,
                     whounderweight: String,
                     iap: String,
                     day: String,
                     month: String,
                     year: String,
                     wasting: String,
                     stunting: String,
                     minutes: String,
                     hours: String,
                     recordnumber: String,
                     dayofbirth: String,
                     monthofbirth: String,
                     yearofbirth: String,
                     address: String,
                     sex: String,
                     category: String,
                     fathername: String,
                     name: String,
                     age: String)

