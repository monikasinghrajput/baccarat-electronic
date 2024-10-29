package model.admin

import java.util.UUID

import model.dao.User
import play.api.libs.json.{JsObject, Json, OWrites, Reads}
import scalikejdbc.WrappedResultSet

case class AccountsActivityLog(action: String, userData: UserData) {
  def json: JsObject = Json.writes[AccountsActivityLog].writes(this);

}

object AccountsActivityLog {
  implicit val writes: OWrites[AccountsActivityLog] = Json.writes[AccountsActivityLog];
  implicit val reads: Reads[AccountsActivityLog] = Json.reads[AccountsActivityLog];

  def fromRS(rs: WrappedResultSet): AccountsActivityLog = {
    AccountsActivityLog(
      rs.string("action"),
      UserData(
        rs.string("code"),
        rs.string("name"),
        rs.string("country"),
        rs.string("account"),
        rs.string("usage"),
        rs.string("phone"),
        rs.string("created"),
        rs.string("updated"),
        rs.string("status")))
  }

}





