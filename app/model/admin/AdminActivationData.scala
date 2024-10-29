package model.admin.data

import play.api.libs.json.{Json, Writes, Reads, JsValue}

case class AdminActivationData(userCode:String,
                userFullName: String,
                userCategory: String) {
  def json = Json.writes[AdminActivationData].writes(this)
}

object AdminActivationData {
  implicit val adminActivationDataWrites = new Writes[AdminActivationData] {
    def writes(adminActivationData : AdminActivationData): JsValue = {
      Json.obj(
        "userCode" -> adminActivationData.userCode,
        "fullName" -> adminActivationData.userFullName,
        "userCategory" -> adminActivationData.userCategory,
      )
    }
  }

  implicit val adminActivationDataReads: Reads[AdminActivationData] = Json.reads[AdminActivationData]
}


