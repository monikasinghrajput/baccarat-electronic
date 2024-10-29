package model

import play.api.libs.json.JsValue

trait UserEvent {
  def action: String
  def json: JsValue
}
