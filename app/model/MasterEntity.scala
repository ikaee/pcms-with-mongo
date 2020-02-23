package model

import com.google.gson.Gson

case class MasterEntity(
                         doctype: String,
                         dayofbirth: String,
                         monthofbirth: String,
                         yearofbirth: String,
                         address: String,
                         sex: String,
                         recordnumber: String,
                         childcode: String,
                         category: String,
                         name: String,
                         fathername: String,
                         aanganwadicode: String
                       )

object MasterEntity {

  def fromJson(body: String): MasterEntity = new Gson().fromJson(body, classOf[MasterEntity])

}