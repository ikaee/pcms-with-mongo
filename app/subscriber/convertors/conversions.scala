package subscriber.convertors

import org.mongodb.scala.bson.collection.Document
import subscriber.child.ChildRecord
import subscriber.epgmsubscriber.Record

object conversions {

  implicit def mapToChildRecord(record: Record): ChildRecord = {
    ChildRecord(
      record("statecode"),
      record("districtcode"),
      record("projectcode"),
      record("sectorcode"),
      record("aanganwadicode"),
      record("childcode"),
      record("weight"),
      record("height"),
      record("bmi"),
      record("whounderweight"),
      record("iap"),
      record("day"),
      record("month"),
      record("year"),
      record("wasting"),
      record("stunting"),
      record("minutes"),
      record("hours"),
      record("dayofbirth"),
      record("monthofbirth"),
      record("yearofbirth"),
      record("address"),
      record("sex"),
      record("category"),
      record("fathername"),
      record("name"),
      record("age"))
  }

  implicit def childRecordToMap(childRecord: ChildRecord): Record = {
    Map(
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

  implicit def documentToChildRecord(document: Document): ChildRecord = {
    ChildRecord(
      document("statecode").asString().getValue,
      document("districtcode").asString().getValue,
      document("projectcode").asString().getValue,
      document("sectorcode").asString().getValue,
      document("aanganwadicode").asString().getValue,
      document("childcode").asString().getValue,
      document("weight").asString().getValue,
      document("height").asString().getValue,
      document("bmi").asString().getValue,
      document("whounderweight").asString().getValue,
      document("iap").asString().getValue,
      document("day").asString().getValue,
      document("month").asString().getValue,
      document("year").asString().getValue,
      document("wasting").asString().getValue,
      document("stunting").asString().getValue,
      document("minutes").asString().getValue,
      document("hours").asString().getValue,
      document("dayofbirth").asString().getValue,
      document("monthofbirth").asString().getValue,
      document("yearofbirth").asString().getValue,
      document("address").asString().getValue,
      document("sex").asString().getValue,
      document("category").asString().getValue,
      document("fathername").asString().getValue,
      document("name").asString().getValue,
      document("age").asString().getValue)
  }

}