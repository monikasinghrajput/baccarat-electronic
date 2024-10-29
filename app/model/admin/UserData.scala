package model.admin

import play.api.libs.json.{JsObject, Json, OWrites, Reads}

case class UserData(code: String, 
                    name: String, 
                    country: String, 
                    account: String, 
                    usage: String, 
                    phone: String, 
                    created: String, 
                    updated: String, 
                    status: String) {
  def json: JsObject = Json.writes[UserData].writes(this);

}

object UserData {
  implicit val writes: OWrites[UserData] = Json.writes[UserData];
  implicit val reads: Reads[UserData] = Json.reads[UserData];

}




