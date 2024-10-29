package model.domain

import play.api.libs.functional.syntax._
import play.api.libs.json._

trait GameViewCodec {

    implicit val gameViewEncoder = new Writes[GameView] {
      override def writes(gameView: GameView): JsValue =
        Json.obj(
          "id" -> Json.toJson(gameView.id),
          "name" -> Json.toJson(gameView.name),
          "game_type" -> Json.toJson(gameView.game_type),
          "route" -> Json.toJson(gameView.toString),
          "thumbnail_url" -> Json.toJson(gameView.thumbnail_url),
        )
    }

    implicit val gameViewDecoder: Reads[GameView] = (
        ((JsPath \ "id").read[String] or (JsPath \ "id").read[Int].map(_.toString)) and
        (JsPath \ "is_demo_supported").read[Boolean] and
        (JsPath \ "is_available_for_anonymous_user").read[Boolean] and
        (JsPath \ "is_mobile_supported").read[Boolean] and
        (JsPath \ "is_mobile_only").read[Boolean] and
        (JsPath \ "is_portrait_view_supported").read[Boolean] and
        (JsPath \ "name").read[String] and
        (JsPath \ "providers").read[Array[Int]] and
        (JsPath \ "system").read[String] and
        (JsPath \ "game_type").read[String] and
        ((JsPath \ "thumbnail_url").read[String] or Reads.pure(""))

      ) (GameView.apply _)


}
