package subscriber

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}
import org.mongodb.scala.model.Updates._

object MongoDBTrials {

  // Use a Connection String
  val mongoClient: MongoClient = MongoClient("mongodb://localhost")

  //  // or provide custom MongoClientSettings
  //  val settings: MongoClientSettings = MongoClientSettings.builder()
  //    .applyToClusterSettings(b => b.hosts(List(new ServerAddress("localhost")).asJava))
  //    .build()
  //  val mongoClient: MongoClient = MongoClient(settings)

  case class Info(x1: Int, y2: Int)

  case class Random(name: String, `type`: String, count: Int, info: Info)

  //  case class Random(_id: ObjectId, name: String, typee: String, count: Int, info: Info)

  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Random], classOf[Info]), DEFAULT_CODEC_REGISTRY)

  val database: MongoDatabase = mongoClient.getDatabase("mydb")

  val collection = database.getCollection("test");

  val doc = Document("_id" -> 0, "name" -> "mongoDB", "type" -> "database",
    "count" -> 1, "info" -> Document("x1" -> 1, "y2" -> 2))

  val random = Random("mongoDB", "database", 1, Info(1, 2))

  def insert = {
    collection.insertOne(doc)
  }

  def find = {
    collection.find(and(equal("type", "database"))).sort(descending("info.x1")).first()
  }

  def update = {
    collection.updateOne(equal("info.x1", 1), Seq(set("info.x1", 11)))
  }

  def updateMany = {
    collection.updateMany(equal("type", "database"), set("type", "databases"))
  }

  def delete = {
    collection.deleteOne(equal("info.x1", 11))
  }

  def deleteMany = {
    collection.deleteMany(equal("type", "database"))
  }

}
