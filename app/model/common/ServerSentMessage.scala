package model

import play.api.libs.json.{JsValue, Json, Writes}


case class ServerSentMessage(event: String, payload: JsValue) {
  def json: JsValue = Json.toJson(this)(ServerSentMessage.writes)
}

object ServerSentMessage {
  val writes = Json.writes[ServerSentMessage]
  def create[T](event: String, payload: T)(implicit encoder: Writes[T]) = ServerSentMessage(event, encoder.writes(payload))
}