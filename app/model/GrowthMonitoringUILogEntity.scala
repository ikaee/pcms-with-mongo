package model

import com.google.gson.Gson

case class GrowthMonitoringUILogEntity(
                           aanganwadicode: String,
                           childcode: String,
                           firstname: String,
                           lastname: String,
                           sex: String,
                           dob: String,
                           logdate: String,
                           weight: String,
                           height: String,
                           bmi: String,
                           stunting: String,
                           wasting: String,
                           whounderweight: String,
                         ) {

  def toJson() = new Gson().toJson(this)
}

 object GrowthMonitoringUILogEntity {

   def from(entity: GrowthMonitoringLogEntity) = {

     GrowthMonitoringUILogEntity(
       entity.aanganwadicode,
       entity.childcode,
       entity.name,
       entity.fathername,
       gender(entity.sex),
       s"${entity.dayofbirth}-${entity.monthofbirth}-${entity.yearofbirth}",
       s"${entity.day}-${entity.month}-${entity.year} ${entity.hours}:${entity.minutes}:00",
       (entity.weight.toDouble / 1000).toString,
       entity.height.toInt.toString,
       entity.bmi.toInt.toString,
       stuntingGrade(entity.stunting),
       wastingGrade(entity.wasting),
       whoUwGrade(entity.whounderweight)
     )
   }

   private def gender(sex: String) =
     if (sex.equals("M")) "MALE"
     else "FEMALE"

   private def whoUwGrade(whounderweight: String) =
     if (whounderweight.equals("0")) "NOR"
     else if(whounderweight.equals("1")) "MUW"
     else if(whounderweight.equals("2")) "SUW"
     else "NA"

   private def stuntingGrade(stunting: String) =
     if (stunting.equals("0")) "NOR"
     else if(stunting.equals("1")) "MAM"
     else if(stunting.equals("2")) "SAM"
     else "NA"

   private def wastingGrade(wasting: String) =
     if (wasting.equals("0")) "NOR"
     else if(wasting.equals("1")) "MAM"
     else if(wasting.equals("2")) "SAM"
     else "NA"

 }