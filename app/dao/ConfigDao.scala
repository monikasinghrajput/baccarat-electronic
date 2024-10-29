package dao

import model.common.messages.{TableLimit, TableLimitCodec}
import model.domain._
import play.api.Logger
import play.api.libs.json.{JsValue, Json}



class ConfigDao extends GameCodec with TableLimitCodec  {

  val log = Logger(this.getClass)

//  def authenticationParamsJsonString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "authenticationParams.json")
//  var authenticationParams: JsValue = Json.parse(authenticationParamsJsonString)
//
//  def definitionsJsonString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "definitions.json")
//  val definitions: JsValue = Json.parse(definitionsJsonString)
//
//  def initialConfigString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "config.json")
//  val initialConfig: JsValue = Json.parse(initialConfigString)
//
//  def gameJsonString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "game.json")
//  val game: JsValue = Json.parse(gameJsonString)
//
//  def gameTypesString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "gameTypes.json")
//  val gameTypes: JsValue = Json.parse(gameTypesString)
//
//  def groupsString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "groups.json")
//  val groups: JsValue = Json.parse(groupsString)
//
//  def localeString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "locale.json")
//  val locale: JsValue = Json.parse(localeString)
//
//  def tablesString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "tables.json")
//  val tables: JsValue = Json.parse(tablesString)
//
//  def userDataString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "user_data.json")
//  val userData: JsValue = Json.parse(userDataString)
//
//  def videoStreamsString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "video_streams.json")
//  val videoStreams: JsValue = Json.parse(videoStreamsString)
//
//
//  def getInitialDataJsonString: String = {
//    Json.prettyPrint(
//      Json.obj(
//        "errorCode" -> 0,
//        "skin" -> "legacy",
//        "language" -> "en",
//        "boAuthenticated" -> false,
//        "initialAuthenticationTarget" -> "lobby",
//        "authErros" -> Json.obj(),
//        "authenticationParams" -> authenticationParams,
//        "definitions" -> definitions,
//        "game" -> game,
//        "gameTypes" -> gameTypes,
//        "initialConfig" -> initialConfig,
//        "locale" -> locale,
//      )
//    );
//  }
//
//  def authenticateJsonString: String = {
//    Json.prettyPrint(
//      Json.obj(
//        "error_code" -> 0,
//        "error_message" -> "",
//        "result" -> Json.obj(
//          "authentication_params" -> authenticationParams,
//          "config" -> initialConfig,
//          "definitions" -> definitions,
//          "favorite" -> Json.arr(),
//          "game_types" -> gameTypes,
//          "groups" -> groups,
//          "language" -> "en",
//          "locale" -> locale,
//          "recent" -> Json.arr(),
//          "tables" -> tables,
//          "user_data" -> userData
//        )
//      )
//    );
//  }
//
//  def sendStreamsJsonString: String = {
//    Json.prettyPrint(
//      videoStreams
//    );
//  }
//
//  def getTableLimits(tableId: String, limitId: Int): TableLimit = {
//    val table = tables(tableId)
//    val tableLimit: JsValue = table("limits")(limitId.toString)
//    tableLimit.as[TableLimit]
//  }


}
