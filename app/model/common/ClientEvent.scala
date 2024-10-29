package model

import play.api.libs.json.{JsValue, Json, Reads}


case class ClientEvent(event: String,payload: JsValue) extends UserEvent {
  override def action: String = event
  override def json: JsValue = Json.writes[ClientEvent].writes(this)
}

object ClientEvent {
  implicit val reads: Reads[ClientEvent] = Json.reads[ClientEvent]
}