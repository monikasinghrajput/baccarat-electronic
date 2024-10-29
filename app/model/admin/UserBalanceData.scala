package model.admin

import play.api.libs.json.{Json, _}

case class UserBalanceData(user: String, curBalance: Double, changeBalance: Double) {
  def json = Json.writes[UserBalanceData].writes(this)
}

object UserBalanceData {
  implicit val UserBalanceDataReads: Reads[UserBalanceData] = Json.reads[UserBalanceData]
}
