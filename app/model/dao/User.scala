package model.dao

import java.util.UUID

import play.api.libs.json._
import play.api.libs.json.{JsPath, Reads}
import play.api.libs.functional.syntax._

import scalikejdbc.WrappedResultSet

case class User(userId: UUID,
                userCode: String,
                fullName: String,
                country: String,
                account: String,
                usage: String,
                phone: String,
                created: String,
                status: String,
                password: String,
                balance: Double,
                exposure: Int,
                isAdmin: Boolean) {
  def urlCode: String = userId.toString.split("-")(0)
}


object User {

  ////val json = Json.toJson(user) is allowed with the below implicit converter
  implicit val writes: Writes[User] = new Writes[User] {
    def writes(user: User): JsValue = Json.obj(
      "name" -> user.fullName,
      "code" -> user.userCode,
      "country" -> user.country,
      "account" -> user.account,
      "usage" -> user.usage,
      "phone" -> user.phone,
      "created" -> user.created,
      "status" -> user.status,
      "balance" -> user.balance
    )
  }
  //implicit val writes = Json.writes[User]
  implicit val reads: Reads[User] = Json.reads[User]

//  implicit val reads: Reads[User] = (
//      (JsPath \ "code").read[String] and
//      (JsPath \ "name").read[String] and
//      (JsPath \ "country").read[String] and
//      (JsPath \ "account").read[String] and
//      (JsPath \ "usage").read[String] and
//      (JsPath \ "phone").read[String] and
//      (JsPath \ "created").read[String] and
//      (JsPath \ "status").read[String] and
//      (JsPath \ "balance").read[Double] and
//      (JsPath \ "isAdmin").read[Boolean]
//    )(User.apply _)

  def fromRS(rs: WrappedResultSet): User = {
    User(
      UUID.fromString(rs.string("user_id")),
      rs.string("user_code"),
      rs.string("full_name"),
      rs.string("country"),
      rs.string("account"),
      rs.string("usage"),
      rs.string("phone"),
      rs.string("created"),
      rs.string("status"),
      rs.string("password"),
      rs.int("balance"),
      rs.int("exposure"),
      rs.boolean("is_admin"))
  }
}
