package model.admin

import play.api.libs.json.{JsObject, Json, OWrites, Reads}

case class UserMoneyData(name: String, userCode: String, amount: Double, dateTime: String) {
  def json: JsObject = Json.writes[UserMoneyData].writes(this);

}

object UserMoneyData {
  implicit val writes: OWrites[UserMoneyData] = Json.writes[UserMoneyData];
  implicit val userMoneyDataRead: Reads[UserMoneyData] = Json.reads[UserMoneyData];

}




