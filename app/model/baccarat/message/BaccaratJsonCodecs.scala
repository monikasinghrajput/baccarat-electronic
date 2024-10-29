package model.baccarat.message

import model.common.messages.{CurrentBalanceMessage, MoneyTransactionMsg, OperationTransactionMsg, Player, PlayerCreatedMsg, PlayerUpdatedMsg, ServerLog}
import model.baccarat.data._

trait BaccaratJsonCodecs {


  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  /* For Admin Configuration Support for
  * ConfigData
  * 1.
  * 2.
  * 3.
  *  */
  implicit val configUpdateMsgWrites: Writes[ConfigUpdateMsg] = new Writes[ConfigUpdateMsg] {
    def writes(configUpdateMsg: ConfigUpdateMsg): JsValue = Json.obj(
      "MessageType" -> configUpdateMsg.MessageType,
      "configData" -> configUpdateMsg.configData,
      "timestamp" -> configUpdateMsg.timestamp,
    )
  }
  implicit val initialConfigMsgWrites: Writes[InitialConfigMsg] = new Writes[InitialConfigMsg] {
    def writes(initialConfigMsg: InitialConfigMsg): JsValue = Json.obj(
      "MessageType" -> initialConfigMsg.MessageType,
      "configData" -> initialConfigMsg.configData,
      "timestamp" -> initialConfigMsg.timestamp,
    )
  }

  implicit val tableBetLimitWrites: Writes[TableBetLimit] = new Writes[TableBetLimit] {
    override def writes(o: TableBetLimit): JsValue = Json.obj(
      "Min_Bet" -> o.Min_Bet,
      "Max_Bet" -> o.Max_Bet,
      "Min_SideBet" -> o.Min_SideBet,
      "Max_SideBet" -> o.Max_SideBet,
      "Min_Tie" -> o.Min_Tie,
      "Max_Tie" -> o.Max_Tie,
    )
  }

  implicit val tableLimitReads: Reads[TableBetLimit] = (
    (JsPath \ "Min_Bet").read[Int] and
      (JsPath \ "Max_Bet").read[Int] and
      (JsPath \ "Min_SideBet").read[Int] and
      (JsPath \ "Max_SideBet").read[Int] and
      (JsPath \ "Min_Tie").read[Int] and
      (JsPath \ "Max_Tie").read[Int]
    ) (TableBetLimit.apply _)



  implicit val configDataWrites: Writes[ConfigData] = new Writes[ConfigData] {
    override def writes(o: ConfigData): JsValue = Json.obj(
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
  }

  implicit val configDataReads: Reads[ConfigData] = (
    (JsPath \ "tableLimit").read[TableBetLimit] and
      (JsPath \ "tableName").read[String] and
      (JsPath \ "tableDifferential").read[Int] and
      (JsPath \ "showInfoPaper").read[Boolean] and
      ((JsPath \ "autoDraw").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "autoPlay").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "isOppositeBettingAllowed").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "isSuitedTieBetEnabled").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "isBetIntentStatisticsEnabled").read[Boolean] or Reads.pure(false))
    ) (ConfigData.apply _)

  implicit val squeezedCardWrites: Writes[SqueezedCard] = new Writes[SqueezedCard] {
    override def writes(o: SqueezedCard): JsValue = Json.obj(
      "hand" -> o.hand,
      "index" -> o.index,
    )
  }

  implicit val squeezedCardReads: Reads[SqueezedCard] = (
    (JsPath \ "hand").read[Int] and
      (JsPath \ "index").read[Int]
    ) (SqueezedCard.apply _)

  /* Admin Configuration Support End... */

  implicit val shuffleMsgWrites: Writes[ShuffleMsg] = new Writes[ShuffleMsg] {
    def writes(shuffleMsg: ShuffleMsg): JsValue = Json.obj(
      "MessageType" -> shuffleMsg.MessageType,
      "destination" -> shuffleMsg.destination,
      "clientId" -> shuffleMsg.clientId,
      "roundId" -> shuffleMsg.roundId,
      "timestamp" -> shuffleMsg.timestamp,
    )
  }


  implicit val currentBalanceMsgWrites: Writes[CurrentBalanceMessage] = new Writes[CurrentBalanceMessage] {
    def writes(currentBalanceMsg: CurrentBalanceMessage): JsValue = Json.obj(
      "MessageType" -> currentBalanceMsg.MessageType,
      "TableId" -> currentBalanceMsg.tableId,
      "destination" -> currentBalanceMsg.destination,
      "ClientId" -> currentBalanceMsg.clientId,
      "roundId" -> currentBalanceMsg.roundId,
      "gameType" -> currentBalanceMsg.gameType,
      "RoundTripStartTime" -> currentBalanceMsg.roundTripStartTime,
      "timestamp" -> currentBalanceMsg.timestamp,
      "balance" -> currentBalanceMsg.balance,
      "SessionCurrency" -> currentBalanceMsg.sessionCurrency
    )
  }

  implicit val playerUpdateWrites: Writes[PlayerUpdatedMsg] = new Writes[PlayerUpdatedMsg] {
    def writes(playerUpdatedMsg: PlayerUpdatedMsg): JsValue = Json.obj(
      "MessageType" -> playerUpdatedMsg.MessageType,
      "player" -> playerUpdatedMsg.player,
      "timestamp" -> playerUpdatedMsg.timestamp,
    )
  }

  implicit val playerCreatedWrites: Writes[PlayerCreatedMsg] = new Writes[PlayerCreatedMsg] {
    def writes(playerCreatedMsg: PlayerCreatedMsg): JsValue = Json.obj(
      "MessageType" -> playerCreatedMsg.MessageType,
      "player" -> playerCreatedMsg.player,
      "timestamp" -> playerCreatedMsg.timestamp
    )
  }


  /* License*/
  implicit val licenseDataWrites: Writes[LicenseData] = (licenseData: LicenseData) => Json.obj(
    "name" -> licenseData.name,
    "client" -> licenseData.client,
    "install" -> licenseData.install,
    "macs" -> licenseData.macs,
    "validProductCode" -> licenseData.validProductCode,
    "validProfitCode" -> licenseData.validProfitCode,
    "toBeExpired" -> licenseData.toBeExpired,
    "profitCode" -> licenseData.profitCode,
    "productCode" -> licenseData.productCode
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
    ) (LicenseData.apply _)

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

  implicit val tableStateWrites: Writes[TableState] = new Writes[TableState] {
    override def writes(o: TableState): JsValue = Json.obj(
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

  implicit val seatDataReads: Reads[Seat] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "ip").read[String] and
      (JsPath \ "balance").read[Double] and
      (JsPath \ "uid").read[String] and
      (JsPath \ "connected").read[Boolean] and
      (JsPath \ "gameStatus").read[String]
    ) (Seat.apply _)


  implicit val seatDataWrites: Writes[Seat] = new Writes[Seat] {
    def writes(seatData: Seat): JsValue = Json.obj(
      "id" -> seatData.id,
      "name" -> seatData.name,
      "ip" -> seatData.ip,
      "balance" -> seatData.balance,
      "uid" -> seatData.uid,
      "connected" -> seatData.connected,
      "gameStatus" -> seatData.gameStatus,
    )
  }

  /* Bet Related Codec Start
  *
  * Main Bets
  * - PlayerBet
  * - TieBet
  * - BankerBet
  * - SideBet
  *   - PlayerPair
  *   - BankerPair
  *   - PerfectPair
  *   - EitherPair
  *   - Small
  *   - Big
  *
  * */


  implicit val wonBetsMsgWrites: Writes[WonBetsMsg] = new Writes[WonBetsMsg] {
    override def writes(o: WonBetsMsg): JsValue = Json.obj(
      "MessageType" -> o.MessageType,
      "destination" -> o.destination,
      "clientId" -> o.clientId,
      "RoundTripStartTime" -> o.RoundTripStartTime,
      "WinningBets" -> o.WinningBets,
    )
  }

  implicit val sideBetWrite: Writes[SideBet] = new Writes[SideBet] {
    override def writes(o: SideBet): JsValue = Json.obj(
      "PlayerPair" -> o.PlayerPair,
      "BankerPair" -> o.BankerPair,
      "PerfectPair" -> o.PerfectPair,
      "EitherPair" -> o.EitherPair,
    )
  }

  implicit val sideBetReads: Reads[SideBet] = (
    ((JsPath \ "PlayerPair").read[Int] or Reads.pure(0) ) and
      ((JsPath \ "BankerPair").read[Int] or Reads.pure(0)) and
      ((JsPath \ "PerfectPair").read[Int] or Reads.pure(0)) and
      ((JsPath \ "EitherPair").read[Int] or Reads.pure(0))
    ) (SideBet.apply _)



  implicit val betsListWrites: Writes[BetsList] = new Writes[BetsList] {
    override def writes(o: BetsList): JsValue = Json.obj(
      "TieBet" -> o.TieBet,
      "BankerBet" -> o.BankerBet,
      "PlayerBet" -> o.PlayerBet,
      "SideBets" -> o.SideBets
    )
  }

  implicit val betsListReads: Reads[BetsList] = (
    ((JsPath \ "TieBet").read[Int] or Reads.pure(0)) and
      ((JsPath \ "BankerBet").read[Int] or Reads.pure(0)) and
      ((JsPath \ "PlayerBet").read[Int] or Reads.pure(0)) and
      ((JsPath \ "SideBets").read[SideBet] or Reads.pure(SideBet()))
    ) (BetsList.apply _)

  /* Bet Related Codec End*/

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

  implicit val winResultWrites: Writes[WinResult] = new Writes[WinResult] {
    def writes(winResult: WinResult): JsValue = Json.obj(
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
      "WinningHand" -> winResult.WinningHand
    )
  }

  implicit val winResultReads: Reads[WinResult] = (
    (JsPath \ "roundId").read[Long] and
      (JsPath \ "isPlayerPair").read[Boolean] and
      (JsPath \ "isBankerPair").read[Boolean] and
      (JsPath \ "isNaturalHand").read[Boolean] and
      (JsPath \ "isSuitedTie").read[Boolean] and
      (JsPath \ "BankerCards").read[Seq[GameCard]] and
      (JsPath \ "PlayerCards").read[Seq[GameCard]] and
      (JsPath \ "playerHandValue").read[Int] and
      (JsPath \ "bankerHandValue").read[Int] and
      (JsPath \ "CardHandValue").read[Int] and
      (JsPath \ "WinningHand").read[String]
    ) (WinResult.apply _)

  implicit val dataWrites: Writes[Data] = new Writes[Data] {
    def writes(data: Data): JsValue = Json.obj(
      "History" -> data.History,
      "ColdNumbers" -> data.ColdNumbers,
      "HotNumbers" -> data.HotNumbers,
      "BankerCards" -> data.BankerCards,
      "PlayerCards" -> data.PlayerCards,
      "bankerHandValue" -> data.bankerHandValue,
      "playerHandValue" -> data.playerHandValue,
      "playerBetOfThisRound" -> data.playerBetOfThisRound
    )
  }

  implicit val dataReads: Reads[Data] = (
    (JsPath \ "History").read[Seq[WinResult]] and
      (JsPath \ "ColdNumbers").read[Seq[Int]] and
      (JsPath \ "HotNumbers").read[Seq[Int]] and
      (JsPath \ "BankerCards").read[Seq[GameCard]] and
      (JsPath \ "PlayerCards").read[Seq[GameCard]] and
      (JsPath \ "bankerHandValue").read[Int] and
      (JsPath \ "playerHandValue").read[Int] and
      (JsPath \ "playerBetOfThisRound").read[BetsList]
    ) (Data.apply _)


  implicit val tableDataUpdatedWrites: Writes[DataUpdatedMsg] = new Writes[DataUpdatedMsg] {
    def writes(tableData: DataUpdatedMsg): JsValue = Json.obj(
      "MessageType" -> tableData.MessageType,
      "data" -> tableData.data,
      "timestamp" -> tableData.timestamp,
    )
  }

  implicit val tableDataUpdatedReads: Reads[DataUpdatedMsg] = (
    (JsPath \ "MessageType").read[String] and
      (JsPath \ "data").read[Data] and
      (JsPath \ "timestamp").read[String]
    ) (DataUpdatedMsg.apply _)


  implicit val initialDataTopperMsgWrites: Writes[InitialDataTopperMsg] = new Writes[InitialDataTopperMsg] {
    def writes(initialDataMsg: InitialDataTopperMsg): JsValue = Json.obj(
      "MessageType" -> initialDataMsg.MessageType,
      "TableId" -> initialDataMsg.tableId,
      "destination" -> initialDataMsg.destination,
      "clientId" -> initialDataMsg.clientId,
      "roundId" -> initialDataMsg.roundId,
      "timestamp" -> initialDataMsg.timestamp,
      "isOppositeBettingAllowed" -> initialDataMsg.isOppositeBettingAllowed,
      "isSuitedTieBetEnabled" -> initialDataMsg.isSuitedTieBetEnabled,
      "isBetIntentStatisticsEnabled" -> initialDataMsg.isBetIntentStatisticsEnabled,
      "data" -> initialDataMsg.data
    )
  }

  implicit val initialDataTopperMsgReads: Reads[InitialDataTopperMsg] = (
    (JsPath \ "MessageType").read[String] and
      (JsPath \ "TableId").read[String] and
      (JsPath \ "destination").read[String] and
      (JsPath \ "clientId").read[String] and
      (JsPath \ "roundId").read[Long] and
      (JsPath \ "timestamp").read[String] and
      (JsPath \ "RoundTripStartTime").read[String] and
      (JsPath \ "isOppositeBettingAllowed").read[Boolean] and
      (JsPath \ "isSuitedTieBetEnabled").read[Boolean] and
      (JsPath \ "isBetIntentStatisticsEnabled").read[Boolean] and
      (JsPath \ "data").read[Data]
    ) (InitialDataTopperMsg.apply _)


  implicit val initialDataPlayerMsgWrites: Writes[InitialDataPlayerMsg] = new Writes[InitialDataPlayerMsg] {
    def writes(initialDataMsg: InitialDataPlayerMsg): JsValue = Json.obj(
      "MessageType" -> initialDataMsg.MessageType,
      "TableId" -> initialDataMsg.tableId,
      "destination" -> initialDataMsg.destination,
      "clientId" -> initialDataMsg.clientId,
      "roundId" -> initialDataMsg.roundId,
      "timestamp" -> initialDataMsg.timestamp,
      "isOppositeBettingAllowed" -> initialDataMsg.isOppositeBettingAllowed,
      "isSuitedTieBetEnabled" -> initialDataMsg.isSuitedTieBetEnabled,
      "isBetIntentStatisticsEnabled" -> initialDataMsg.isBetIntentStatisticsEnabled,
      "data" -> initialDataMsg.data
    )
  }

  implicit val initialDataPlayerMsgReads: Reads[InitialDataPlayerMsg] = (
    (JsPath \ "MessageType").read[String] and
      (JsPath \ "TableId").read[String] and
      (JsPath \ "destination").read[String] and
      (JsPath \ "clientId").read[String] and
      (JsPath \ "roundId").read[Long] and
      (JsPath \ "timestamp").read[String] and
      (JsPath \ "RoundTripStartTime").read[String] and
      (JsPath \ "isOppositeBettingAllowed").read[Boolean] and
      (JsPath \ "isSuitedTieBetEnabled").read[Boolean] and
      (JsPath \ "isBetIntentStatisticsEnabled").read[Boolean] and
      (JsPath \ "data").read[Data]
    ) (InitialDataPlayerMsg.apply _)


  implicit val initialDataAdminMsgWrites: Writes[InitialDataAdminMsg] = new Writes[InitialDataAdminMsg] {
    def writes(initialDataMsg: InitialDataAdminMsg): JsValue = Json.obj(
      "MessageType" -> initialDataMsg.MessageType,
      "TableId" -> initialDataMsg.tableId,
      "destination" -> initialDataMsg.destination,
      "clientId" -> initialDataMsg.clientId,
      "roundId" -> initialDataMsg.roundId,
      "timestamp" -> initialDataMsg.timestamp,
      "data" -> initialDataMsg.data,
      "logs" -> initialDataMsg.logs,
      "players" -> initialDataMsg.players,
      "transactions" -> initialDataMsg.transactions,
      "operations" -> initialDataMsg.operations,
    )
  }

  implicit val initialDataAdminMsgReads: Reads[InitialDataAdminMsg] = (
    (JsPath \ "MessageType").read[String] and
      (JsPath \ "TableId").read[String] and
      (JsPath \ "destination").read[String] and
      (JsPath \ "clientId").read[String] and
      (JsPath \ "roundId").read[Long] and
      (JsPath \ "timestamp").read[String] and
      (JsPath \ "data").read[Data] and
      (JsPath \ "logs").read[Seq[ServerLog]] and
      (JsPath \ "players").read[Seq[Player]] and
      (JsPath \ "transactions").read[Seq[MoneyTransactionMsg]] and
      (JsPath \ "operations").read[Seq[OperationTransactionMsg]]
    ) (InitialDataAdminMsg.apply _)

  implicit val logWrites: Writes[ServerLog] = new Writes[ServerLog] {
    def writes(log: ServerLog): JsValue = Json.obj(
      "logType" -> log.logType,
      "runtimeClass" -> log.runtimeClass,
      "content" -> log.content,
      "timestamp" -> log.timestamp,
    )
  }
  implicit val logReads: Reads[ServerLog] = (
    (JsPath \ "logType").read[String] and
      (JsPath \ "runtimeClass").read[String] and
      (JsPath \ "content").read[String] and
      (JsPath \ "timestamp").read[String]
    ) (ServerLog.apply _)

  implicit val playerWrites: Writes[Player] = new Writes[Player] {
    def writes(player: Player): JsValue = Json.obj(
      "client_ip" -> player.clientIp,
      "client_id" -> player.clientId,
      "session_id" -> player.sessionId,
      "current_player_token" -> player.currentPlayerToken,
      "nickname" -> player.nickname,
      "operator_id" -> player.operatorId,
      "currency" -> player.currency,
      "uid" -> player.uid,
      "vip" -> player.vip,
      "status" -> player.status,
      "usage" -> player.usage,
      "balance" -> player.balance
    )
  }

  implicit val playerReads: Reads[Player] = (
    (JsPath \ "client_ip").read[String] and
      (JsPath \ "client_id").read[String] and
      (JsPath \ "session_id").read[Int] and
      (JsPath \ "current_player_token").read[String] and
      (JsPath \ "nickname").read[String] and
      (JsPath \ "operator_id").read[Int] and
      (JsPath \ "currency").read[String] and
      (JsPath \ "uid").read[String] and
      (JsPath \ "vip").read[Int] and
      (JsPath \ "status").read[String] and
      (JsPath \ "usage").read[String] and
      (JsPath \ "balance").read[Double]
    ) (Player.apply _)

  implicit val moneyTransactionMsgReads: Reads[MoneyTransactionMsg] = (
    (JsPath \ "transType").read[String] and
      (JsPath \ "MessageType").read[String] and
      (JsPath \ "playerIp").read[String] and
      (JsPath \ "rake").read[Double] and
      (JsPath \ "roundId").read[Long] and
      (JsPath \ "amount").read[Double] and
      (JsPath \ "oldBalance").read[Double] and
      (JsPath \ "newBalance").read[Double] and
      (JsPath \ "timestamp").read[String]
    ) (MoneyTransactionMsg.apply _)

  implicit val moneyTransactionMsgWrites: Writes[MoneyTransactionMsg] = new Writes[MoneyTransactionMsg] {
    def writes(moneyTransaction: MoneyTransactionMsg): JsValue = Json.obj(
      "transType" -> moneyTransaction.transType,
      "MessageType" -> moneyTransaction.MessageType,
      "playerIp" -> moneyTransaction.playerIp,
      "rake" -> moneyTransaction.rake,
      "roundId" -> moneyTransaction.roundId,
      "amount" -> moneyTransaction.amount,
      "oldBalance" -> moneyTransaction.oldBalance,
      "newBalance" -> moneyTransaction.newBalance,
      "timestamp" -> moneyTransaction.timestamp,
    )
  }

  implicit val operationTransactionMsgReads: Reads[OperationTransactionMsg] = (
    (JsPath \ "transType").read[String] and
      (JsPath \ "MessageType").read[String] and
      (JsPath \ "uid").read[String] and
      (JsPath \ "client_ip").read[String] and
      (JsPath \ "nickname").read[String] and
      (JsPath \ "status").read[String] and
      (JsPath \ "usage").read[String] and
      (JsPath \ "timestamp").read[String]
    ) (OperationTransactionMsg.apply _)


  implicit val operationTransactionMsgWrites: Writes[OperationTransactionMsg] = new Writes[OperationTransactionMsg] {
    def writes(operationTransaction: OperationTransactionMsg): JsValue = Json.obj(
      "transType" -> operationTransaction.transType,
      "MessageType" -> operationTransaction.MessageType,
      "uid" -> operationTransaction.uid,
      "client_ip" -> operationTransaction.client_ip,
      "nickname" -> operationTransaction.nickname,
      "status" -> operationTransaction.status,
      "usage" -> operationTransaction.usage,
      "timestamp" -> operationTransaction.timestamp,
    )
  }

  implicit val placeYourBetsMsgWrites: Writes[PlaceYourBetsMsg] = (
    (JsPath \ "MessageType").write[String] and
      (JsPath \ "destination").write[String] and
      (JsPath \ "clientId").write[String] and
      (JsPath \ "roundId").write[Long] and
      (JsPath \ "TimerTimeLeft").write[Int] and
      (JsPath \ "TimerTime").write[Int] and
      (JsPath \ "timestamp").write[String]
    ) (unlift(PlaceYourBetsMsg.unapply))

  implicit val noMoreBetsMsgWrites: Writes[NoMoreBetsMsg] = (
    (JsPath \ "MessageType").write[String] and
      (JsPath \ "destination").write[String] and
      (JsPath \ "clientId").write[String] and
      (JsPath \ "roundId").write[Long] and
      (JsPath \ "timestamp").write[String]
    ) (unlift(NoMoreBetsMsg.unapply))


  implicit val cardMsgWrites: Writes[CardMsg] = (
    (JsPath \ "MessageType").write[String] and
      (JsPath \ "destination").write[String] and
      (JsPath \ "clientId").write[String] and
      (JsPath \ "roundId").write[Long] and
      (JsPath \ "CardHand").write[String] and
      (JsPath \ "CardHandValue").write[Int] and
      (JsPath \ "CardName").write[String] and
      (JsPath \ "CardValue").write[Int] and
      (JsPath \ "squeezed").write[Boolean] and
      (JsPath \ "timestamp").write[String]
    ) (unlift(CardMsg.unapply))


  implicit val cardSqueezedMsgWrites: Writes[CardSqueezedMsg] = (
    (JsPath \ "MessageType").write[String] and
      (JsPath \ "destination").write[String] and
      (JsPath \ "clientId").write[String] and
      (JsPath \ "roundId").write[Long] and
      (JsPath \ "CardHand").write[String] and
      (JsPath \ "CardIndex").write[Int] and
      (JsPath \ "timestamp").write[String]
    ) (unlift(CardSqueezedMsg.unapply))



  /* You have to be very careful with the order of fields here!!!!*/
  implicit val gameResultWrites: Writes[GameResult] = (
    (JsPath \ "roundId").write[Long] and
      (JsPath \ "isPlayerPair").write[Boolean] and
      (JsPath \ "isBankerPair").write[Boolean] and
      (JsPath \ "isNaturalHand").write[Boolean] and
      (JsPath \ "isSuitedTie").write[Boolean] and
      (JsPath \ "BankerCards").write[Seq[GameCard]] and
      (JsPath \ "PlayerCards").write[Seq[GameCard]] and
      (JsPath \ "playerHandValue").write[Int] and
      (JsPath \ "bankerHandValue").write[Int] and
      (JsPath \ "CardHandValue").write[Int] and
      (JsPath \ "WinningHand").write[String]
    ) (unlift(GameResult.unapply))

  implicit val gameResultMsgWrites: Writes[GameResultMsg] = (
    (JsPath \ "MessageType").write[String] and
      (JsPath \ "destination").write[String] and
      (JsPath \ "clientId").write[String] and
      (JsPath \ "roundId").write[Long] and
      (JsPath \ "timestamp").write[String] and
      (JsPath \ "WinAmount").write[Int] and
      (JsPath \ "GameResults").write[GameResult]
    ) (unlift(GameResultMsg.unapply))



}

