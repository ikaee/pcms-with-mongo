package model

import com.google.gson.Gson

case class GrowthMonitoringLogEntity(
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
                           age: String
                         )

 object GrowthMonitoringLogEntity {

   def fromJson(json: String): GrowthMonitoringLogEntity = new Gson().fromJson(json, classOf[GrowthMonitoringLogEntity])

 }