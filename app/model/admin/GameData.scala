package model.admin

import play.api.libs.json.{JsObject, Json, OWrites, Reads}
import scalikejdbc.WrappedResultSet

case class GameData(name: String, 
                    minBet: Int, 
                    maxBet: Int,
                    operationalState: String,
                    workingState: String, 
                    updated: String, 
                    dealer: String) {
  def json: JsObject = Json.writes[GameData].writes(this);

}


object GameData {
  def fromRS(rs: WrappedResultSet): GameData = {
    GameData(
      rs.string("name"),
      rs.int("minBet"),
      rs.int("maxBet"),
      rs.string("operationalState"),
      rs.string("workingState"),
      rs.string("updated"),
      rs.string("dealer"))
  }

  implicit val writes: OWrites[GameData] = Json.writes[GameData];
  implicit val reads: Reads[GameData] = Json.reads[GameData];

}





