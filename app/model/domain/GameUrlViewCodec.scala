package model.domain

import play.api.libs.functional.syntax._
import play.api.libs.json._

trait GameUrlViewCodec {

    implicit val gameUrlViewEncoder: Writes[GameUrlView] = new Writes[GameUrlView] {
      override def writes(gameUrlView: GameUrlView): JsValue =
        Json.obj(
          "name" -> Json.toJson(gameUrlView.name),
          "id" -> Json.toJson(gameUrlView.id),
          "game_name" -> Json.toJson(gameUrlView.game_name),
          "game_type" -> Json.toJson(gameUrlView.game_type),
          "provider_name" -> Json.toJson(gameUrlView.provider_name),
          "is_demo_supported" -> Json.toJson(gameUrlView.is_demo_supported),
          "is_available_for_anonymous_user" -> Json.toJson(gameUrlView.is_available_for_anonymous_user),
          "is_mobile_only" -> Json.toJson(gameUrlView.is_mobile_only),
          "is_mobile_supported" -> Json.toJson(gameUrlView.is_mobile_supported),
          "is_portrait_view_supported" -> Json.toJson(gameUrlView.is_portrait_view_supported),
          "is_visible" -> Json.toJson(gameUrlView.is_visible),
          "game_url" -> Json.toJson(gameUrlView.game_url),
          "thumbnail_url" -> Json.toJson(gameUrlView.thumbnail_url),
        )
    }

    implicit val gameUrlViewDecoder: Reads[GameUrlView] = (
      ((JsPath \ "name").read[String] or Reads.pure("")) and
      ((JsPath \ "id").read[String] or Reads.pure("")) and
      ((JsPath \ "game_name").read[String] or Reads.pure("")) and
      ((JsPath \ "game_type").read[String] or Reads.pure("")) and
      ((JsPath \ "provider_name").read[String] or Reads.pure("")) and
      ((JsPath \ "is_visible").read[Boolean] ) and
      ((JsPath \ "is_demo_supported").read[Boolean] ) and
      ((JsPath \ "is_mobile_only").read[Boolean] ) and
      ((JsPath \ "is_mobile_supported").read[Boolean] ) and
      ((JsPath \ "is_portrait_view_supported").read[Boolean] ) and
      ((JsPath \ "is_available_for_anonymous_user").read[Boolean] ) and
      ((JsPath \ "game_url").read[String] or Reads.pure("")) and
      ((JsPath \ "thumbnail_url").read[String] or Reads.pure(""))
      ) (GameUrlView.apply _)


}
