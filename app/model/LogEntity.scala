package model

import com.google.gson.Gson

case class LogEntity(
                           datestamp: String,
                           doctype: String,
                           image: String,
                           ismatching: String,
                           lattitude: String,
                           longitude: String,
                           predictedpersoncode: String,
                           schoolcode: String,
                           studentcode: String,
                           timestamp: String
                         )

 object LogEntity {

   def fromJson(json: String): LogEntity = new Gson().fromJson(json, LogEntity.getClass)

 }