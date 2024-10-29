package dao

import model.baccarat.data._
import os.Path
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json._

import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.Calendar
import scala.collection.convert.ImplicitConversions.`enumeration AsScalaIterator`

trait JsonPaths {
  val gameDataPath: Path = os.home / "baccarat" / "game_data.json"
  val licenceDataPath: Path = os.pwd / "conf" / "jsons" / "license.json"

  val authParamsPath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "authenticationParams.json"
  val definitionsPath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "definitions.json"
  val initialConfigPath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "config.json"
  val gamePath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "game.json"
  val gameTypesPath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "gameTypes.json"
  val groupsPath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "groups.json"
  val localePath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "locale.json"
  val tablesPath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "tables.json"
  val userDataPath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "user_data.json"
  val videoStreamsPath: Path = os.pwd / "conf" / "jsons" / "config" / "baccarat" / "video_streams.json"
}

trait JsonStrings extends JsonPaths {
  def licenseDataJsonString: String = os.read(licenceDataPath)

  def authenticationParamsJsonString: String = os.read(authParamsPath)

  def definitionsJsonString: String = os.read(definitionsPath)

  def initialConfigString: String = os.read(initialConfigPath)

  def gameJsonString: String = os.read(gamePath)

  def gameTypesString: String = os.read(gameTypesPath)

  def groupsString: String = os.read(groupsPath)

  def localeString: String = os.read(localePath)

  def tablesString: String = os.read(tablesPath)

  def userDataString: String = os.read(userDataPath)

  def videoStreamsString: String = os.read(videoStreamsPath)


}

trait BaccaratDaoCodecs {

  /* License*/
  implicit val licenseDataWrites: Writes[LicenseData] = (licenseData: LicenseData) => Json.obj(
    "name" -> licenseData.name,
    "client" -> licenseData.client,
    "install" -> licenseData.install,
    "macs" -> licenseData.macs,
    "validProductCode" -> licenseData.validProductCode,
    "validProfitCode" -> licenseData.validProfitCode,
    "toBeExpired" -> licenseData.toBeExpired,
    "productCode" -> licenseData.productCode,
    "profitCode" -> licenseData.profitCode
  )

  implicit val licenseDataReads: Reads[LicenseData] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "client").read[String] and
      (JsPath \ "install").read[String] and
      (JsPath \ "macs").read[List[String]] and
      (JsPath \ "validProductCode").read[Boolean] and
      (JsPath \ "validProfitCode").read[Boolean] and
      (JsPath \ "toBeExpired").read[Boolean] and
      (JsPath \ "productCode").read[String] and
      (JsPath \ "profitCode").read[String]
  )(LicenseData.apply _)

  /* For Admin Configuration Support for
  * ConfigData
  * 1.
  * 2.
  * 3.
  *  */

  implicit val tableBetLimitWrites: Writes[TableBetLimit] = (o: TableBetLimit) => Json.obj(
    "Min_Bet" -> o.Min_Bet,
    "Max_Bet" -> o.Max_Bet,
    "Min_SideBet" -> o.Min_SideBet,
    "Max_SideBet" -> o.Max_SideBet,
    "Min_Tie" -> o.Min_Tie,
    "Max_Tie" -> o.Max_Tie,
  )

  implicit val tableLimitReads: Reads[TableBetLimit] = (
    (JsPath \ "Min_Bet").read[Int] and
      (JsPath \ "Max_Bet").read[Int] and
      (JsPath \ "Min_SideBet").read[Int] and
      (JsPath \ "Max_SideBet").read[Int] and
      (JsPath \ "Min_Tie").read[Int] and
      (JsPath \ "Max_Tie").read[Int]
    ) (TableBetLimit.apply _)


  implicit val configDataWrites: Writes[ConfigData] = (o: ConfigData) => Json.obj(
    "tableLimit" -> o.tableLimit,
    "tableName" -> o.tableName,
    "tableDifferential" -> o.tableDifferential,
    "showInfoPaper" -> o.showInfoPaper,
    "autoDraw" -> o.autoDraw,
    "autoPlay" -> o.autoPlay,
    "isOppositeBettingAllowed" -> o.isOppositeBettingAllowed,
    "isSuitedTieBetEnabled" -> o.isSuitedTieBetEnabled,
    "isBetIntentStatisticsEnabled" -> o.isBetIntentStatisticsEnabled,
  )

  implicit val configDataReads: Reads[ConfigData] = (
    (JsPath \ "tableLimit").read[TableBetLimit] and
      (JsPath \ "tableName").read[String] and
      (JsPath \ "tableDifferential").read[Int] and
      ((JsPath \ "showInfoPaper").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "autoDraw").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "autoPlay").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "isOppositeBettingAllowed").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "isSuitedTieBetEnabled").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "isBetIntentStatisticsEnabled").read[Boolean] or Reads.pure(false))
    ) (ConfigData.apply _)

  /* Admin Configuration Support End... */

  implicit val gameCardWrites: Writes[GameCard] = new Writes[GameCard] {
    override def writes(o: GameCard): JsValue = Json.obj(
      "CardName" -> o.CardName,
      "CardValue" -> o.CardValue,
      "squeezed" -> o.squeezed
    )
  }

  implicit val gameCardReads: Reads[GameCard] = (
    (JsPath \ "CardName").read[String] and
      (JsPath \ "CardValue").read[Int] and
      (JsPath \ "squeezed").read[Boolean]
    ) (GameCard.apply _)

  implicit val seatDataReads: Reads[Seat] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "ip").read[String] and
      (JsPath \ "balance").read[Double] and
      (JsPath \ "uid").read[String] and
      (JsPath \ "connected").read[Boolean] and
      (JsPath \ "gameStatus").read[String]
    ) (Seat.apply _)


  implicit val seatDataWrites: Writes[Seat] = (seatData: Seat) => Json.obj(
    "id" -> seatData.id,
    "name" -> seatData.name,
    "ip" -> seatData.ip,
    "balance" -> seatData.balance,
    "uid" -> seatData.uid,
    "connected" -> seatData.connected,
    "gameStatus" -> seatData.gameStatus,
  )

  implicit val winResultWrites: Writes[WinResult] = (winResult: WinResult) => Json.obj(
    "roundId" -> winResult.roundId,
    "isPlayerPair" -> winResult.isPlayerPair,
    "isBankerPair" -> winResult.isBankerPair,
    "isNaturalHand" -> winResult.isNaturalHand,
    "isSuitedTie" -> winResult.isSuitedTie,
    "BankerCards" -> winResult.BankerCards,
    "PlayerCards" -> winResult.PlayerCards,
    "playerHandValue" -> winResult.playerHandValue,
    "bankerHandValue" -> winResult.bankerHandValue,
    "CardHandValue" -> winResult.CardHandValue,
    "winningHand" -> winResult.WinningHand
  )

  implicit val winResultReads: Reads[WinResult] = (
    (JsPath \ "roundId").read[Long] and
      (JsPath \ "isPlayerPair").read[Boolean] and
      (JsPath \ "isBankerPair").read[Boolean] and
      (JsPath \ "isNaturalHand").read[Boolean] and
      (JsPath \ "isSuitedTie").read[Boolean] and
      (JsPath \ "BankerCards").read[Seq[GameCard]] and
      (JsPath \ "playerCards").read[Seq[GameCard]] and
      (JsPath \ "playerHandValue").read[Int] and
      (JsPath \ "bankerHandValue").read[Int] and
      (JsPath \ "CardHandValue").read[Int] and
      (JsPath \ "WinningHand").read[String]
    ) (WinResult.apply _)

  implicit val tableStateReads: Reads[TableState] = (
    (JsPath \ "roundId").read[Long] and
      (JsPath \ "WinningHand").read[String] and
      (JsPath \ "CardHandValue").read[Int] and
      (JsPath \ "playerHandValue").read[Int] and
      (JsPath \ "bankerHandValue").read[Int] and
      (JsPath \ "isPlayerPair").read[Boolean] and
      (JsPath \ "isBankerPair").read[Boolean] and
      (JsPath \ "isSuitedTie").read[Boolean] and
      (JsPath \ "isNaturalHand").read[Boolean] and
      (JsPath \ "BankerCards").read[Seq[GameCard]] and
      (JsPath \ "PlayerCards").read[Seq[GameCard]] and
      (JsPath \ "configData").read[ConfigData] and
      (JsPath \ "History").read[Seq[WinResult]] and
      (JsPath \ "licenseData").read[LicenseData] and

      (JsPath \ "gameStatus").read[String] and
      (JsPath \ "winCards").read[List[String]] and
      (JsPath \ "gameCards").read[Array[List[String]]] and
      (JsPath \ "waitingCounter").read[Int] and
      (JsPath \ "bettingCounter").read[Int] and
      (JsPath \ "drawCardCounter").read[Int] and
      (JsPath \ "turn").read[(Int, Int)] and
      (JsPath \ "seats").read[Seq[Seat]]
    ) (TableState.apply _)

  implicit val tableStateWrites: Writes[TableState] = (o: TableState) => Json.obj(
    "roundId" -> o.roundId,
    "WinningHand" -> o.WinningHand,
    "CardHandValue" -> o.CardHandValue,
    "playerHandValue" -> o.playerHandValue,
    "bankerHandValue" -> o.bankerHandValue,
    "isPlayerPair" -> o.isPlayerPair,
    "isBankerPair" -> o.isBankerPair,
    "isSuitedTie" -> o.isSuitedTie,
    "isNaturalHand" -> o.isNaturalHand,
    "BankerCards" -> o.BankerCards,
    "PlayerCards" -> o.PlayerCards,
    "configData" -> o.configData,
    "History" -> o.History,
    "licenseData" -> o.licenseData,

    "gameStatus" -> o.gameStatus,
    "winCards" -> o.winCards,
    "gameCards" -> o.gameCards,
    "waitingCounter" -> o.waitingCounter,
    "bettingCounter" -> o.bettingCounter,
    "drawCardCounter" -> o.drawCardCounter,
    "turn" -> o.turn,
    "seats" -> o.seats,
  )

}

class BaccaratDao(playerDao: BaccaratPlayerDao) extends JsonStrings with BaccaratDaoCodecs {

  val log: Logger = Logger(this.getClass)

  /*########################################### TableState ###############################################################*/
  /*Check whether the table state backup files exists?*/
  if (!os.exists(gameDataPath)) {
    val usersBalanceMap: Map[String, Double] = playerDao.players.map(user => user.uid -> user.balance).toMap
    val usersIpMap: Map[String, String] = playerDao.players.map(user => user.uid -> user.clientIp).toMap
    val usersNameMap: Map[String, String] = playerDao.players.map(user => user.uid -> user.nickname).toMap

    log.info(s"Backup Game Missing, creating a fresh one..")

    os.write.over(
      gameDataPath,
      Json.prettyPrint(
        Json.toJson(
          TableState(seats = Seq(
            Seat(id = 0, uid = "1", balance = usersBalanceMap.getOrElse("1", 0), ip = usersIpMap.getOrElse("1", "192.168.1.2"), name = usersNameMap.getOrElse("1", "P1")),
            Seat(id = 1, uid = "2", balance = usersBalanceMap.getOrElse("2", 0), ip = usersIpMap.getOrElse("2", "192.168.1.3"), name = usersNameMap.getOrElse("2", "P2")),
            Seat(id = 2, uid = "3", balance = usersBalanceMap.getOrElse("3", 0), ip = usersIpMap.getOrElse("3", "192.168.1.4"), name = usersNameMap.getOrElse("3", "P3")),
            Seat(id = 3, uid = "4", balance = usersBalanceMap.getOrElse("4", 0), ip = usersIpMap.getOrElse("4", "192.168.1.5"), name = usersNameMap.getOrElse("4", "P4")),
            Seat(id = 4, uid = "5", balance = usersBalanceMap.getOrElse("5", 0), ip = usersIpMap.getOrElse("5", "192.168.1.6"), name = usersNameMap.getOrElse("5", "P5")),
            Seat(id = 5, uid = "6", balance = usersBalanceMap.getOrElse("6", 0), ip = usersIpMap.getOrElse("6", "192.168.1.7"), name = usersNameMap.getOrElse("6", "P6")),
            Seat(id = 6, uid = "7", balance = usersBalanceMap.getOrElse("7", 0), ip = usersIpMap.getOrElse("7", "192.168.1.8"), name = usersNameMap.getOrElse("7", "P7")),
            Seat(id = 7, uid = "8", balance = usersBalanceMap.getOrElse("8", 0), ip = usersIpMap.getOrElse("8", "192.168.1.9"), name = usersNameMap.getOrElse("8", "P8")))
          )
        )
      ),
      createFolders = true
    )
  }
  val dataJson: JsValue = Json.parse(os.read(gameDataPath))
  var gameData: TableState = dataJson.validate[TableState].fold(
    invalid = { fieldErrors =>
      log.info(s"Backup Game Data Read failed..")
      fieldErrors.foreach { x =>
        log.info(s"field: ${x._1}, errors: ${x._2}")
      }

      val usersBalanceMap: Map[String, Double] = playerDao.players.map(user => user.uid -> user.balance).toMap
      val usersIpMap: Map[String, String] = playerDao.players.map(user => user.uid -> user.clientIp).toMap
      val usersNameMap: Map[String, String] = playerDao.players.map(user => user.uid -> user.nickname).toMap

      log.info(s"Backup Data Read Error, creating a fresh one..")

      val freshTableState = TableState(seats = Seq(
        Seat(id = 0, uid = "1", balance = usersBalanceMap.getOrElse("1", 0), ip = usersIpMap.getOrElse("1", "192.168.1.2"), name = usersNameMap.getOrElse("1", "P1")),
        Seat(id = 1, uid = "2", balance = usersBalanceMap.getOrElse("2", 0), ip = usersIpMap.getOrElse("2", "192.168.1.3"), name = usersNameMap.getOrElse("2", "P2")),
        Seat(id = 2, uid = "3", balance = usersBalanceMap.getOrElse("3", 0), ip = usersIpMap.getOrElse("3", "192.168.1.4"), name = usersNameMap.getOrElse("3", "P3")),
        Seat(id = 3, uid = "4", balance = usersBalanceMap.getOrElse("4", 0), ip = usersIpMap.getOrElse("4", "192.168.1.5"), name = usersNameMap.getOrElse("4", "P4")),
        Seat(id = 4, uid = "5", balance = usersBalanceMap.getOrElse("5", 0), ip = usersIpMap.getOrElse("5", "192.168.1.6"), name = usersNameMap.getOrElse("5", "P5")),
        Seat(id = 5, uid = "6", balance = usersBalanceMap.getOrElse("6", 0), ip = usersIpMap.getOrElse("6", "192.168.1.7"), name = usersNameMap.getOrElse("6", "P6")),
        Seat(id = 6, uid = "7", balance = usersBalanceMap.getOrElse("7", 0), ip = usersIpMap.getOrElse("7", "192.168.1.8"), name = usersNameMap.getOrElse("7", "P7")),
        Seat(id = 7, uid = "8", balance = usersBalanceMap.getOrElse("8", 0), ip = usersIpMap.getOrElse("8", "192.168.1.9"), name = usersNameMap.getOrElse("8", "P8")))
      )

      os.write.over(
        gameDataPath,
        Json.prettyPrint(
          Json.toJson(
            freshTableState
          )
        ),
        createFolders = true
      )

      freshTableState

    },
    valid = { data =>
      log.info(s"Backup Game Data Read Success..")
      data
    }
  )

  def getGameData(): TableState = gameData

  def setGameData(data: TableState): Unit = {
    gameData = data

    os.write.over(
      gameDataPath,
      Json.prettyPrint(Json.toJson(data)),
      createFolders = true
    )
  }
  /*########################################### TableState ###############################################################*/

  /*######################################### LicenseData ################################################################*/
  /*Check whether a license file exists */
  if (!os.exists(licenceDataPath)) {
    log.error("License File Missing :-(")
    os.write.over(
      licenceDataPath,
      Json.prettyPrint((
        Json.toJson(
          LicenseData(name = "Baccarat", client = "tykhe", install = "01-01-1970", productCode = "???", profitCode = "???")
        )
      )),
      createFolders = true
    )

  }
  val licenseJson = Json.parse(os.read(licenceDataPath))
  var licenseData = licenseJson.validate[LicenseData].fold(
    invalid = {fieldsErrors =>
      log.error("License Read Failed")
      fieldsErrors.foreach { x =>
        log.info(s"field: ${x._1}, errors: ${x._2}")
      }
      os.write.over(
        licenceDataPath,
        Json.prettyPrint((
          Json.toJson(
            LicenseData(name = "Baccarat", client = "BigB", install = "10-02-2023").copy(macs = getMacAddresses)
          )
          )),
        createFolders = true
      )

      LicenseData(name = "Baccarat", client = "BigB", install = "10-02-2023").copy(macs = getMacAddresses)

    },
    valid = {licenseData =>
      log.info("License Read Success..")

      log.info(s"${getMacAddresses.mkString(start ="Mac List(", ", ", end = ")")}")

      licenseData.copy(macs = getMacAddresses)
    }
  )


  def getLicenseData(): LicenseData = licenseData

  def getMacAddresses = NetworkInterface.networkInterfaces()
    .filter(x => x.getHardwareAddress != null)
    .map(x => x.getHardwareAddress.map(x => String.format("%02X", x)).mkString("", ":", ""))
    .toArray
    .toList
    .map(_.toString)


  def setLicenseData(data: LicenseData) = {
    licenseData = data
    os.write.over(
      licenceDataPath,
      Json.prettyPrint(Json.toJson(data)),
      createFolders = true
    )
  }
  /*######################################### LicenseData ################################################################*/


  val definitions: JsValue = Json.parse(definitionsJsonString)
  val initialConfig: JsValue = Json.parse(initialConfigString)
  val game: JsValue = Json.parse(gameJsonString)
  val gameTypes: JsValue = Json.parse(gameTypesString)
  val groups: JsValue = Json.parse(groupsString)
  val locale: JsValue = Json.parse(localeString)
  val tables: JsValue = Json.parse(tablesString)
  val userData: JsValue = Json.parse(userDataString)
  val videoStreams: JsValue = Json.parse(videoStreamsString)
  var authenticationParams: JsValue = Json.parse(authenticationParamsJsonString)
  var tableLimits: JsValue = Json.parse(tableLimitsString)

  def getInitialDataJsonString: String = {
    Json.prettyPrint(
      Json.obj(
        "errorCode" -> 0,
        "skin" -> "legacy",
        "language" -> "en",
        "boAuthenticated" -> false,
        "initialAuthenticationTarget" -> "lobby",
        "authErros" -> Json.obj(),
        "authenticationParams" -> authenticationParams,
        "definitions" -> definitions,
        "game" -> game,
        "gameTypes" -> gameTypes,
        "initialConfig" -> initialConfig,
        "locale" -> locale,
      )
    )
  }

  def authenticateJsonString: String = {
    Json.prettyPrint(
      Json.obj(
        "error_code" -> 0,
        "error_message" -> "",
        "result" -> Json.obj(
          "authentication_params" -> authenticationParams,
          "config" -> initialConfig,
          "definitions" -> definitions,
          "favorite" -> Json.arr(),
          "game_types" -> gameTypes,
          "groups" -> groups,
          "language" -> "en",
          "locale" -> locale,
          "recent" -> Json.arr(),
          "tables" -> tables,
          "user_data" -> userData
        )
      )
    )
  }

  def sendStreamsJsonString: String = {
    Json.prettyPrint(
      videoStreams
    )
  }

  def tableLimitsString: String = os.read(os.pwd / "conf" / "jsons" / "config" / "baccarat" / "table_limits.json")

  def getTableLimits(tableId: String, limitId: Int): JsValue = {

    tableLimits
  }

  def setTableLimits(tableId: String, limitId: Int, tableLimit_i: JsValue) = {
    tableLimits = tableLimit_i

    os.write.over(
      os.pwd / "conf" / "jsons" / "config" / "baccarat" / "table_limits.json",
      Json.prettyPrint(tableLimit_i),
      createFolders = true
    )
  }





  def reloadGameData(): Unit = {
    log.info(s"Reloading Game Data..")

    val dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss.SSS z")

    val usersBalanceMap: Map[String, Double] = playerDao.players.map(user => user.uid -> user.balance).toMap
    val usersIpMap: Map[String, String] = playerDao.players.map(user => user.uid -> user.clientIp).toMap
    val usersNameMap: Map[String, String] = playerDao.players.map(user => user.uid -> user.nickname).toMap


    val freshTableState = TableState(seats = Seq(
      Seat(id = 0, uid = "1", balance = usersBalanceMap.getOrElse("1", 0), ip = usersIpMap.getOrElse("1", "192.168.1.2"), name = usersNameMap.getOrElse("1", "P1")),
      Seat(id = 1, uid = "2", balance = usersBalanceMap.getOrElse("2", 0), ip = usersIpMap.getOrElse("2", "192.168.1.3"), name = usersNameMap.getOrElse("2", "P2")),
      Seat(id = 2, uid = "3", balance = usersBalanceMap.getOrElse("3", 0), ip = usersIpMap.getOrElse("3", "192.168.1.4"), name = usersNameMap.getOrElse("3", "P3")),
      Seat(id = 3, uid = "4", balance = usersBalanceMap.getOrElse("4", 0), ip = usersIpMap.getOrElse("4", "192.168.1.5"), name = usersNameMap.getOrElse("4", "P4")),
      Seat(id = 4, uid = "5", balance = usersBalanceMap.getOrElse("5", 0), ip = usersIpMap.getOrElse("5", "192.168.1.6"), name = usersNameMap.getOrElse("5", "P5")),
      Seat(id = 5, uid = "6", balance = usersBalanceMap.getOrElse("6", 0), ip = usersIpMap.getOrElse("6", "192.168.1.7"), name = usersNameMap.getOrElse("6", "P6")),
      Seat(id = 6, uid = "7", balance = usersBalanceMap.getOrElse("7", 0), ip = usersIpMap.getOrElse("7", "192.168.1.8"), name = usersNameMap.getOrElse("7", "P7")),
      Seat(id = 7, uid = "8", balance = usersBalanceMap.getOrElse("8", 0), ip = usersIpMap.getOrElse("8", "192.168.1.9"), name = usersNameMap.getOrElse("8", "P8")))
    )

    gameData = freshTableState

    os.copy(
      gameDataPath,
      os.pwd / "jsons" / "baccarat" / s"game_data_${dateFormat.format(Calendar.getInstance().getTime)}.json",
      replaceExisting = true
    )

    os.write.over(
      gameDataPath,
      Json.prettyPrint(Json.toJson(freshTableState)),
      createFolders = true
    )
  }

}
