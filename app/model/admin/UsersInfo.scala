package model.admin

import play.api.libs.json.{Json, _}

case class UsersInfo(usersInfo: Array[UserData]) {
  def json = Json.writes[UsersInfo].writes(this)
}

object UsersInfo {
  implicit val UsersInfoReads: Reads[UsersInfo] = Json.reads[UsersInfo]
}


