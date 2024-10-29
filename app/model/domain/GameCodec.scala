package model.domain

import play.api.libs.functional.syntax._
import play.api.libs.json._

trait GameCodec {

  implicit val gameEncoder = new Writes[Game] {
    override def writes(game: Game): JsValue =
      Json.obj(
        "id" -> Json.toJson(game.id),
        "name" -> Json.toJson(game.name),
        "game_type" -> Json.toJson(game.game_type),
        "route" -> Json.toJson(game.toString),
        "thumbnail_url" -> Json.toJson(game.thumbnail_url),
        "background_url" -> Json.toJson(game.background_url),
      )
  }

  implicit val gameDecoder: Reads[Game] = (
    (JsPath \ "system").read[String] and
      ((JsPath \ "id").read[String] or (JsPath \ "id").read[Int].map(_.toString)) and
      (JsPath \ "is_mobile_supported").read[Boolean] and
      (JsPath \ "is_demo_supported").read[Boolean] and
      (JsPath \ "is_portrait_view_supported").read[Boolean] and
      (JsPath \ "is_visible").read[Boolean] and
      (JsPath \ "is_available_for_anonymous_user").read[Boolean] and

      (JsPath \ "name").read[String] and
      ((JsPath \ "thumbnail_url").read[String] or Reads.pure("")) and
      ((JsPath \ "background_url").read[String] or Reads.pure("")) and
      (JsPath \ "game_type").read[String] and

      (JsPath \ "categories").read[Array[Int]] and
      (JsPath \ "collections").read[Array[Int]] and
      (JsPath \ "providers").read[Array[Int]] and

      ((JsPath \ "priority").read[Int] or Reads.pure(0)) and
      ((JsPath \ "rating").read[Double] or Reads.pure(0.0)) and
      ((JsPath \ "votes").read[Int] or Reads.pure(0)) and

      (JsPath \ "featured").read[Boolean] and
      (JsPath \ "is_mobile_only").read[Boolean]

    ) (Game.apply _)


}
