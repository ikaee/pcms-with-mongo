import org.mongodb.scala.bson.BsonValue

package object subscriber {
  val dbName = "thewall"
  val collectionName = "tyrion"
  val dbHost = "https://epgm.documents.azure.com:443/"
  val dbPassword = "KY0qwksz9NUPbfHW2j7XdKmHn5cmR3Iu2dAQPYstWEdhcTxxNY9C08qcWGVQXmoO9nQRnxPnq7DAoY8Ep6XZiA=="

  val queueName = "epgm_logdata"

  val mongoDbHost = "mongodb://localhost"
  val databaseTheWall = "thewall"
  val collectionTyrion = "tyrion"

  implicit def bsonValueToInt(bsonValue: Option[BsonValue]): Int =
    Integer.parseInt(bsonValue.get.asString().getValue)

  implicit def bsonValueToString(bsonValue: Option[BsonValue]): String =
    bsonValue.get.asString().getValue

}
