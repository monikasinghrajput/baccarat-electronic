package model.admin

import play.api.libs.json.{JsObject, Json, OWrites, Reads}
import scalikejdbc.WrappedResultSet

case class GameActivityLog(action: String, gameData: GameData) {
  def json: JsObject = Json.writes[GameActivityLog].writes(this);

}


object GameActivityLog {
  implicit val writes: OWrites[GameActivityLog] = Json.writes[GameActivityLog];
  implicit val reads: Reads[GameActivityLog] = Json.reads[GameActivityLog];

  def fromRS(rs: WrappedResultSet): GameActivityLog = {
    GameActivityLog(
      rs.string("action"),
      GameData(
        rs.string("name"),
        rs.int("minBet"),
        rs.int("maxBet"),
        rs.string("operationalState"),
        rs.string("workingState"),
        rs.string("dateTime"),
        rs.string("dealer")))
  }

}




