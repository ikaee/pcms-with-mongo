package service

import java.util.Properties

import javax.mail._
import javax.mail.internet.{InternetAddress, MimeMessage}
import subscriber.child.ChildRecord

object EmailService {

  def send(childRecord: ChildRecord): String = {

    val username = "pcms.notifications@gmail.com"
    val password = "Pcms@123"

    val prop = new Properties
    prop.put("mail.smtp.host", "smtp.gmail.com")
    prop.put("mail.smtp.port", "587")
    prop.put("mail.smtp.auth", "true")
    prop.put("mail.smtp.starttls.enable", "true")

    val session = Session.getInstance(prop, new Authenticator() {
      override protected def getPasswordAuthentication = new PasswordAuthentication(username, password)
    })

    try {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress("pcms.notifications@gmail.com", "PCMS Notifications"))
      message.setRecipients(Message.RecipientType.TO, "pcms.notifications@gmail.com")
      message.setSubject("Malnourished Child Recorded")
      message.setText(createBody(childRecord))
      Transport.send(message)
      "Email sent successfully"
    } catch {
      case e: MessagingException =>
        s"Error while sending email, ${e.getMessage}"
    }
  }

  private def createBody(record: ChildRecord): String =
    "Beneficiary Details - \n" +
      "Code - " + record.childcode +
      "\nAanganwadi Code - " + record.aanganwadicode +
      "\nName - " + record.name + " " + record.fathername +
      "\nGender - " + gender(record.sex) +
      "\nWeight - " + (record.weight.toDouble / 1000).toString + " Kgs" +
      "\nHeight - " + record.height.toInt.toString + " Cms" +
      "\nWHO Underweight - " + whoUwGrade(record.whounderweight) +
      "\nStunting - " + stuntingGrade(record.stunting) +
      "\nWasting - " + wastingGrade(record.wasting) +
      "\nLog Date - " + record.day + "-" + record.month + "-" + record.year

  private def gender(sex: String) =
    if (sex.equals("M")) "Male"
    else "Female"

  private def whoUwGrade(whounderweight: String) =
    if (whounderweight.equals("0")) "NOR"
    else if (whounderweight.equals("1")) "MUW"
    else if (whounderweight.equals("2")) "SUW"
    else "NA"

  private def stuntingGrade(stunting: String) =
    if (stunting.equals("0")) "NOR"
    else if (stunting.equals("1")) "MAM"
    else if (stunting.equals("2")) "SAM"
    else "NA"

  private def wastingGrade(wasting: String) =
    if (wasting.equals("0")) "NOR"
    else if (wasting.equals("1")) "MAM"
    else if (wasting.equals("2")) "SAM"
    else "NA"

}
