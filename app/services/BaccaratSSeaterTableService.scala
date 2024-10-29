package services

import actors.baccarat.BaccaratTableActor
import actors.baccarat.BaccaratTableActor.GuestPlayerConnected
import akka.actor.{ActorRef, ActorSystem}
import dao.{BaccaratDao, BaccaratPlayerDao}
import model.baccarat.data.{LicenseData, TableData, TableLimit, TableState}
import model.common.messages._
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsError, JsPath, Json, Reads, Writes}
import play.api.mvc.Request
import play.api.mvc.Results.BadRequest

class BaccaratSSeaterTableService(actorSystem: ActorSystem,
                                  gameService: GameService,
                                  playerDao: BaccaratPlayerDao,
                                  baccaratDao: BaccaratDao) extends ChipCodec {

  val log: Logger = Logger(this.getClass)


  implicit val tableLimitReads: Reads[TableLimit] = (
    (JsPath \ "chips").read[Seq[Chip]] and
      (JsPath \ "Min_Bet").read[Int] and
      (JsPath \ "Max_Bet").read[Int] and
      (JsPath \ "Min_SideBet").read[Int] and
      (JsPath \ "Max_SideBet").read[Int] and
      (JsPath \ "Min_StraightUpBet").read[Int] and
      (JsPath \ "Max_StraightUpBet").read[Int] and
      (JsPath \ "Min_SplitBet").read[Int] and
      (JsPath \ "Max_SplitBet").read[Int] and
      (JsPath \ "Min_StreetBet").read[Int] and
      (JsPath \ "Max_StreetBet").read[Int] and
      (JsPath \ "Min_CornerBet").read[Int] and
      (JsPath \ "Max_CornerBet").read[Int] and
      (JsPath \ "Min_LineBet").read[Int] and
      (JsPath \ "Max_LineBet").read[Int] and
      (JsPath \ "Min_Dozen_ColumnBet").read[Int] and
      (JsPath \ "Max_Dozen_ColumnBet").read[Int] and
      (JsPath \ "Min_OutsideBet").read[Int] and
      (JsPath \ "Max_OutsideBet").read[Int] and
      (JsPath \ "Min_Tie").read[Int] and
      (JsPath \ "Max_Tie").read[Int]
    ) (TableLimit.apply _)

  implicit val tableLimitWrites: Writes[TableLimit] = (
    (JsPath \ "chips").write[Seq[Chip]] and
      (JsPath \ "Min_Bet").write[Int] and
      (JsPath \ "Max_Bet").write[Int] and
      (JsPath \ "Min_SideBet").write[Int] and
      (JsPath \ "Max_SideBet").write[Int] and
      (JsPath \ "Min_StraightUpBet").write[Int] and
      (JsPath \ "Max_StraightUpBet").write[Int] and
      (JsPath \ "Min_SplitBet").write[Int] and
      (JsPath \ "Max_SplitBet").write[Int] and
      (JsPath \ "Min_StreetBet").write[Int] and
      (JsPath \ "Max_StreetBet").write[Int] and
      (JsPath \ "Min_CornerBet").write[Int] and
      (JsPath \ "Max_CornerBet").write[Int] and
      (JsPath \ "Min_LineBet").write[Int] and
      (JsPath \ "Max_LineBet").write[Int] and
      (JsPath \ "Min_Dozen_ColumnBet").write[Int] and
      (JsPath \ "Max_Dozen_ColumnBet").write[Int] and
      (JsPath \ "Min_OutsideBet").write[Int] and
      (JsPath \ "Max_OutsideBet").write[Int] and
      (JsPath \ "Min_Tie").write[Int] and
      (JsPath \ "Max_Tie").write[Int]
    ) (unlift(TableLimit.unapply))

  var actorBaccaratTable: ActorRef = _

  def init(): Unit = {
    actorBaccaratTable = actorSystem.actorOf(BaccaratTableActor.props(gameService = gameService, baccaratSSeaterTableService = this), BaccaratTableActor.name)
  }

  def getBaccaratTableActor: ActorRef = actorBaccaratTable

  def getInitialDataJsonString: String = baccaratDao.getInitialDataJsonString

  def authenticateJsonString: String = baccaratDao.authenticateJsonString

  def sendStreamsJsonString: String = baccaratDao.sendStreamsJsonString

  def sendTableLimitsJsonString: String = {
    val limits = baccaratDao.getTableLimits(tableId = "32100", limitId = 992712 )
    Json.prettyPrint(
      limits
    )
  }


  def saveTableLimits(tableLimit: TableLimit) = {
    baccaratDao.setTableLimits(tableId = "32100", limitId = 992712, Json.toJson(tableLimit))
  }

  def getTableLimits(tableId: String, limitId: Int): TableLimit = {
    val limits = baccaratDao.getTableLimits(tableId, limitId)
    limits.as[TableLimit]
  }

  def getTableData(tableId: String): TableData = baccaratDao.getGameData().toTableData

  def getTableState(tableId: String): TableState = baccaratDao.getGameData()

  def getLicenseData: LicenseData = baccaratDao.getLicenseData()

  def getTransactions: Seq[MoneyTransactionMsg] = playerDao.getTransactionsData()

  def addTransaction(transaction: MoneyTransactionMsg): Unit = {
    playerDao.addTransaction(transaction)
  }

  def getOperationTransactions: Seq[OperationTransactionMsg] = playerDao.getOperationTransactionsData()

  def addOperationTransaction(transaction: OperationTransactionMsg): Unit = {
    playerDao.addOperationTransaction(transaction)
  }

  //TODO do table id specific dao action
  def getPlayersData(tableId: String): Seq[Player] = playerDao.getPlayersData

  def getPlayerData(player: String): Player = {
    val playerData = playerDao.getPlayerData(player)
    if (playerData.nickname == "Guest") {
      val updatedPlayerData = playerData.copy(nickname = s"Guest${player}");
      playerDao.addPlayer(updatedPlayerData)
      actorBaccaratTable ! GuestPlayerConnected(updatedPlayerData)
      log.logger.warn(s"Guest Player Allowed to play ${updatedPlayerData}")
      updatedPlayerData
    } else playerData
  }

  def createPlayer(player: Player): Seq[Player] = playerDao.addPlayer(player) //Used only for Guest Connection case but uid is still -1!!

  def updatePlayerData(player: Player, uid: String): Unit = playerDao.updatePlayer(player, uid)


  def setTableData(tableId: String, data: TableState): Unit = {
    tableId match {
      case "4000" => baccaratDao.setGameData(data)
      case _ =>
    }
  }

  def reloadTableState(tableId: String): Unit = {
    tableId match {
      case "4000" => baccaratDao.reloadGameData()
      case _ =>
    }
  }


  def saveAllData(): Unit = {
    playerDao.setTransactions();
    playerDao.setOperationTransactions();
  }

}
