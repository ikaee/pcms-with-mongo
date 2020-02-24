package model

case class LogAndMasterEntity(
                               doctype: String,
                               image: String,
                               lattitude: String,
                               longitude: String,
                               schoolcode: String,
                               studentcode: String,
                               dob: String,
                               sex: String,
                               firstname: String,
                               lastname: String,
                               timestamp: String,
                               datestamp: String
                             )

object LogAndMasterEntity {

  def createFrom = (logEntity: LogEntity) => (masterEntity: MasterEntity) => {
    LogAndMasterEntity(
      logEntity.doctype,
      logEntity.image,
      logEntity.lattitude,
      logEntity.longitude,
      logEntity.schoolcode,
      logEntity.studentcode,
      s"${masterEntity.dayofbirth}-${masterEntity.monthofbirth}-${masterEntity.yearofbirth}",
      masterEntity.sex,
      masterEntity.name,
      masterEntity.fathername,
      s"${logEntity.datestamp} ${logEntity.timestamp}",
      logEntity.datestamp
    )
  }

}