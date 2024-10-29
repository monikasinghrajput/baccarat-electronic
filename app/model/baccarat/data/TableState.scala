package model.baccarat.data

import akka.actor.ActorRef

import java.time.Instant
import collection.mutable.{Map => MMap}
import model.common.data.{BetCreatedData, BetWonData, PlayerData, WinMessageData}
import model.common.messages.{AdminClientData, CurrentBalanceMessage, GameTransaction, MoneyTransactionMsg, OperationTransactionMsg, Player, ServerLog}
import model.baccarat.message.{BaccaratJsonCodecs, CardMsg, CardSqueezedMsg, ConfigUpdateMsg, GameResultMsg, InitialConfigMsg, InitialDataAdminMsg, InitialDataPlayerMsg, NoMoreBetsMsg, PlaceYourBetsMsg, ShuffleMsg, WonBetsMsg}
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, JsValue, Json, Reads, Writes}

import java.text.SimpleDateFormat

case class TableState(roundId: Long = 1,
                      WinningHand: String = "",
                      CardHandValue: Int = 0,
                      playerHandValue: Int = 0,
                      bankerHandValue: Int = 0,
                      isPlayerPair: Boolean = false,
                      isBankerPair: Boolean = false,
                      isNaturalHand: Boolean = false,
                      isSuitedTie: Boolean = false,
                      BankerCards: Seq[GameCard] = Seq.empty[GameCard],
                      PlayerCards: Seq[GameCard] = Seq.empty[GameCard],
                      configData: ConfigData = ConfigData(tableName = "BAC-"),
                      History: Seq[WinResult] = Seq.empty[WinResult],
                      licenseData: LicenseData = LicenseData(),

                      gameStatus: String = "READY",
                      winCards: List[String] = List("", "", ""),
                      gameCards: Array[List[String]] = Array(List("Tie", ""), List("Player", "", "", "", ""), List("Banker", "", "", "", "")),
                      waitingCounter: Int = 0,
                      bettingCounter: Int = 0,
                      drawCardCounter: Int = 0,
                      turn: (Int, Int) = (1, 1),
                      seats: Seq[Seat] = Seq(
                        Seat(id = 0, uid = "1", balance = 0, ip = "192.168.1.2", name = "P1"),
                        Seat(id = 1, uid = "2", balance = 0, ip = "192.168.1.3", name = "P2"),
                        Seat(id = 2, uid = "3", balance = 0, ip = "192.168.1.4", name = "P3"),
                        Seat(id = 3, uid = "4", balance = 0, ip = "192.168.1.5", name = "P4"),
                        Seat(id = 4, uid = "5", balance = 0, ip = "192.168.1.6", name = "P5"),
                        Seat(id = 5, uid = "6", balance = 0, ip = "192.168.1.7", name = "P6"),
                        Seat(id = 6, uid = "7", balance = 0, ip = "192.168.1.8", name = "P7"),
                        Seat(id = 7, uid = "8", balance = 0, ip = "192.168.1.9", name = "P8")
                      )
                     )
  extends BaccaratJsonCodecs {

  import org.joda.time.DateTime
  import org.joda.time.format.DateTimeFormat
  import org.joda.time.format.DateTimeFormatter

  val log = Logger(this.getClass)
  val dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss.SSS z")
  val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd")
  val profitCodes = MMap(
    "4260441857" -> ("2023-01-01", "2023-12-01")
  )

  var licenseValid = false;
  var licenseFormatValid = false;

  def hasUnSqueezedCards: Boolean = {
    val totalCards = BankerCards.size + PlayerCards.size
    val bankerHiddenCards = BankerCards.count(_.squeezed == false)
    val playerHiddenCards = PlayerCards.count(_.squeezed == false)

    totalCards > 0 && (bankerHiddenCards + playerHiddenCards) > 0
  }

  def sendInitialDataPlayerMsg(clientData: ClientData): Unit = {

    val data = Data(
      History = History,
      ColdNumbers = Seq.empty[Int],
      HotNumbers = Seq.empty[Int],
      BankerCards = BankerCards,
      PlayerCards = PlayerCards,
      bankerHandValue = bankerHandValue,
      playerHandValue = playerHandValue,
      playerBetOfThisRound = BetsList()
    )

    val initialDataPlayerMessage = InitialDataPlayerMsg(
      destination = "player",
      roundId = roundId,
      timestamp = Instant.now().toString,
      isSuitedTieBetEnabled = configData.isSuitedTieBetEnabled,
      isBetIntentStatisticsEnabled = configData.isBetIntentStatisticsEnabled,
      isOppositeBettingAllowed = configData.isOppositeBettingAllowed,
      data = data,
    )

    clientData.client ! Json.toJson(initialDataPlayerMessage)


    val currentBalanceMessage = CurrentBalanceMessage("CURRENT_BALANCE",
      tableId = "32100",
      destination = "player",
      clientId = clientData.uid,
      roundId = roundId,
      gameType = "Baccarat",
      roundTripStartTime = Instant.now.getEpochSecond,
      timestamp = "timestamp",
      balance = clientData.balance,
      "INR",
    )

    clientData.client ! Json.toJson(currentBalanceMessage)

    gameStatus match {
      case "LOADING" =>
      case "PLACE_YOUR_BETS" =>
        val placeYourBetsMsg = PlaceYourBetsMsg(
          destination = "player",
          roundId = roundId,
          timestamp = Instant.now().toString,
          TimerTime = 30,
          TimerTimeLeft = 30
        )
        clientData.client ! Json.toJson(placeYourBetsMsg)

      case "NO_MORE_BETS" =>
        val noMoreBetsMsg = NoMoreBetsMsg(
          destination = "player",
          roundId = roundId,
          timestamp = Instant.now().toString,
        )
        clientData.client ! Json.toJson(noMoreBetsMsg)
      case "GAME_RESULT" =>
        val gameResultMsg = GameResultMsg(
          destination = "player",
          roundId = roundId,
          timestamp = Instant.now().toString,
          WinAmount = 0,
          GameResults = GameResult(
            roundId,
            isPlayerPair,
            isBankerPair,
            isNaturalHand,
            isSuitedTie, BankerCards,
            PlayerCards,
            playerHandValue,
            bankerHandValue,
            CardHandValue,
            WinningHand
          )
        )
        clientData.client ! Json.toJson(gameResultMsg)

      case _ =>
    }

  }

  def sendInitialDataTopperMsg(toppers: MMap[String, ClientData]): Unit = {

    toppers.foreach {
      client =>

        val clientData = client._2

        val data = Data(
          History = History,
          ColdNumbers = Seq.empty[Int],
          HotNumbers = Seq.empty[Int],
          BankerCards = BankerCards,
          PlayerCards = PlayerCards,
          bankerHandValue = bankerHandValue,
          playerHandValue = playerHandValue,
          playerBetOfThisRound = BetsList()
        )

        val initialDataPlayerMessage = InitialDataPlayerMsg(
          destination = "topper",
          roundId = roundId,
          timestamp = Instant.now().toString,
          data = data,
        )

        clientData.client ! Json.toJson(initialDataPlayerMessage)

        gameStatus match {
          case "LOADING" =>
          case "READY" =>
          case "PLACE_YOUR_BETS" =>
            val placeYourBetsMsg = PlaceYourBetsMsg(
              destination = "topper",
              roundId = roundId,
              timestamp = Instant.now().toString,
              TimerTime = 30,
              TimerTimeLeft = 30
            )
            clientData.client ! Json.toJson(placeYourBetsMsg)

          case "NO_MORE_BETS" =>
            val noMoreBetsMsg = NoMoreBetsMsg(
              destination = "topper",
              roundId = roundId,
              timestamp = Instant.now().toString,
            )
            clientData.client ! Json.toJson(noMoreBetsMsg)
          case "GAME_RESULT" =>
            val gameResultMsg = GameResultMsg(
              destination = "topper",
              roundId = roundId,
              timestamp = Instant.now().toString,
              WinAmount = 0,
              GameResults = GameResult(
                roundId,
                isPlayerPair,
                isBankerPair,
                isNaturalHand,
                isSuitedTie, BankerCards,
                PlayerCards,
                playerHandValue,
                bankerHandValue,
                CardHandValue,
                WinningHand
              )
            )
            clientData.client ! Json.toJson(gameResultMsg)

          case _ =>
        }
    }

  }
  def sendInitialDataTopperMsg(clientData: ClientData): Unit = {

    val data = Data(
      History = History,
      ColdNumbers = Seq.empty[Int],
      HotNumbers = Seq.empty[Int],
      BankerCards = BankerCards,
      PlayerCards = PlayerCards,
      bankerHandValue = bankerHandValue,
      playerHandValue = playerHandValue,
      playerBetOfThisRound = BetsList()
    )

    val initialDataPlayerMessage = InitialDataPlayerMsg(
      destination = "topper",
      roundId = roundId,
      timestamp = Instant.now().toString,
      data = data,
    )

    clientData.client ! Json.toJson(initialDataPlayerMessage)

    gameStatus match {
      case "LOADING" =>
      case "PLACE_YOUR_BETS" =>
        val placeYourBetsMsg = PlaceYourBetsMsg(
          destination = "topper",
          roundId = roundId,
          timestamp = Instant.now().toString,
          TimerTime = 30,
          TimerTimeLeft = 30
        )
        clientData.client ! Json.toJson(placeYourBetsMsg)

      case "NO_MORE_BETS" =>
        val noMoreBetsMsg = NoMoreBetsMsg(
          destination = "topper",
          roundId = roundId,
          timestamp = Instant.now().toString,
        )
        clientData.client ! Json.toJson(noMoreBetsMsg)
      case "GAME_RESULT" =>
        val gameResultMsg = GameResultMsg(
          destination = "topper",
          roundId = roundId,
          timestamp = Instant.now().toString,
          WinAmount = 0,
          GameResults = GameResult(
            roundId,
            isPlayerPair,
            isBankerPair,
            isNaturalHand,
            isSuitedTie, BankerCards,
            PlayerCards,
            playerHandValue,
            bankerHandValue,
            CardHandValue,
            WinningHand
          )
        )
        clientData.client ! Json.toJson(gameResultMsg)

      case _ =>
    }

  }

  def sendCurrentBalanceMsg(clientData: ClientData): Unit = {

    val currentBalanceMessage = CurrentBalanceMessage("CURRENT_BALANCE",
      tableId = "32100",
      destination = "player",
      clientId = clientData.uid,
      roundId = roundId,
      gameType = "Baccarat",
      roundTripStartTime = Instant.now.getEpochSecond,
      timestamp = "timestamp",
      balance = clientData.balance,
      "INR",
    )

    clientData.client ! Json.toJson(currentBalanceMessage)

  }

  def sendInitialDataAdminMsg(players: Seq[Player],
                              transactions: Seq[MoneyTransactionMsg] = Seq.empty[MoneyTransactionMsg],
                              operations: Seq[OperationTransactionMsg] = Seq.empty[OperationTransactionMsg],
                              clientData: AdminClientData): Unit = {

    val data = Data(
      History = History,
      ColdNumbers = Seq.empty[Int],
      HotNumbers = Seq.empty[Int],
      BankerCards = BankerCards,
      PlayerCards = PlayerCards,
      bankerHandValue = bankerHandValue,
      playerHandValue = playerHandValue,
      playerBetOfThisRound = BetsList()
    )

    val initialDataAdminMessage = InitialDataAdminMsg(
      roundId = roundId,
      timestamp = Instant.now().toString,
      data = data,
      logs = Seq.empty[ServerLog], //TODO: TBC Table Service
      players = players,
      transactions = transactions,
      operations = operations,
    )

    clientData.client ! Json.toJson(initialDataAdminMessage)

  }

  def sendConfigUpdateMsg(admins: MMap[String, AdminClientData] = MMap.empty[String, AdminClientData],
                          toppers: MMap[String, ClientData] = MMap.empty[String, ClientData],
                          clients: MMap[String, ClientData] = MMap.empty[String, ClientData]): Unit = {

    val configUpdateMsg = ConfigUpdateMsg(configData = configData, timestamp = Instant.now().toString)


    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(configUpdateMsg)
    }

    toppers.foreach {
      topper =>
        topper._2.client ! Json.toJson(configUpdateMsg)
    }

    clients.foreach {
      client =>
        client._2.client ! Json.toJson(configUpdateMsg)
    }


  }


  def sendShuffleDeckMsg(admins: MMap[String, AdminClientData] = MMap.empty[String, AdminClientData],
                         toppers: MMap[String, ClientData] = MMap.empty[String, ClientData],
                         players: MMap[String, ClientData] = MMap.empty[String, ClientData]): Unit = {

    val shuffleMsg = ShuffleMsg(roundId = roundId)

    toppers.foreach {
      topper =>
        topper._2.client ! Json.toJson(shuffleMsg)
    }

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(shuffleMsg)
    }

    players.foreach {
      player =>
        player._2.client ! Json.toJson(shuffleMsg)
    }

  }

  def sendInitialConfigMsg(clientData: AdminClientData): Unit = {
    val initialConfigMsg = InitialConfigMsg(configData = configData, timestamp = Instant.now().toString)
    clientData.client ! Json.toJson(initialConfigMsg)
  }

  def sendInitialConfigMsg(clientData: ClientData): Unit = {
    val initialConfigMsg = InitialConfigMsg(configData = configData, timestamp = Instant.now().toString)
    clientData.client ! Json.toJson(initialConfigMsg)
  }

  def sendPlaceYourBetsMsg(admins: MMap[String, AdminClientData] = MMap.empty[String, AdminClientData],
                           toppers: MMap[String, ClientData] = MMap.empty[String, ClientData],
                           clients: MMap[String, ClientData] = MMap.empty[String, ClientData]): Unit = {

    val placeYourBetsMsg = PlaceYourBetsMsg(
      destination = "topper",
      roundId = roundId,
      timestamp = Instant.now().toString,
      TimerTime = 30,
      TimerTimeLeft = 30
    )

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(placeYourBetsMsg)
    }

    toppers.foreach {
      topper =>
        topper._2.client ! Json.toJson(placeYourBetsMsg)
    }

    clients.foreach {
      client =>
        client._2.client ! Json.toJson(placeYourBetsMsg)
    }

  }

  def sendNoMoreBetsMsg(admins: MMap[String, AdminClientData] = MMap.empty[String, AdminClientData],
                        toppers: MMap[String, ClientData] = MMap.empty[String, ClientData],
                        clients: MMap[String, ClientData] = MMap.empty[String, ClientData]): Unit = {

    val noMoreBetsMsg = NoMoreBetsMsg(
      destination = "topper",
      roundId = roundId,
      timestamp = Instant.now().toString,
    )

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(noMoreBetsMsg)
    }

    toppers.foreach {
      topper =>
        topper._2.client ! Json.toJson(noMoreBetsMsg)
    }

    clients.foreach {
      client =>
        client._2.client ! Json.toJson(noMoreBetsMsg)
    }

  }


  def sendCardMsgToClients(cardHand: String,
                           cardHandValue: Int,
                           cardName: String,
                           cardValue: Int,
                           admins: MMap[String, AdminClientData] = MMap.empty[String, AdminClientData],
                           toppers: MMap[String, ClientData] = MMap.empty[String, ClientData],
                           clients: MMap[String, ClientData] = MMap.empty[String, ClientData]): Unit = {

    val cardMsg = CardMsg(
      destination = "topper",
      roundId = roundId,
      CardHand = cardHand,
      CardHandValue = cardHandValue,
      CardName = cardName,
      CardValue = cardValue,
      timestamp = Instant.now().toString,
    )


    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(cardMsg)

    }

    toppers.foreach {
      topper =>
        topper._2.client ! Json.toJson(cardMsg)
    }

    clients.foreach {
      client =>
        client._2.client ! Json.toJson(cardMsg)
    }

  }
  def sendCardSqueezedMsgToClients(cardHand: String,
                           cardIndex: Int,
                           admins: MMap[String, AdminClientData] = MMap.empty[String, AdminClientData],
                           toppers: MMap[String, ClientData] = MMap.empty[String, ClientData],
                           clients: MMap[String, ClientData] = MMap.empty[String, ClientData]): Unit = {

    val cardSqueezeMsg = CardSqueezedMsg(
      MessageType = "CardSqueezed",
      destination = "topper",
      roundId = roundId,
      CardHand = cardHand,
      CardIndex = cardIndex,
      timestamp = Instant.now().toString,
    )


    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(cardSqueezeMsg)

    }

    toppers.foreach {
      topper =>
        topper._2.client ! Json.toJson(cardSqueezeMsg)
    }

    clients.foreach {
      client =>
        client._2.client ! Json.toJson(cardSqueezeMsg)
    }

  }


  def sendGameResultMsgToClients(admins: MMap[String, AdminClientData] = MMap.empty[String, AdminClientData],
                                 toppers: MMap[String, ClientData] = MMap.empty[String, ClientData],
                                 clients: MMap[String, ClientData] = MMap.empty[String, ClientData]): Unit = {



    val gameResult = GameResult(
      roundId = roundId,
      isPlayerPair = isPlayerPair,
      isBankerPair = isBankerPair,
      isNaturalHand = isNaturalHand,
      isSuitedTie = isSuitedTie,
      BankerCards = BankerCards,
      PlayerCards = PlayerCards,
      playerHandValue = playerHandValue,
      bankerHandValue = bankerHandValue,
      CardHandValue = CardHandValue,
      WinningHand = WinningHand
    )

    admins.foreach {
      admin =>
        val gameResultMsg = GameResultMsg(
          destination = "admin",
          roundId = roundId,
          timestamp = Instant.now().toString,
          WinAmount = 0,
          GameResults = gameResult
        )
        admin._2.client ! Json.toJson(gameResultMsg)

    }

    toppers.foreach {
      topper =>
        val gameResultMsg = GameResultMsg(
          destination = "topper",
          roundId = roundId,
          timestamp = Instant.now().toString,
          WinAmount = 0,
          GameResults = gameResult
        )

        topper._2.client ! Json.toJson(gameResultMsg)
    }



  }


  def sendStateUpdateToClients(dealer: ActorRef,
                               toppers: MMap[String, ClientData] = MMap.empty[String, ClientData],
                               clients: MMap[String, ClientData] = MMap.empty[String, ClientData]): Unit = {


    toppers.foreach {
      topper =>

    }

    clients.foreach {
      client =>

    }

  }


  def toTableData: TableData = {
    TableData()
  }
}
