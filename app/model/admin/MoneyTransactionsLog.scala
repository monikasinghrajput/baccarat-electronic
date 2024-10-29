package model.admin

import play.api.libs.json.{JsObject, Json, OWrites, Reads}
import scalikejdbc.WrappedResultSet

case class MoneyTransactionsLog(action: String, userMoneyData: UserMoneyData) {
  def json: JsObject = Json.writes[MoneyTransactionsLog].writes(this);

}


object MoneyTransactionsLog {
  implicit val writes: OWrites[MoneyTransactionsLog] = Json.writes[MoneyTransactionsLog];
  implicit val reads: Reads[MoneyTransactionsLog] = Json.reads[MoneyTransactionsLog];

  def fromRS(rs: WrappedResultSet): MoneyTransactionsLog = {
    MoneyTransactionsLog(
      rs.string("action"),
      UserMoneyData(
        rs.string("name"),
        rs.string("userCode"),
        rs.double("amount"),
        rs.string("dateTime")))
  }

}








