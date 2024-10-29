package actors.baccarat

//Standard Packages
import akka.actor.{Actor, ActorLogging, ActorRef, Props, Stash, Timers}
import play.api.libs.json.Json

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import scala.collection.mutable.{Map => MMap}
import scala.concurrent.duration._
import scala.language.postfixOps
import services.{BaccaratSSeaterTableService, GameService}
import model.common.messages.{AdminClientData, CurrentBalanceMessage, GameTransaction, MoneyTransaction, MoneyTransactionMsg, OperationTransactionMsg, Player, PlayerCreatedMsg, PlayerUpdatedMsg}
import model.baccarat.{Card, Deck416, Hand}
import model.baccarat.data.{BetsList, ClientData, ConfigData, GameCard, GameResult, LicenseData, SideBet, SqueezedCard, TableState, WinResult}
import model.baccarat.message.{BaccaratJsonCodecs, GameResultMsg, WonBetsMsg}


/*
Every Actor shall follow a few Akka conventions:

  - The messages it sends/receives, or its protocol, are defined on its companion object
  - It also defines a props method on its companion object that returns the props for creating it
  -

 */
object BaccaratTableActor {
  val name = "baccarat-table-actor"
  val path = s"/usr/$name"

  def props(gameService: GameService,
            baccaratSSeaterTableService: BaccaratSSeaterTableService
           ): Props = Props(new BaccaratTableActor(gameService, baccaratSSeaterTableService))

  //Self Triggered Messages
  case object TimerKey

  case object PollTick

  case object Init

  //Incoming Messages

  case class PlayerConnected(name: String, actor: ActorRef, client: ActorRef)

  case class PlayerDisConnected(name: String)

  case class TopperConnected(name: String, actor: ActorRef, client: ActorRef)

  case class TopperDisconnected(name: String)


  //ADMIN PAGE RELATED Messages
  case class InfoPaperShow(show: Boolean = false)

  case class AdminConnected(name: String, actor: ActorRef, client: ActorRef)

  case class AdminDisConnected(name: String)

  case object Reconnected

  case class TableSettingsChange(tableId: String, gameType: String, rake: Int, blind: Int)

  case class BaccaratTableSettingChange(tableId: String, dealer: String, minBet: Int, maxBet: Int)

  case class PlayerMoneyTransaction(moneyTransaction: MoneyTransaction, actor: ActorRef, client: ActorRef)

  case class PlayerGameTransaction(gameTransaction: GameTransaction)

  case class PlayerStatusOnline(uid: String)

  case class PlayerStatusOffline(uid: String)

  case class SeatBalanceUpdated(uid: String, balance: Double = 0)

  case class SeatNameUpdated(uid: String, name: String)

  case class SeatIpUpdated(uid: String, ip: String)

  case class PlayerNameUpdate(uid: String, nickname: String)

  case class PlayerIpUpdate(uid: String, ip: String)

  case class GuestPlayerConnected(player: Player)

  //Game Related Messages

  case class PersistTableState(data: TableState)

  case class CardDrawn(card: Card)

  case class PlayerBetPlaced(name: String, betsList: BetsList, client: ActorRef)

  case class PlayerBetIntentPlaced(name: String, betsList: BetsList, client: ActorRef)

  case class PlayerBalanceUpdated(name: String, balance: Double = 0)


  case object PlaceYourBets

  case object NoMoreBets

  case object ShuffleDeck

  case object CancelPrevGame

  case object ToggleAutoDraw

  case object ToggleAutoPlay

  case object NoWinDetected

  case object WinDetected

  case class WinResultMsg(winResult: WinResult)

  case class SqueezedCardMsg(squeezedCard: SqueezedCard)

  case class ConfigUpdateCommand(configData: ConfigData)

}

class BaccaratTableActor(gameService: GameService,
                         baccaratSSeaterTableService: BaccaratSSeaterTableService
                        ) extends Actor
  with Stash
  with ActorLogging
  with Timers
  with BaccaratUtilities
  with BaccaratJsonCodecs {

  import BaccaratTableActor._
  import actors.LogManagerActor._

  private val logManagerActor: ActorRef = gameService.getLoggingActor

  private val tableId = "32100"
  private val limitId = 992712
  private var deck416 = Deck416
  val dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss.SSS z")

  private var players: Seq[Player] = baccaratSSeaterTableService.getPlayersData(tableId)
  private var admins = MMap.empty[String, AdminClientData]
  private var clients: MMap[String, ClientData] = MMap.empty[String, ClientData]
  private var toppers: MMap[String, ClientData] = MMap.empty[String, ClientData]
  private var autoPlayMod = false;
  private var autoDrawMod = false;

  /* Licence Setup Changes */

  import org.joda.time.DateTime
  import org.joda.time.format.DateTimeFormat
  import org.joda.time.format.DateTimeFormatter

  private var productValid = true;
  private var licenseValid = false;
  private var licenseFormatValid = false;

  val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd")
  private var profitCodes = MMap(
    "4260441857" -> ("2023-01-01", "2023-02-05"),
    "9645318040" -> ("2023-02-01", "2023-03-05"),
    "9645618040" -> ("2023-01-01", "2023-12-01"),
  )

  private var productCodes = MMap(
    "2509330773" -> "96:11:5A:6C:9F:28"
  )

  /* License Setup End */

  //This method is called when the actor is first created, and on all restarts.
  override def preStart(): Unit = {

    log.info(s"Baccarat Table Actor Pre Start...")
    super.preStart()

    timers.startSingleTimer(TimerKey, Init, timeout = 5 second)

  }

  override def postStop(): Unit = {
    log.info("Baccarat Table Actor Stopped...")
    super.postStop()
  }

  override def receive: Receive = TABLE_CLOSED_STATE(data = TableState(gameStatus = "TABLE_CLOSED"))


  def TABLE_CLOSED_STATE(data: TableState): Receive = {
    case Init =>
      log.info("Init Received")
      timers.startTimerWithFixedDelay(TimerKey, PollTick, 1 second)
      context.become(TABLE_READY_STATE_1(data.copy(gameStatus = "READY")))
    case PollTick =>
    case message => {
      log.info(s"Stashing $message because I can't handle it in the LOADING state")
    }
  }


  def TABLE_READY_STATE_1(data: TableState): Receive = {
    case InfoPaperShow(show) => context.become(TABLE_READY_STATE_1(data = handleInfoPaperShow(data, show)))
    case ConfigUpdateCommand(configData) => context.become(TABLE_READY_STATE_1(data = handleConfigUpdate(data, configData)))
    case ShuffleDeck => context.become(TABLE_READY_STATE_1(data = handleShuffleDeck(data)))
    case CancelPrevGame => context.become(TABLE_READY_STATE_1(data = handleCancelPrevGame(data)))


    case AdminDisConnected(name) => if (admins.contains(name)) admins -= name
    case AdminConnected(name, actor, client) => handleAdminConnected(data, name, actor, client)
    case PlayerMoneyTransaction(moneyTransaction, actor, client) => handleMoneyTransaction(data, moneyTransaction, actor, client)
    case PlayerGameTransaction(gameTransaction) => handleGameTransaction(gameTransaction)
    case PlayerStatusOnline(uid: String) => handlePlayerStatus(uid, status = "online")
    case PlayerStatusOffline(uid: String) => handlePlayerStatus(uid, status = "offline")
    case PlayerNameUpdate(uid: String, nickname: String) => handlePlayerNameUpdate(uid, nickname)
    case PlayerIpUpdate(uid: String, ip: String) => handlePlayerIpUpdate(uid, ip)

    case BaccaratTableSettingChange(id, dealer, minBet, maxBet) =>
      val updatedState = handleTableSettingsChange(data, dealer, minBet, maxBet)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_READY_STATE_1(data = updatedState))
    case SeatBalanceUpdated(uid, balance) =>
      val updatedState = handlePlayerBalanceUpdated(data, uid, balance)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_READY_STATE_1(data = updatedState))

    case PlayerDisConnected(playerIp) =>
      val updatedState = handlePlayerDisconnected(data, playerIp)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_READY_STATE_1(data = updatedState))
    case PlayerConnected(name, actor, client) =>
      val updatedState = handlePlayerConnected(data, name, actor, client)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_READY_STATE_1(data = updatedState))

    case GuestPlayerConnected(player) => handleGuestPlayerConnected(player)
    case TopperConnected(name, actor, client) => handleTopperConnected(data, name, actor, client)
    case PollTick =>
      if(autoPlayMod) {
        context become TABLE_READY_STATE_1(data = data.copy(waitingCounter = data.waitingCounter + 1))
        if (data.waitingCounter == 10) {
          val updatedState = handleReadyToPlaceYourBetsState(data)
          context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = updatedState))
        }
      }

    case PlaceYourBets =>
      val updatedState = handleReadyToPlaceYourBetsState(data)
      context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = updatedState))

    case msg => log.error(s"message $msg not handled in TABLE_READY_STATE_1 state")
  }


  def TABLE_PLACE_YOUR_BETS_STATE_2(data: TableState): Receive = {
    case InfoPaperShow(show) => context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = handleInfoPaperShow(data, show)))
    case ConfigUpdateCommand(configData) => context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = handleConfigUpdate(data, configData)))
    case ShuffleDeck => context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = handleShuffleDeck(data)))
    case CancelPrevGame => context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = handleCancelPrevGame(data)))

    /* Betting related messages handling */
    case PlaceYourBets =>
      val updatedState = handleReadyToPlaceYourBetsState(data)
      context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = updatedState))
    case NoMoreBets =>
      val licenseData = baccaratSSeaterTableService.getLicenseData
      val updatedState = handlePYBtoDrawingCardsState(data, licenseData = licenseData)
      context.become(TABLE_DRAWING_CARDS_STATE_3(data = updatedState))
    case PlayerBetPlaced(name, betsList, client) =>
    case PlayerBetIntentPlaced(name, betsList, client) =>

    case AdminDisConnected(name) => if (admins.contains(name)) admins -= name
    case AdminConnected(name, actor, client) => handleAdminConnected(data, name, actor, client)
    case PlayerMoneyTransaction(moneyTransaction, actor, client) => handleMoneyTransaction(data, moneyTransaction, actor, client)
    case PlayerGameTransaction(gameTransaction) => handleGameTransaction(gameTransaction)
    case PlayerStatusOnline(uid: String) => handlePlayerStatus(uid, status = "online")
    case PlayerStatusOffline(uid: String) => handlePlayerStatus(uid, status = "offline")
    case PlayerNameUpdate(uid: String, nickname: String) => handlePlayerNameUpdate(uid, nickname)
    case PlayerIpUpdate(uid: String, ip: String) => handlePlayerIpUpdate(uid, ip)

    case SeatBalanceUpdated(uid, balance) =>
      val updatedState = handlePlayerBalanceUpdated(data, uid, balance)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = updatedState))
    case PlayerDisConnected(playerIp) =>
      val updatedState = handlePlayerDisconnected(data, playerIp)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = updatedState))
    case PlayerConnected(name, actor, client) =>
      val updatedState = handlePlayerConnected(data, name, actor, client)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = updatedState))

    case GuestPlayerConnected(player) => handleGuestPlayerConnected(player)
    case TopperConnected(name, actor, client) => handleTopperConnected(data, name, actor, client)
    case PollTick =>
      if(autoPlayMod) {
        context become TABLE_PLACE_YOUR_BETS_STATE_2(data = data.copy(bettingCounter = data.bettingCounter + 1))
        if ((data.bettingCounter % 5) == 0) {
          val updatedState = data.copy(bettingCounter = 30 - data.bettingCounter)
          updatedState.sendStateUpdateToClients(self, toppers, clients)
        }
        if (data.bettingCounter == 15) {
          val licenseData = baccaratSSeaterTableService.getLicenseData
          val updatedState = handlePYBtoDrawingCardsState(data, licenseData = licenseData)
          context.become(TABLE_DRAWING_CARDS_STATE_3(data = updatedState))
        }
      }


    case msg => log.error(s"message $msg not handled in TABLE_PLACE_YOUR_BETS_STATE_2 state")
  }


  def TABLE_DRAWING_CARDS_STATE_3(data: TableState): Receive = {
    case InfoPaperShow(show) => context.become(TABLE_DRAWING_CARDS_STATE_3(data = handleInfoPaperShow(data, show)))
    case ConfigUpdateCommand(configData) => context.become(TABLE_DRAWING_CARDS_STATE_3(data = handleConfigUpdate(data, configData)))
    case ShuffleDeck => context.become(TABLE_DRAWING_CARDS_STATE_3(data = handleShuffleDeck(data)))
    case CancelPrevGame => context.become(TABLE_DRAWING_CARDS_STATE_3(data = handleCancelPrevGame(data)))

    case PlaceYourBets =>
      val updatedState = handleReadyToPlaceYourBetsState(data)
      context.become(TABLE_PLACE_YOUR_BETS_STATE_2(data = updatedState))

    case PlayerBetPlaced(name, betsList, client) =>
      log.info(s"Player Bets Placed Received $name, $betsList")
      context.become(TABLE_DRAWING_CARDS_STATE_3(data = handlePlayerBetPlaced(data, name, betsList, client)))

    case AdminDisConnected(name) => if (admins.contains(name)) admins -= name
    case AdminConnected(name, actor, client) => handleAdminConnected(data, name, actor, client)
    case PlayerMoneyTransaction(moneyTransaction, actor, client) => handleMoneyTransaction(data, moneyTransaction, actor, client)
    case PlayerGameTransaction(gameTransaction) => handleGameTransaction(gameTransaction)
    case PlayerStatusOnline(uid: String) => handlePlayerStatus(uid, status = "online")
    case PlayerStatusOffline(uid: String) => handlePlayerStatus(uid, status = "offline")
    case PlayerNameUpdate(uid: String, nickname: String) => handlePlayerNameUpdate(uid, nickname)
    case PlayerIpUpdate(uid: String, ip: String) => handlePlayerIpUpdate(uid, ip)

    case SeatBalanceUpdated(uid, balance) =>
      val updatedState = handlePlayerBalanceUpdated(data, uid, balance)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_DRAWING_CARDS_STATE_3(data = updatedState))

    case PlayerDisConnected(playerIp) =>
      val updatedState = handlePlayerDisconnected(data, playerIp)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_DRAWING_CARDS_STATE_3(data = updatedState))
    case PlayerConnected(name, actor, client) =>
      val updatedState = handlePlayerConnected(data, name, actor, client)
      updatedState.sendStateUpdateToClients(self, toppers, clients)
      context.become(TABLE_DRAWING_CARDS_STATE_3(data = updatedState))

    case GuestPlayerConnected(player) => handleGuestPlayerConnected(player)
    case TopperConnected(name, actor, client) => handleTopperConnected(data, name, actor, client)

    case PollTick  =>

      if(autoPlayMod || autoDrawMod) {
        val drawnCard = deck416.drawCard

        data.turn match {
          case (3, 2) => { //3rd Round Card Draw For Banker
            val gameCards = data.gameCards
            gameCards(2) = gameCards(2) ::: List(drawnCard.toString);
            gameCards(2) = gameCards(2).updated(2, getPlayerHand(gameCards, 2).score.toString) //Update Score

            val updatedState = handleGameCompletedState(data, gameCards, drawnCard.toString, 2, 3)
            context become TABLE_READY_STATE_1(data = updatedState)

          }
          case (3, 1) => { //3rd Round Card Draw For Player
            val gameCards = data.gameCards
            gameCards(1) = gameCards(1) ::: List(drawnCard.toString);
            gameCards(1) = gameCards(1).updated(2, getPlayerHand(gameCards, 1).score.toString) //Update Score

            /*
            * After Player 3rd Card Drawn,
            * Depending on Player 3rd card and Current Banker score,
            * the decision is auto taken to draw/not draw another banker card
            *
            * Player Card Drawn          Banker Score          Action
            * 8                          3,4,5,6,7               Stay
            * 0, 1, 9                    4,5,6,7                 Stay
            * 2, 3                       5,6,7                   Stay
            * 4, 5                       6, 7                    Stay
            * 6, 7                       7                       Stay
            *
            * OR ANOTHER WAY TO LOOK AT IT IS
            *
            * - Banker Score is 3, Only way to stay is Player 3rd Card drawn is 8
            * - Banker Score is 4, Banker Stays if Player Card Drawn is 0, 1, 8, OR 9
            * - Banker Score is 5, Banker Stays if Player Card Drawn is 0, 1, 2, 3, 8 OR 9
            * - Banker Score is 6, Banker Stays if Player Card Drawn is 0, 1, 2, 3, 4, 5, 8 OR 9
            * - Banker Score is 7, Banker Stays Any Card Drawn
            *
            * */

            (getPlayerHand(gameCards, 2).score, drawnCard.rank.value) match {
              case (3, 8) |
                   (4, 0 | 1 | 8 | 9) |
                   (5, 0 | 1 | 2 | 3 | 8 | 9) |
                   (6, 0 | 1 | 2 | 3 | 4 | 5 | 8 | 9) |
                   (7, _) => {
                val updatedState = handleGameCompletedState(data, gameCards, drawnCard.toString, 1, 3)
                context become TABLE_READY_STATE_1(data = updatedState)
              }

              case (_, _) => {
                val updatedState = handleCardsDrawnStateChange(data, gameCards = gameCards, drawCardCounter = 0, turn = (3, 2), drawnCard.toString, 1, 3)
                context become TABLE_DRAWING_CARDS_STATE_3(updatedState);
              }
            }
          }
          case (2, 2) => { //2nd Round Card Draw For Banker
            val gameCards = data.gameCards
            gameCards(2) = gameCards(2) ::: List(drawnCard.toString);
            gameCards(2) = gameCards(2).updated(2, getPlayerHand(gameCards, 2).score.toString) //Update Score

            (getPlayerHand(gameCards, 1).score, getPlayerHand(gameCards, 2).score) match { //Player or Banker has Natural??
              case (9 | 8, _) | (_, 9 | 8) => {
                val updatedState = handleGameCompletedState(data, gameCards, drawnCard.toString, 2, 2)
                context become TABLE_READY_STATE_1(data = updatedState)
              }
              case (_, _) => {

                //Player Draw Check
                getPlayerHand(gameCards, 1).score match {
                  case 0 | 1 | 2 | 3 | 4 | 5 => {
                    val updatedState = handleCardsDrawnStateChange(data, gameCards = gameCards, drawCardCounter = 0, turn = (3, 1), drawnCard.toString, 2, 2)
                    context become TABLE_DRAWING_CARDS_STATE_3(updatedState);
                  }
                  case _ => {

                    //Player Stand and Banker Draw Check
                    getPlayerHand(gameCards, 2).score match {
                      case 0 | 1 | 2 | 3 | 4 | 5 => {
                        val updatedState = handleCardsDrawnStateChange(data, gameCards = gameCards, drawCardCounter = 0, turn = (3, 2), drawnCard.toString, 2, 2)
                        context become TABLE_DRAWING_CARDS_STATE_3(updatedState);
                      }
                      case 6 | 7 => {
                        val updatedState = handleGameCompletedState(data, gameCards, drawnCard.toString, 2, 2)
                        context become TABLE_READY_STATE_1(data = updatedState)

                      }
                    }

                  }
                }
              }
            }
          }
          case (rnd: Int, player: Int) => { //Initial Draw For Player & Banker
            val gameCards = data.gameCards
            gameCards(player) = gameCards(player) ::: List(drawnCard.toString); //Add Card
            gameCards(player) = gameCards(player).updated(2, getPlayerHand(gameCards, player).score.toString) //Update Score

            val nextPlayer = if (player == 2) 1 else player + 1
            val nextRound = if (player == 2) rnd + 1 else rnd

            val updatedState = handleCardsDrawnStateChange(data, gameCards = gameCards, drawCardCounter = 0, turn = (nextRound, nextPlayer), drawnCard.toString, player, rnd)

            context become TABLE_DRAWING_CARDS_STATE_3(updatedState);
          }
        }
      }
      else {
        if (data.gameStatus == "GAME_RESULT" && !data.hasUnSqueezedCards) {
          doGameCompletedAnnouncement(data)
          context become TABLE_READY_STATE_1(data)
        }
      }




    case CardDrawn(card) =>
      val drawnCard = card

      data.turn match {
        case (3, 2) => { //3rd Round Card Draw For Banker
          val gameCards = data.gameCards
          gameCards(2) = gameCards(2) ::: List(drawnCard.toString);
          gameCards(2) = gameCards(2).updated(2, getPlayerHand(gameCards, 2).score.toString) //Update Score

          val updatedState = handleGameCompletedState(data, gameCards, drawnCard.toString, 2, 3)
          context become TABLE_DRAWING_CARDS_STATE_3(data = updatedState)

        }
        case (3, 1) => { //3rd Round Card Draw For Player
          val gameCards = data.gameCards
          gameCards(1) = gameCards(1) ::: List(drawnCard.toString);
          gameCards(1) = gameCards(1).updated(2, getPlayerHand(gameCards, 1).score.toString) //Update Score

          (getPlayerHand(gameCards, 2).score, drawnCard.rank.value) match {
            case (3, 8) |
                 (4, 0 | 1 | 8 | 9) |
                 (5, 0 | 1 | 2 | 3 | 8 | 9) |
                 (6, 0 | 1 | 2 | 3 | 4 | 5 | 8 | 9) |
                 (7, _) => {
              val updatedState = handleGameCompletedState(data, gameCards, drawnCard.toString, 1, 3)
              context become TABLE_DRAWING_CARDS_STATE_3(data = updatedState)
            }

            case (_, _) => {
              val updatedState = handleCardsDrawnStateChange(data, gameCards = gameCards, drawCardCounter = 0, turn = (3, 2), drawnCard.toString, 1, 3)
              context become TABLE_DRAWING_CARDS_STATE_3(updatedState);
            }
          }
        }
        case (2, 2) => { //2nd Round Card Draw For Banker
          val gameCards = data.gameCards
          gameCards(2) = gameCards(2) ::: List(drawnCard.toString);
          gameCards(2) = gameCards(2).updated(2, getPlayerHand(gameCards, 2).score.toString) //Update Score

          (getPlayerHand(gameCards, 1).score, getPlayerHand(gameCards, 2).score) match { //Player or Banker has Natural??
            case (9 | 8, _) | (_, 9 | 8) => {
              val updatedState = handleGameCompletedState(data, gameCards, drawnCard.toString, 2, 2)
              context become TABLE_DRAWING_CARDS_STATE_3(data = updatedState)
            }
            case (_, _) => {

              //Player Draw Check
              getPlayerHand(gameCards, 1).score match {
                case 0 | 1 | 2 | 3 | 4 | 5 => {
                  val updatedState = handleCardsDrawnStateChange(data, gameCards = gameCards, drawCardCounter = 0, turn = (3, 1), drawnCard.toString, 2, 2)
                  context become TABLE_DRAWING_CARDS_STATE_3(updatedState);
                }
                case _ => {

                  //Player Stand and Banker Draw Check
                  getPlayerHand(gameCards, 2).score match {
                    case 0 | 1 | 2 | 3 | 4 | 5 => {
                      val updatedState = handleCardsDrawnStateChange(data, gameCards = gameCards, drawCardCounter = 0, turn = (3, 2), drawnCard.toString, 2, 2)
                      context become TABLE_DRAWING_CARDS_STATE_3(updatedState);
                    }
                    case 6 | 7 => {
                      val updatedState = handleGameCompletedState(data, gameCards, drawnCard.toString, 2, 2)
                      context become TABLE_DRAWING_CARDS_STATE_3(data = updatedState)

                    }
                  }

                }
              }
            }
          }
        }
        case (rnd: Int, player: Int) => { //Initial Draw For Player & Banker
          val gameCards = data.gameCards
          gameCards(player) = gameCards(player) ::: List(drawnCard.toString); //Add Card
          gameCards(player) = gameCards(player).updated(2, getPlayerHand(gameCards, player).score.toString) //Update Score

          val nextPlayer = if (player == 2) 1 else player + 1
          val nextRound = if (player == 2) rnd + 1 else rnd

          val updatedState = handleCardsDrawnStateChange(
            data,
            gameCards = gameCards,
            drawCardCounter = 0,
            turn = (nextRound, nextPlayer),
            drawnCard.toString,
            player,
            rnd)

          context become TABLE_DRAWING_CARDS_STATE_3(updatedState);
        }
      }

    case SqueezedCardMsg(squeezedCard) =>
      val updatedState = handleCardSqueezedMsg(data, squeezedCard);
      context become TABLE_DRAWING_CARDS_STATE_3(updatedState);

    case WinResultMsg(winResult) =>

      /* 1. Copy winResult to an UpdatedState
      *  2. Send Game Result Message to all connected clients
      *  3. Move to Next Stage, TABLE_READY_STATE_1 with updatedState
      * */

      val gameResult = GameResult(
        roundId = winResult.roundId,
        isPlayerPair = winResult.isPlayerPair,
        isBankerPair = winResult.isBankerPair,
        isNaturalHand = winResult.isNaturalHand,
        isSuitedTie = winResult.isSuitedTie,
        BankerCards = winResult.BankerCards,
        PlayerCards = winResult.PlayerCards,
        playerHandValue = winResult.playerHandValue,
        bankerHandValue = winResult.bankerHandValue,
        CardHandValue = winResult.CardHandValue,
        WinningHand = winResult.WinningHand,
      )

      /* 1 */
      val updatedState = data.copy(
        roundId = winResult.roundId,
        isPlayerPair = winResult.isPlayerPair,
        isBankerPair = winResult.isBankerPair,
        isNaturalHand = winResult.isNaturalHand,
        isSuitedTie = winResult.isSuitedTie,
        BankerCards = winResult.BankerCards,
        PlayerCards = winResult.PlayerCards,
        playerHandValue = winResult.playerHandValue,
        bankerHandValue = winResult.bankerHandValue,
        CardHandValue = winResult.CardHandValue,
        WinningHand = winResult.WinningHand,
        winCards = List.empty[String],

        History = (data.History :+ WinResult(
          roundId = winResult.roundId,
          isPlayerPair = winResult.isPlayerPair,
          isBankerPair = winResult.isBankerPair,
          isNaturalHand = winResult.isNaturalHand,
          isSuitedTie = winResult.isSuitedTie,
          BankerCards = winResult.BankerCards,
          PlayerCards = winResult.PlayerCards,
          playerHandValue = winResult.playerHandValue,
          bankerHandValue = winResult.bankerHandValue,
          CardHandValue = winResult.CardHandValue,
          WinningHand = winResult.WinningHand
        )).takeRight(100)
      )


      /* 2 */
      clients.foreach {
        client =>
          val betsList = client._2.betsList
          val totalBetsValue = getTotalBetsValue(betsList = betsList)
          if (totalBetsValue > 0) {
            /* Get the wonBetsList */
            val wonBetsList = getWonBetsList(betsList = betsList, gameResult = gameResult)
            val totalWonBetsValue = getTotalWonBetsValue(wonBetsList = wonBetsList)

            if (totalWonBetsValue > 0) {

              /* Clients With Bets, Won Bets Send
              *   - Current Balance
              *   - Won Bets
              * */
              val wonBetsMsg = WonBetsMsg(
                clientId = client._2.uid,
                RoundTripStartTime = Instant.now.getEpochSecond,
                WinningBets = wonBetsList
              )

              client._2.client ! Json.toJson(wonBetsMsg)

              val currentBalanceMessage = CurrentBalanceMessage("CURRENT_BALANCE",
                tableId = "32100",
                destination = "player",
                clientId = client._2.uid,
                roundId = data.roundId,
                gameType = "Baccarat",
                roundTripStartTime = Instant.now.getEpochSecond,
                timestamp = "timestamp",
                balance = client._2.balance + totalWonBetsValue,
                "INR",
              )

              client._2.client ! Json.toJson(currentBalanceMessage)


            } else {
              /* Clients With Bets, Do not send anything but make sure Reports are updated with NoWin
              * */

            }

            /* Clients With Bets, Send
            *   - Game Results
            * */
            val gameResultMsg = GameResultMsg(
              destination = "player",
              roundId = data.roundId,
              timestamp = Instant.now().toString,
              WinAmount = totalWonBetsValue,
              GameResults = gameResult
            )

            client._2.client ! Json.toJson(gameResultMsg)

          } else {
            /* Clients Without Bet, Send
            *   - Game Results
            * */
            val gameResultMsg = GameResultMsg(
              destination = "player",
              roundId = data.roundId,
              timestamp = Instant.now().toString,
              WinAmount = 0,
              GameResults = gameResult
            )

            client._2.client ! Json.toJson(gameResultMsg)
          }

      }

      updatedState.sendGameResultMsgToClients(admins, toppers)

      /* 3 */
      context.become(TABLE_READY_STATE_1(data = updatedState))


    case msg => log.error(s"message $msg not handled in TABLE_DRAWING_CARDS_STATE_3 state")
  }


  def handleReadyToPlaceYourBetsState(tableState: TableState): TableState = {
    deck416.reShuffle();

    clients = clients.map { client => (client._1) -> client._2.copy(betsList = BetsList()) }
    val updatedState = tableState.copy(gameStatus = "PLACE_YOUR_BETS",
      WinningHand = "",
      CardHandValue = 0,
      playerHandValue = 0,
      bankerHandValue = 0,
      isPlayerPair = false,
      isBankerPair = false,
      isSuitedTie = false,
      isNaturalHand = false,
      PlayerCards = Seq.empty[GameCard],
      BankerCards = Seq.empty[GameCard],
      waitingCounter = 0,
      winCards = List("", "", ""),
      gameCards = Array(List("Tie", ""), List("Player", "", "", "", ""), List("Banker", "", "", "", "")),
      roundId = tableState.roundId + 1,
    )

    updatedState.sendPlaceYourBetsMsg(admins, toppers, clients)
    updatedState
  }


  def handlePYBtoDrawingCardsState(tableState: TableState, licenseData: LicenseData): TableState = {

    val LicenseData(name, client, install, macs, validProductCode, validProfitCode, toBeExpired, productCode, profitCode) = licenseData

    licenseFormatValid = false
    licenseValid = false

    if(profitCodes.contains(profitCode)) {
      val licenseDateStart = try {
        Some(new DateTime(profitCodes(profitCode)._1).toDateTime)
      } catch {
        case e: Throwable => None
      }
      val licenseDateEnd = try {
        Some(new DateTime(profitCodes(profitCode)._2).toDateTime)
      } catch {
        case e: Throwable => None
      }

      (licenseDateStart, licenseDateEnd) match {
        case (Some(startDate), Some(endDate)) =>
          licenseFormatValid = true
          licenseValid = startDate.isBeforeNow && endDate.isAfterNow
        case _ =>
      }
    }

    productValid = productCodes.contains(productCode) && macs.contains(productCodes(productCode))

    val updatedLicenseData = licenseData.copy(
      validProductCode = productValid,
      validProfitCode = licenseValid && licenseFormatValid ,
      toBeExpired = false
    )

    log.info(s"${updatedLicenseData.macs} ${productCode} ${profitCode} ${updatedLicenseData.validProductCode} ${updatedLicenseData.validProfitCode}")

    val updatedState = tableState.copy(
      gameStatus = "NO_MORE_BETS",
      bettingCounter = 0,
      drawCardCounter = 0,
      turn = (1, 1),
      licenseData = updatedLicenseData
    )

    updatedState.sendNoMoreBetsMsg(admins, toppers, clients)
    updatedState
  }

  def handleCardsDrawnStateChange(tableState: TableState,
                                  gameCards: Array[List[String]],
                                  drawCardCounter: Int, turn: (Int, Int),
                                  card: String,
                                  index: Int,
                                  rnd: Int): TableState = {

    val handIndex = index
    val cardIndex = 4 + rnd
    val playerCards = tableState.PlayerCards
    val bankerCards = tableState.BankerCards

    val updatedBankerCards = if (handIndex == 1) bankerCards else bankerCards :+ Card.parseGameCard(gameCards(2)(cardIndex))
    val updatedPlayerCards = if (handIndex == 2) playerCards else playerCards :+ Card.parseGameCard(gameCards(1)(cardIndex))


    val updatedState = tableState.copy(
      gameCards = gameCards,
      drawCardCounter = drawCardCounter,
      turn = turn,
      playerHandValue = getPlayerHand(gameCards, 1).score,
      bankerHandValue = getPlayerHand(gameCards, 2).score,
      isPlayerPair = getPlayerHand(gameCards, 1).hasPair,
      isBankerPair = getPlayerHand(gameCards, 2).hasPair,
      PlayerCards = updatedPlayerCards,
      BankerCards = updatedBankerCards,
    )

    index match {
      case 1 => updatedState.sendCardMsgToClients("player", getPlayerHand(gameCards, index).score, Card.parseGameCard(card).CardName, Card.parseGameCard(card).CardValue, admins, toppers, clients)
      case _ => updatedState.sendCardMsgToClients("banker", getPlayerHand(gameCards, index).score, Card.parseGameCard(card).CardName, Card.parseGameCard(card).CardValue, admins, toppers, clients)
    }


    updatedState
  }

  def handleCardSqueezedMsg(tableState: TableState, squeezedCard: SqueezedCard): TableState = {
    /*
    * Read the Inputs
    *
    * handIndex //[1,2] => 1. player 2. banker
    * cardIndex //[0, 1, 2] =>
    * PlayerCards
    * BankerCards
    *
    * 1. Either Banker Card or Player Card gets Squeezed
    * */
    val handIndex = squeezedCard.hand
    val cardIndex = squeezedCard.index
    val playerCards = tableState.PlayerCards
    val bankerCards = tableState.BankerCards


    val updatedBankerCards = if (handIndex == 1) bankerCards else bankerCards.updated(cardIndex, bankerCards(cardIndex).copy(squeezed = true))
    val updatedPlayerCards = if (handIndex == 2) playerCards else playerCards.updated(cardIndex, playerCards(cardIndex).copy(squeezed = true))


    val updatedState = tableState.copy(
      BankerCards = updatedBankerCards,
      PlayerCards = updatedPlayerCards
    )

    handIndex match {
      case 1 => updatedState.sendCardSqueezedMsgToClients("player", cardIndex = cardIndex, admins, toppers, clients)
      case _ => updatedState.sendCardSqueezedMsgToClients("banker", cardIndex = cardIndex, admins, toppers, clients)
    }

    updatedState
  }


  def handleGameCompletedState(tableState: TableState,
                               gameCards: Array[List[String]],
                               card: String,
                               index: Int,
                               rnd: Int): TableState = {

    val handIndex = index
    val cardIndex = 4 + rnd
    val playerCards = tableState.PlayerCards
    val bankerCards = tableState.BankerCards

    val updatedBankerCards = if (handIndex == 1) bankerCards else bankerCards :+ Card.parseGameCard(gameCards(2)(cardIndex))
    val updatedPlayerCards = if (handIndex == 2) playerCards else playerCards :+ Card.parseGameCard(gameCards(1)(cardIndex))


    val winnerIndex = if (getPlayerHand(gameCards, 1).score == getPlayerHand(gameCards, 2).score) {
      0
    }
    else if (getPlayerHand(gameCards, 1).score > getPlayerHand(gameCards, 2).score) {
      1
    }
    else {
      2
    }

    val winCards = gameCards(winnerIndex)
    gameCards(winnerIndex) = gameCards(winnerIndex).updated(1, "Winner")
    gameCards(1) = gameCards(1).updated(3, if (getPlayerHand(gameCards, 1).hasPair) "Pair" else "")
    gameCards(1) = gameCards(1).updated(4, if (getPlayerHand(gameCards, 1).isNatural) "Natural" else "")

    gameCards(2) = gameCards(2).updated(3, if (getPlayerHand(gameCards, 2).hasPair) "Pair" else "")
    gameCards(2) = gameCards(2).updated(4, if (getPlayerHand(gameCards, 2).isNatural) "Natural" else "")

    val updatedState = tableState.copy(
      gameStatus = "GAME_RESULT",
      waitingCounter = 0,
      WinningHand = getPlayerName(gameCards, winnerIndex),
      CardHandValue = getPlayerHand(gameCards, winnerIndex).score,
      playerHandValue = getPlayerHand(gameCards, 1).score,
      bankerHandValue = getPlayerHand(gameCards, 2).score,
      isPlayerPair = getPlayerHandFirst2Cards(gameCards, 1).hasPair,
      isBankerPair = getPlayerHandFirst2Cards(gameCards, 2).hasPair,
      isSuitedTie = getPlayerName(gameCards, winnerIndex) == "Tie",
      isNaturalHand = getPlayerHand(gameCards, winnerIndex).isNatural,
      PlayerCards = updatedPlayerCards,
      BankerCards = updatedBankerCards,
      winCards = winCards,

      History = (tableState.History :+ WinResult(
        roundId = tableState.roundId,
        isPlayerPair = getPlayerHandFirst2Cards(gameCards, 1).hasPair,
        isBankerPair = getPlayerHandFirst2Cards(gameCards, 2).hasPair,
        isNaturalHand = getPlayerHand(gameCards, winnerIndex).isNatural,
        isSuitedTie = getPlayerName(gameCards, winnerIndex) == "Tie",
        BankerCards = updatedBankerCards,
        PlayerCards = updatedPlayerCards,
        playerHandValue = getPlayerHand(gameCards, 1).score,
        bankerHandValue = getPlayerHand(gameCards, 2).score,
        CardHandValue = getPlayerHand(gameCards, winnerIndex).score,
        WinningHand = getPlayerName(gameCards, winnerIndex)
      )).takeRight(100)
    )

    index match {
      case 1 => updatedState.sendCardMsgToClients("player", getPlayerHand(gameCards, index).score, Card.parseGameCard(card).CardName, Card.parseGameCard(card).CardValue, admins, toppers, clients)
      case _ => updatedState.sendCardMsgToClients("banker", getPlayerHand(gameCards, index).score, Card.parseGameCard(card).CardName, Card.parseGameCard(card).CardValue, admins, toppers, clients)
    }

//    val gameResult = GameResult(
//      roundId = tableState.roundId,
//      isPlayerPair = getPlayerHandFirst2Cards(gameCards, 1).hasPair,
//      isBankerPair = getPlayerHandFirst2Cards(gameCards, 2).hasPair,
//      isNaturalHand = getPlayerHand(gameCards, winnerIndex).isNatural,
//      isSuitedTie = getPlayerName(gameCards, winnerIndex) == "Tie",
//      BankerCards = gameCards(2).drop(5) map (x => Card.parseGameCard(x)),
//      PlayerCards = gameCards(1).drop(5).map(x => Card.parseGameCard(x)),
//      playerHandValue = getPlayerHand(gameCards, 1).score,
//      bankerHandValue = getPlayerHand(gameCards, 2).score,
//      CardHandValue = getPlayerHand(gameCards, winnerIndex).score,
//      WinningHand = getPlayerName(gameCards, winnerIndex)
//    )
//
//    clients.foreach {
//      client =>
//        val betsList = client._2.betsList
//        val totalBetsValue = getTotalBetsValue(betsList = betsList)
//        if (totalBetsValue > 0) {
//          /* Get the wonBetsList */
//          val wonBetsList = getWonBetsList(betsList = betsList, gameResult = gameResult)
//          val totalWonBetsValue = getTotalWonBetsValue(wonBetsList = wonBetsList)
//
//
//          if (totalWonBetsValue > 0) {
//
//            /* Clients With Bets, Won Bets Send
//            *   - Current Balance
//            *   - Won Bets
//            * */
//            val wonBetsMsg = WonBetsMsg(
//              clientId = client._2.uid,
//              RoundTripStartTime = Instant.now.getEpochSecond,
//              WinningBets = wonBetsList
//            )
//
//            client._2.client ! Json.toJson(wonBetsMsg)
//
//            val currentBalanceMessage = CurrentBalanceMessage("CURRENT_BALANCE",
//              tableId = "32100",
//              destination = "player",
//              clientId = client._2.uid,
//              roundId = tableState.roundId,
//              gameType = "Baccarat",
//              roundTripStartTime = Instant.now.getEpochSecond,
//              timestamp = "timestamp",
//              balance = client._2.balance + totalWonBetsValue,
//              "INR",
//            )
//
//            client._2.client ! Json.toJson(currentBalanceMessage)
//
//
//          } else {
//            /* Clients With Bets, Do not send anything but make sure Reports are updated with NoWin
//            * */
//
//          }
//
//          /* Clients With Bets, Send
//          *   - Game Results
//          * */
//          val gameResultMsg = GameResultMsg(
//            destination = "player",
//            roundId = tableState.roundId,
//            timestamp = Instant.now().toString,
//            WinAmount = totalWonBetsValue,
//            GameResults = gameResult
//          )
//
//          client._2.client ! Json.toJson(gameResultMsg)
//
//
//        } else {
//          /* Clients Without Bet, Send
//          *   - Game Results
//          * */
//          val gameResultMsg = GameResultMsg(
//            destination = "player",
//            roundId = tableState.roundId,
//            timestamp = Instant.now().toString,
//            WinAmount = 0,
//            GameResults = gameResult
//          )
//
//          client._2.client ! Json.toJson(gameResultMsg)
//        }
//
//    }
//
//    updatedState.sendGameResultMsgToClients(admins, toppers)

    updatedState

  }


  def doGameCompletedAnnouncement(tableState: TableState) = {


    val gameResult = GameResult(
      roundId = tableState.roundId,
      isPlayerPair = tableState.isPlayerPair,
      isBankerPair = tableState.isBankerPair,
      isNaturalHand = tableState.isNaturalHand,
      isSuitedTie = tableState.isSuitedTie,
      BankerCards = tableState.BankerCards,
      PlayerCards = tableState.PlayerCards,
      playerHandValue = tableState.playerHandValue,
      bankerHandValue = tableState.bankerHandValue,
      CardHandValue = tableState.CardHandValue,
      WinningHand = tableState.WinningHand
    )

    clients.foreach {
      client =>
        val betsList = client._2.betsList
        val totalBetsValue = getTotalBetsValue(betsList = betsList)
        if (totalBetsValue > 0) {
          /* Get the wonBetsList */
          val wonBetsList = getWonBetsList(betsList = betsList, gameResult = gameResult)
          val totalWonBetsValue = getTotalWonBetsValue(wonBetsList = wonBetsList)


          if (totalWonBetsValue > 0) {

            /* Clients With Bets, Won Bets Send
            *   - Current Balance
            *   - Won Bets
            * */
            val wonBetsMsg = WonBetsMsg(
              clientId = client._2.uid,
              RoundTripStartTime = Instant.now.getEpochSecond,
              WinningBets = wonBetsList
            )

            client._2.client ! Json.toJson(wonBetsMsg)

            val currentBalanceMessage = CurrentBalanceMessage("CURRENT_BALANCE",
              tableId = "32100",
              destination = "player",
              clientId = client._2.uid,
              roundId = tableState.roundId,
              gameType = "Baccarat",
              roundTripStartTime = Instant.now.getEpochSecond,
              timestamp = "timestamp",
              balance = client._2.balance + totalWonBetsValue,
              "INR",
            )

            client._2.client ! Json.toJson(currentBalanceMessage)


          } else {
            /* Clients With Bets, Do not send anything but make sure Reports are updated with NoWin
            * */

          }

          /* Clients With Bets, Send
          *   - Game Results
          * */
          val gameResultMsg = GameResultMsg(
            destination = "player",
            roundId = tableState.roundId,
            timestamp = Instant.now().toString,
            WinAmount = totalWonBetsValue,
            GameResults = gameResult
          )

          client._2.client ! Json.toJson(gameResultMsg)


        } else {
          /* Clients Without Bet, Send
          *   - Game Results
          * */
          val gameResultMsg = GameResultMsg(
            destination = "player",
            roundId = tableState.roundId,
            timestamp = Instant.now().toString,
            WinAmount = 0,
            GameResults = gameResult
          )

          client._2.client ! Json.toJson(gameResultMsg)
        }

    }

    tableState.sendGameResultMsgToClients(admins, toppers)

  }


  def handleTopperConnected(tableState: TableState, playerIp: String, actor: ActorRef, client: ActorRef): Unit = {

    if (toppers.contains(playerIp)) {
      toppers(playerIp) = toppers(playerIp).copy(actor = actor, client = client)
      logManagerActor ! AddLog(logType = "warning", runtimeClass = "Baccarat", content = s"Topper Re-Connected from ${playerIp}")
    } else {
      toppers = toppers ++ Map(playerIp -> ClientData(actor = actor, client = client))
      logManagerActor ! AddLog(logType = "warning", runtimeClass = "Baccarat", content = s"Topper Connected from ${playerIp}")

    }

    tableState.sendInitialDataTopperMsg(toppers(playerIp))
    tableState.sendInitialConfigMsg(toppers(playerIp))


  }

  def handleInfoPaperShow(tableState: TableState, show: Boolean) = {
    log.info("received config update from admin")
    val updatedState = tableState.copy(configData = tableState.configData.copy(showInfoPaper = show))
    updatedState.sendConfigUpdateMsg(toppers = toppers, admins = admins)
    updatedState
  }

  def handleConfigUpdate(tableState: TableState, configData: ConfigData) = {
    val updatedState = tableState.copy(configData = configData)
    updatedState.sendConfigUpdateMsg(toppers = toppers, admins = admins)
    updatedState
  }

  def handleShuffleDeck(tableState: TableState) = {
    val updatedState = tableState.copy()
    updatedState.sendShuffleDeckMsg(toppers = toppers, admins = admins)
    updatedState
  }

  def handleCancelPrevGame(tableState: TableState) = {
    val updatedState = tableState.copy(
      History = tableState.History.dropRight(1).take(100)
    )
    updatedState.sendInitialDataTopperMsg(toppers = toppers)
    updatedState
  }

  def handlePlayerBetPlaced(tableState: TableState, playerIp: String, betsList: BetsList, client: ActorRef): TableState = {
    /*
    * if clients map has this entry
    * 1. update the client's balance
    * 2. update the client's betsList
    * 3. send currentBalanceMessage to the client
    * 4. Note PlayerGameTransaction
    * */
    if (clients.contains(playerIp)) {
      val oldBalance = clients(playerIp).balance
      val totalBetValue = {
        import betsList.{TieBet => tieBetAmount, BankerBet => bankerBetAmount, PlayerBet => playerBetAmount}
        import betsList.SideBets.{PlayerPair => playerPairAmount, BankerPair => bankerPairAmount, EitherPair => eitherPairAmount, PerfectPair => perfectPairAmount}

        tieBetAmount + bankerBetAmount + playerBetAmount + playerPairAmount + bankerPairAmount + eitherPairAmount + perfectPairAmount
      }

      /*
      * 1. update the client's balance
      * 2. update the client's betsList
      * */
      clients(playerIp) = clients(playerIp).copy(
        balance = oldBalance - totalBetValue,
        betsList = betsList
      )

      /*
      * 3. send currentBalanceMessage to the client
      * */
      val currentBalanceMessage = CurrentBalanceMessage(
        MessageType = "CURRENT_BALANCE",
        tableId = tableId,
        destination = "player",
        clientId = playerIp,
        roundId = tableState.roundId,
        gameType = "Baccarat",
        roundTripStartTime = Instant.now.getEpochSecond,
        timestamp = dateFormat.format(Calendar.getInstance().getTime),
        balance = clients(playerIp).balance,
        sessionCurrency = "INR"
      )

      client ! Json.toJson(currentBalanceMessage)

      /*
      * 4. Note PlayerGameTransaction
      * */
      /*
      * Update the balance of the player
      * 1. Update local data structure
      * 2. Update the Service - Player Update
      * 3. Update the Service - Money Transaction
      * 4. Inform the connected admins about
      *     - Money Transaction
      *     - Player Update
      * */
      val player = players.find(p => p.uid == clients(playerIp).uid).get
      players = players.filter(p => p.uid != clients(playerIp).uid).:+(player.copy(balance = clients(playerIp).balance))
      baccaratSSeaterTableService.updatePlayerData(players.find(p => p.uid == clients(playerIp).uid).get, clients(playerIp).uid)
      baccaratSSeaterTableService.addTransaction(
        MoneyTransactionMsg(
          MessageType = "PLAYER_BET_PLACED",
          transType = "Bet",
          playerIp = player.clientIp,
          roundId = tableState.roundId,
          amount = totalBetValue,
          oldBalance = oldBalance,
          newBalance = clients(playerIp).balance,
          timestamp = dateFormat.format(Calendar.getInstance().getTime))
      )

      admins.foreach {
        admin =>
          admin._2.client ! Json.toJson(
            MoneyTransactionMsg(
              MessageType = "PLAYER_BET_PLACED",
              transType = "Bet",
              playerIp = player.clientIp,
              roundId = tableState.roundId,
              amount = totalBetValue,
              oldBalance = oldBalance,
              newBalance = clients(playerIp).balance,
              timestamp = dateFormat.format(Calendar.getInstance().getTime))
          )

          admin._2.client ! Json.toJson(
            PlayerUpdatedMsg(
              MessageType = "PLAYER_UPDATED",
              player = players.find(p => p.uid == clients(playerIp).uid).get,
              timestamp = dateFormat.format(Calendar.getInstance().getTime))
          )
      }
      tableState

    } else {
      tableState
    }
  }

  def handleAdminConnected(tableState: TableState, adminIp: String, actor: ActorRef, client: ActorRef): Unit = {

    players = baccaratSSeaterTableService.getPlayersData(tableId)

    if (admins.contains(adminIp)) {
      admins(adminIp) = admins(adminIp).copy(actor = actor, client = client)
      logManagerActor ! AddLog(logType = "warning", runtimeClass = "Baccarat", content = s"Admin Re-Connected from ${adminIp}")
    } else {
      admins = admins ++ Map(adminIp -> AdminClientData(actor = actor, client = client))
      logManagerActor ! AddLog(logType = "warning", runtimeClass = "Baccarat", content = s"Topper Admin from ${adminIp}")
    }

    tableState.sendInitialDataAdminMsg(
      players = players,
      transactions = baccaratSSeaterTableService.getTransactions,
      operations = baccaratSSeaterTableService.getOperationTransactions,
      admins(adminIp)
    )

    tableState.sendInitialConfigMsg(admins(adminIp))

  }

  def handlePlayerConnected(tableState: TableState, playerIp: String, actor: ActorRef, client: ActorRef): TableState = {
    if (clients.contains(playerIp)) {
      log.info("Player Trying to Reconnect...")
      val playerUid = clients(playerIp).uid
      clients(playerIp) = clients(playerIp).copy(actor = actor, client = client)
      logManagerActor ! AddLog(logType = "warning", runtimeClass = "Baccarat", content = s"${playerUid} Re-Connected from ${playerIp}")

      log.info(s"ReConnected as Player ${playerUid} from ${playerIp}")
      tableState.sendInitialDataPlayerMsg(clients(playerIp))
      tableState.sendInitialConfigMsg(clients(playerIp))

      tableState

    } else {
      log.info("A Player Trying to connect...")

      val playerOpt = baccaratSSeaterTableService.getPlayersData(tableId = "4000").find(_.clientIp == playerIp)
      playerOpt match {
        case Some(matchingPlayer) =>
          /*Straight Case - A Player Trying to connect from a known ip*/
          val playerUid = matchingPlayer.uid
          clients = clients ++ Map(playerIp -> ClientData(playerIp = playerIp, uid = playerUid, actor = actor, client = client, betsList = BetsList(), balance = matchingPlayer.balance))
          logManagerActor ! AddLog(logType = "warning", runtimeClass = "Baccarat", content = s"${playerUid} Connected from ${playerIp}")
          self ! PlayerStatusOnline(playerUid)


          log.info(s"Connected as Player ${playerUid} from ${playerIp}")
          tableState.sendInitialDataPlayerMsg(clients(playerIp))
          tableState.sendInitialConfigMsg(clients(playerIp))

          tableState

        case None =>
          /*Special Case - A Player Trying to connect from an unknown ip
          * 1. find a seat which is not connected and has empty balance
          * */

          val playerData = baccaratSSeaterTableService.getPlayerData(playerIp);
          clients = clients ++ Map(playerIp -> ClientData(playerIp = playerIp, uid = playerData.uid, actor = actor, client = client, betsList = BetsList(), balance = playerData.balance))
          self ! PlayerStatusOnline(playerData.uid)

          tableState.sendInitialDataPlayerMsg(clients(playerIp))
          tableState.sendInitialConfigMsg(clients(playerIp))

          log.info("Guest Player Connected...")
          tableState
      }

    }


  }

  def handlePlayerDisconnected(tableState: TableState, playerIp: String): TableState = {
    if (clients.contains(playerIp)) {
      val playerUid = clients(playerIp).uid

      clients.remove(playerIp)
      self ! PlayerStatusOffline(playerUid)

      logManagerActor ! AddLog(runtimeClass = "BaccaratTableActor", logType = "warning", content = s"Client ${playerIp} Disconnecting..")
      log.info(s"Client ${playerIp} Disconnecting..")

      if (tableState.seats.exists(_.uid == playerUid)) {
        logManagerActor ! AddLog(runtimeClass = "BaccaratTableActor", logType = "warning", content = s"Client ${playerIp} Disconnected From Seat uid ${playerUid}")
        log.info(s"Client ${playerIp} Disconnected From Seat uid${playerUid}")

        tableState.copy(
          seats = tableState.seats.map(seat => if (seat.uid == playerUid) seat.copy(connected = false) else seat)
        )


      } else {
        tableState
      }

    } else {
      tableState
    }

  }

  def handlePlayerBalanceUpdated(tableState: TableState, playerIp: String, balance: Double): TableState = {
    if (clients.contains(playerIp)) {
      clients(playerIp) = clients(playerIp).copy(balance = balance)

      tableState.sendCurrentBalanceMsg(clients(playerIp))
      logManagerActor ! AddLog(runtimeClass = "RouletteActor", content = s"${playerIp}  Balance Updated! Balance Now=${clients(playerIp).balance}")
      log.info(s"Player IP ${playerIp} Balance = ${clients(playerIp).balance}")
    }
    tableState
  }

  def handlePlayerNameUpdated(tableState: TableState, uid: String, nickname: String): TableState = {
    players = baccaratSSeaterTableService.getPlayersData(tableId)

    val player = players.find(p => p.uid == uid).get
    val foundIndex = players.indexWhere(p => p.uid == uid)

    players = players.updated(foundIndex, player.copy(nickname = nickname))
    baccaratSSeaterTableService.updatePlayerData(player.copy(nickname = nickname), uid)

    val MessageType = "PLAYER_UPDATED"

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(
          PlayerUpdatedMsg(MessageType = MessageType, player = players.find(p => p.uid == uid).get, timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )
    }

    baccaratSSeaterTableService.addOperationTransaction(
      OperationTransactionMsg(MessageType = MessageType,
        transType = "Operation",
        uid = players.find(p => p.uid == uid).get.uid,
        nickname = players.find(p => p.uid == uid).get.nickname,
        client_ip = players.find(p => p.uid == uid).get.clientIp,
        status = players.find(p => p.uid == uid).get.status,
        usage = players.find(p => p.uid == uid).get.usage,
        timestamp = dateFormat.format(Calendar.getInstance().getTime))
    )

    tableState
  }

  def handlePlayerIpUpdated(tableState: TableState, uid: String, ip: String): TableState = {
    players = baccaratSSeaterTableService.getPlayersData(tableId)

    val player = players.find(p => p.uid == uid).get
    val foundIndex = players.indexWhere(p => p.uid == uid)

    players = players.updated(foundIndex, player.copy(clientIp = ip))
    baccaratSSeaterTableService.updatePlayerData(players.find(p => p.uid == uid).get, uid)

    val MessageType = "PLAYER_UPDATED"

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(
          PlayerUpdatedMsg(MessageType = MessageType, player = players.find(p => p.uid == uid).get, timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )
    }

    baccaratSSeaterTableService.addOperationTransaction(
      OperationTransactionMsg(MessageType = MessageType,
        transType = "Operation",
        uid = players.find(p => p.uid == uid).get.uid,
        nickname = players.find(p => p.uid == uid).get.nickname,
        client_ip = players.find(p => p.uid == uid).get.clientIp,
        status = players.find(p => p.uid == uid).get.status,
        usage = players.find(p => p.uid == uid).get.usage,
        timestamp = dateFormat.format(Calendar.getInstance().getTime))
    )

    tableState
  }


  def handleMoneyTransaction(tableState: TableState, moneyTransaction: MoneyTransaction, actor: ActorRef, client: ActorRef): Unit = {

    val player = players.find(p => p.uid == moneyTransaction.uid).get
    val currentBalance = player.balance

    moneyTransaction.transType match {
      case "DEPOSIT" => {
        client ! Json.toJson(
          MoneyTransactionMsg(MessageType = "DEPOSIT_REQ",
            playerIp = moneyTransaction.uid,
            amount = moneyTransaction.amount,
            oldBalance = currentBalance,
            newBalance = players.find(p => p.uid == moneyTransaction.uid).get.balance,
            timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )
        baccaratSSeaterTableService.addTransaction(
          MoneyTransactionMsg(MessageType = "DEPOSIT_REQ",
            playerIp = moneyTransaction.uid,
            amount = moneyTransaction.amount,
            oldBalance = currentBalance,
            newBalance = players.find(p => p.uid == moneyTransaction.uid).get.balance,
            timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )

        players = players.filter(p => p.uid != moneyTransaction.uid).:+(player.copy(balance = currentBalance + moneyTransaction.amount))

        client ! Json.toJson(
          MoneyTransactionMsg(MessageType = "DEPOSIT_SUCCESS",
            playerIp = moneyTransaction.uid,
            amount = moneyTransaction.amount,
            oldBalance = currentBalance,
            newBalance = players.find(p => p.uid == moneyTransaction.uid).get.balance,
            timestamp = dateFormat.format(Calendar.getInstance().getTime)))

        baccaratSSeaterTableService.addTransaction(
          MoneyTransactionMsg(MessageType = "DEPOSIT_SUCCESS",
            playerIp = moneyTransaction.uid,
            amount = moneyTransaction.amount,
            oldBalance = currentBalance,
            newBalance = players.find(p => p.uid == moneyTransaction.uid).get.balance,
            timestamp = dateFormat.format(Calendar.getInstance().getTime)))

      }
      case "WITHDRAW" => {
        client ! Json.toJson(
          MoneyTransactionMsg(MessageType = "WITHDRAW_REQ",
            playerIp = moneyTransaction.uid,
            amount = moneyTransaction.amount,
            oldBalance = currentBalance,
            newBalance = players.find(p => p.uid == moneyTransaction.uid).get.balance,
            timestamp = dateFormat.format(Calendar.getInstance().getTime)))

        baccaratSSeaterTableService.addTransaction(
          MoneyTransactionMsg(MessageType = "WITHDRAW_REQ",
            playerIp = moneyTransaction.uid,
            amount = moneyTransaction.amount,
            oldBalance = currentBalance,
            newBalance = players.find(p => p.uid == moneyTransaction.uid).get.balance,
            timestamp = dateFormat.format(Calendar.getInstance().getTime)))

        players = players.filter(p => p.uid != moneyTransaction.uid).:+(player.copy(balance = currentBalance - moneyTransaction.amount))

        client ! Json.toJson(
          MoneyTransactionMsg(MessageType = "WITHDRAW_SUCCESS",
            playerIp = moneyTransaction.uid,
            amount = moneyTransaction.amount,
            oldBalance = currentBalance,
            newBalance = players.find(p => p.uid == moneyTransaction.uid).get.balance,
            timestamp = dateFormat.format(Calendar.getInstance().getTime)))
        baccaratSSeaterTableService.addTransaction(
          MoneyTransactionMsg(MessageType = "WITHDRAW_SUCCESS",
            playerIp = moneyTransaction.uid,
            amount = moneyTransaction.amount,
            oldBalance = currentBalance,
            newBalance = players.find(p => p.uid == moneyTransaction.uid).get.balance,
            timestamp = dateFormat.format(Calendar.getInstance().getTime)))

      }
    }


    baccaratSSeaterTableService.updatePlayerData(players.find(p => p.uid == moneyTransaction.uid).get, moneyTransaction.uid)

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(
          PlayerUpdatedMsg(MessageType = "PLAYER_UPDATED", player = players.find(p => p.uid == moneyTransaction.uid).get, timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )
    }

    //Important!! Some Magic ?
    val impactedPlayer = players.find(p => p.uid == moneyTransaction.uid).get
    val updatedBalance = impactedPlayer.balance
    val playerIp = impactedPlayer.clientIp

    if (clients.contains(playerIp)) {
      clients(playerIp) = clients(playerIp).copy(balance = updatedBalance)

      tableState.sendCurrentBalanceMsg(clients(playerIp))
      logManagerActor ! AddLog(runtimeClass = "RouletteActor", content = s"${playerIp}  Balance Updated! Balance Now=${clients(playerIp).balance}")
      log.info(s"Player IP ${playerIp} Balance = ${clients(playerIp).balance}")
    }
  }

  def handleGameTransaction(gameTransaction: GameTransaction): Unit = {

    /*
    * TODO in Game Transaction, player field is filled with uid of the seat
    * */
    log.info(s"Game Transaction ${gameTransaction.player} ${gameTransaction.transType} Bet=${gameTransaction.totalBet} Win=${gameTransaction.totalWin} Rake=${gameTransaction.rake}")
    val player = players.find(p => p.uid == gameTransaction.player).get
    val currentBalance = player.balance
    var MessageType = "Unknown"
    var amount = 0.0
    var newBalance = currentBalance

    gameTransaction.transType match {
      case "Bet" =>
        MessageType = "PLAYER_BET_PLACED"
        amount = gameTransaction.totalBet
        newBalance = newBalance - amount
      case "Win" =>
        MessageType = "PLAYER_BET_WON"
        amount = gameTransaction.totalWin
        newBalance = newBalance + amount
      case "NoWin" =>
        MessageType = "PLAYER_BET_LOST"
        amount = gameTransaction.totalBet
    }


    players = players.filter(p => p.uid != gameTransaction.player).:+(player.copy(balance = newBalance))
    baccaratSSeaterTableService.updatePlayerData(players.find(p => p.uid == gameTransaction.player).get, gameTransaction.player)


    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(
          MoneyTransactionMsg(MessageType = MessageType,
            transType = gameTransaction.transType,
            playerIp = gameTransaction.player,
            rake = gameTransaction.rake,
            roundId = gameTransaction.roundId,
            amount = amount,
            oldBalance = currentBalance,
            newBalance = players.find(p => p.uid == gameTransaction.player).get.balance,
            timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )

        admin._2.client ! Json.toJson(
          PlayerUpdatedMsg(MessageType = "PLAYER_UPDATED", player = players.find(p => p.uid == gameTransaction.player).get, timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )
    }

    baccaratSSeaterTableService.addTransaction(
      MoneyTransactionMsg(MessageType = MessageType,
        transType = gameTransaction.transType,
        playerIp = gameTransaction.player,
        rake = gameTransaction.rake,
        roundId = gameTransaction.roundId,
        amount = amount,
        oldBalance = currentBalance,
        newBalance = players.find(p => p.uid == gameTransaction.player).get.balance,
        timestamp = dateFormat.format(Calendar.getInstance().getTime))
    )

  }

  def handlePlayerStatus(uid: String, status: String): Unit = {
    players = baccaratSSeaterTableService.getPlayersData(tableId)

    val player = players.find(p => p.uid == uid).get
    val foundIndex = players.indexWhere(p => p.uid == uid)

    var MessageType = "Unknown"

    status match {
      case "offline" =>
        MessageType = "PLAYER_OFFLINE"
        players = players.updated(foundIndex, player.copy(status = "offline"))
      case "online" =>
        MessageType = "PLAYER_ONLINE"
        players = players.updated(foundIndex, player.copy(status = "online"))
    }

    baccaratSSeaterTableService.updatePlayerData(players.find(p => p.uid == uid).get, uid)

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(
          PlayerUpdatedMsg(MessageType = MessageType, player = players.find(p => p.uid == uid).get, timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )
    }

    baccaratSSeaterTableService.addOperationTransaction(
      OperationTransactionMsg(MessageType = MessageType,
        transType = "Operation",
        uid = players.find(p => p.uid == uid).get.uid,
        nickname = players.find(p => p.uid == uid).get.nickname,
        client_ip = players.find(p => p.uid == uid).get.clientIp,
        status = players.find(p => p.uid == uid).get.status,
        usage = players.find(p => p.uid == uid).get.usage,
        timestamp = dateFormat.format(Calendar.getInstance().getTime))
    )


  }

  def handlePlayerNameUpdate(uid: String, nickname: String): Unit = {
    players = baccaratSSeaterTableService.getPlayersData(tableId)

    val player = players.find(p => p.uid == uid).get
    val foundIndex = players.indexWhere(p => p.uid == uid)

    players = players.updated(foundIndex, player.copy(nickname = nickname))
    baccaratSSeaterTableService.updatePlayerData(player.copy(nickname = nickname), uid)

    val MessageType = "PLAYER_UPDATED"

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(
          PlayerUpdatedMsg(MessageType = MessageType, player = players.find(p => p.uid == uid).get, timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )
    }

    baccaratSSeaterTableService.addOperationTransaction(
      OperationTransactionMsg(MessageType = MessageType,
        transType = "Operation",
        uid = players.find(p => p.uid == uid).get.uid,
        nickname = players.find(p => p.uid == uid).get.nickname,
        client_ip = players.find(p => p.uid == uid).get.clientIp,
        status = players.find(p => p.uid == uid).get.status,
        usage = players.find(p => p.uid == uid).get.usage,
        timestamp = dateFormat.format(Calendar.getInstance().getTime))
    )

    //    self ! SeatNameUpdated(uid, nickname)

  }

  def handlePlayerIpUpdate(uid: String, ip: String): Unit = {
    players = baccaratSSeaterTableService.getPlayersData(tableId)

    val player = players.find(p => p.uid == uid).get
    val foundIndex = players.indexWhere(p => p.uid == uid)

    players = players.updated(foundIndex, player.copy(clientIp = ip))
    baccaratSSeaterTableService.updatePlayerData(players.find(p => p.uid == uid).get, uid)

    val MessageType = "PLAYER_UPDATED"

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(
          PlayerUpdatedMsg(MessageType = MessageType, player = players.find(p => p.uid == uid).get, timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )
    }

    baccaratSSeaterTableService.addOperationTransaction(
      OperationTransactionMsg(MessageType = MessageType,
        transType = "Operation",
        uid = players.find(p => p.uid == uid).get.uid,
        nickname = players.find(p => p.uid == uid).get.nickname,
        client_ip = players.find(p => p.uid == uid).get.clientIp,
        status = players.find(p => p.uid == uid).get.status,
        usage = players.find(p => p.uid == uid).get.usage,
        timestamp = dateFormat.format(Calendar.getInstance().getTime))
    )

    //Inform the poker table actor about the Seat IP change
    //    self ! SeatIpUpdated(uid, ip)

  }


  def handleGuestPlayerConnected(player: Player): Unit = {

    admins.foreach {
      admin =>
        admin._2.client ! Json.toJson(
          PlayerCreatedMsg(MessageType = "PLAYER_CREATED", player = player, timestamp = dateFormat.format(Calendar.getInstance().getTime))
        )
    }

    baccaratSSeaterTableService.addOperationTransaction(
      OperationTransactionMsg(MessageType = "PLAYER_CREATED",
        transType = "Operation",
        uid = player.uid,
        nickname = player.nickname,
        client_ip = player.clientIp,
        status = player.status,
        usage = player.usage,
        timestamp = dateFormat.format(Calendar.getInstance().getTime))
    )

  }

  def handleTableSettingsChange(tableState: TableState, dealer: String, minBet: Int, maxBet: Int): TableState = {
    tableState.copy(

    )
  }

  def handleGameCompleted(tableState: TableState, winnerIndex: Int) = {
    val gameCards = tableState.gameCards

    val winScore = getPlayerHand(gameCards, winnerIndex).score.toString
    val winCards = gameCards(winnerIndex)
    gameCards(winnerIndex) = gameCards(winnerIndex).updated(1, "Winner")
    gameCards(1) = gameCards(1).updated(3, if (getPlayerHand(gameCards, 1).hasPair) "Pair" else "")
    gameCards(1) = gameCards(1).updated(4, if (getPlayerHand(gameCards, 1).isNatural) "Natural" else "")

    gameCards(2) = gameCards(2).updated(3, if (getPlayerHand(gameCards, 2).hasPair) "Pair" else "")
    gameCards(2) = gameCards(2).updated(4, if (getPlayerHand(gameCards, 2).isNatural) "Natural" else "")
    //
    //    val winner = (getPlayerName(gameCards, winnerIndex),
    //      getPlayerHand(gameCards, 1).hasPair,
    //      getPlayerHand(gameCards, 2).hasPair,
    //      (getPlayerHand(gameCards, 2).score == 6),
    //      getPlayerHand(gameCards, winnerIndex).isNatural) match {
    //      //PlayerWin - 3! + 2!
    //      case ("Player", false, false, _, false) => "PLAYERWIN"
    //      case ("Player", true, false, _, false) => "PLAYERWIN-PLAYERPAIR"
    //      case ("Player", false, true, _, false) => "PLAYERWIN-BANKERPAIR"
    //      case ("Player", true, true, _, false) => "PLAYERWIN-PLAYERPAIR-BANKERPAIR"
    //      case ("Player", false, false, _, true) => "PLAYERWIN-NATURAL"
    //      case ("Player", true, false, _, true) => "PLAYERWIN-PLAYERPAIR-NATURAL"
    //      case ("Player", false, true, _, true) => "PLAYERWIN-BANKERPAIR-NATURAL"
    //      case ("Player", true, true, _, true) => "PLAYERWIN-PLAYERPAIR-BANKERPAIR-NATURAL"
    //
    //      //Banker - 3! + 2!
    //      case ("Banker", false, false, false, false) => "BANKERWIN"
    //      case ("Banker", true, false, false, false) => "BANKERWIN-PLAYERPAIR"
    //      case ("Banker", false, true, false, false) => "BANKERWIN-BANKERPAIR"
    //      case ("Banker", true, true, false, false) => "BANKERWIN-PLAYERPAIR-BANKERPAIR"
    //      case ("Banker", false, false, false, true) => "BANKERWIN-NATURAL"
    //      case ("Banker", true, false, false, true) => "BANKERWIN-PLAYERPAIR-NATURAL"
    //      case ("Banker", false, true, false, true) => "BANKERWIN-BANKERPAIR-NATURAL"
    //      case ("Banker", true, true, false, true) => "BANKERWIN-PLAYERPAIR-BANKERPAIR-NATURAL"
    //
    //      case ("Banker", false, false, true, false) => "BANKERWIN-SUPER6"
    //      case ("Banker", true, false, true, false) => "BANKERWIN-PLAYERPAIR-SUPER6"
    //      case ("Banker", false, true, true, false) => "BANKERWIN-BANKERPAIR-SUPER6"
    //      case ("Banker", true, true, true, false) => "BANKERWIN-PLAYERPAIR-BANKERPAIR-SUPER6"
    //      case ("Banker", false, false, true, true) => "BANKERWIN-SUPER6-NATURAL"
    //      case ("Banker", true, false, true, true) => "BANKERWIN-PLAYERPAIR-SUPER6-NATURAL"
    //      case ("Banker", false, true, true, true) => "BANKERWIN-BANKERPAIR-SUPER6-NATURAL"
    //      case ("Banker", true, true, true, true) => "BANKERWIN-PLAYERPAIR-BANKERPAIR-SUPER6-NATURAL"
    //
    //      //Tie - 3! + 2!
    //      case ("Tie", false, false, _, false) => "TIEWIN"
    //      case ("Tie", true, false, _, false) => "TIEWIN-PLAYERPAIR"
    //      case ("Tie", false, true, _, false) => "TIEWIN-BANKERPAIR"
    //      case ("Tie", true, true, _, false) => "TIEWIN-PLAYERPAIR-BANKERPAIR"
    //      case ("Tie", false, false, _, true) => "TIEWIN-NATURAL"
    //      case ("Tie", true, false, _, true) => "TIEWIN-PLAYERPAIR-NATURAL"
    //      case ("Tie", false, true, _, true) => "TIEWIN-BANKERPAIR-NATURAL"
    //      case ("Tie", true, true, _, true) => "TIEWIN-PLAYERPAIR-BANKERPAIR-NATURAL"
    //
    //      case (_, _, _, _, _) => "EMPTY"
    //    }

    //    players.foreach {
    //      (player) => {
    //        player._2.actor ! GameWon(WinMessageData(round, winner, winScore, winCards, List("", ""), gameCards))
    //
    //      }
    //    }
    //
    //    admins.foreach {
    //      (admin) => {
    //        admin._2 ! ServerSentMessage.create("BACCARAT_GAME_WON", WinMessageData(round, winner, winScore, winCards, List("", ""), gameCards).json).json
    //      }
    //    }
    //
    //    lastWins = lastWins :+ WinMessageData(round, winner, winScore, winCards, List("", ""), gameCards)
    //
    //
    //    for {
    //      (player, bets) <- playersBets
    //      bet <- bets
    //      betName = bet.Player.toUpperCase() + bet.Hand.toUpperCase()
    //      if (!winner.contains(betName))
    //      betLostData = BetWonData("baccarat", player, round, bet.Player, bet.Hand, bet.Stake, 0)
    //    } {
    //      sendBetWonToPlayer(player, betLostData);
    //    }
    //
    //    for {
    //      (player, bets) <- playersBets
    //      bet <- bets
    //      betName = bet.Player.toUpperCase() + bet.Hand.toUpperCase()
    //      if (winner.contains(betName))
    //      betWonData = BetWonData("baccarat", player, round, bet.Player, bet.Hand, bet.Stake, (bet.Stake * bet.Odds))
    //    } {
    //      sendBetWonToPlayer(player, betWonData);
    //      handleBalanceUpdate(player, betWonData.Amount)
    //
    //      if (playersBetsWon.contains(player)) {
    //        playersBetsWon(player) = playersBetsWon(player) :+ betWonData
    //      } else {
    //        playersBetsWon = playersBetsWon ++ Map(player -> Array(betWonData))
    //      }
    //    }
    //


    tableState.copy(gameStatus = "Game Completed",
      WinningHand = getPlayerName(gameCards, winnerIndex),
      CardHandValue = getPlayerHand(gameCards, winnerIndex).score,
      playerHandValue = getPlayerHand(gameCards, 1).score,
      bankerHandValue = getPlayerHand(gameCards, 2).score,
      isPlayerPair = getPlayerHand(gameCards, 1).hasPair,
      isBankerPair = getPlayerHand(gameCards, 2).hasPair,
      isNaturalHand = getPlayerHand(gameCards, winnerIndex).isNatural,
      isSuitedTie = getPlayerName(gameCards, winnerIndex) == "Tie",
      BankerCards = gameCards(2).drop(5) map (x => Card.parseGameCard(x)),
      PlayerCards = gameCards(1).drop(5).map(x => Card.parseGameCard(x)),
      winCards = winCards,
      History = (tableState.History :+ WinResult(
        roundId = tableState.roundId,
        isPlayerPair = getPlayerHand(gameCards, 1).hasPair,
        isBankerPair = getPlayerHand(gameCards, 2).hasPair,
        isNaturalHand = getPlayerHand(gameCards, winnerIndex).isNatural,
        isSuitedTie = getPlayerName(gameCards, winnerIndex) == "Tie",
        BankerCards = gameCards(2).drop(5) map (x => Card.parseGameCard(x)),
        PlayerCards = gameCards(1).drop(5).map(x => Card.parseGameCard(x)),
        playerHandValue = getPlayerHand(gameCards, 1).score,
        bankerHandValue = getPlayerHand(gameCards, 2).score,
        CardHandValue = getPlayerHand(gameCards, winnerIndex).score,
        WinningHand = getPlayerName(gameCards, winnerIndex)
      )).takeRight(100)
    )

  }


}

trait BaccaratUtilities {

  def getPlayerName(gameCards: Array[List[String]], index: Int): String = {
    val players = for {
      p1 <- gameCards
    } yield p1.head

    players(index)
  }

  def getPlayerHand(gameCards: Array[List[String]], index: Int): Hand = {
    val hands = for {
      p1 <- gameCards
    } yield p1.tail

    val hand = Hand(for {
      str <- hands(index)
      card <- Card(str)
    } yield card)

    hand
  }

  def getPlayerHandFirst2Cards(gameCards: Array[List[String]], index: Int): Hand = {
    val hands: Array[List[String]] = for {
      p1 <- gameCards
    } yield p1.drop(5)

    val hand = Hand(for {
      str <- hands(index).take(2)
      card <- Card(str)
    } yield card)

    hand
  }

  def getWonBetsList(betsList: BetsList, gameResult: GameResult): BetsList = {
    import betsList.{TieBet => tieBetAmount, BankerBet => bankerBetAmount, PlayerBet => playerBetAmount}
    import betsList.SideBets.{PlayerPair => playerPairAmount, BankerPair => bankerPairAmount, EitherPair => eitherPairAmount, PerfectPair => perfectPairAmount}

    BetsList(
      TieBet = if (gameResult.WinningHand == "Tie") tieBetAmount * 9 else 0,
      BankerBet = if (gameResult.WinningHand == "Tie") bankerBetAmount else if (gameResult.WinningHand == "Banker") bankerBetAmount * 2 else 0,
      PlayerBet = if (gameResult.WinningHand == "Tie") playerBetAmount else if (gameResult.WinningHand == "Player") (playerBetAmount * 1.95).toInt else 0,
      SideBets = SideBet(
        PlayerPair = if (gameResult.isPlayerPair) playerPairAmount * 12 else 0,
        BankerPair = if (gameResult.isBankerPair) bankerPairAmount * 12 else 0,
        PerfectPair = 0,
        EitherPair = if (gameResult.isBankerPair || gameResult.isPlayerPair) eitherPairAmount * 6 else 0
      )
    )
  }

  def getTotalBetsValue(betsList: BetsList): Int = {
    import betsList.{TieBet => tieBetAmount, BankerBet => bankerBetAmount, PlayerBet => playerBetAmount}
    import betsList.SideBets.{PlayerPair => playerPairAmount, BankerPair => bankerPairAmount, EitherPair => eitherPairAmount, PerfectPair => perfectPairAmount}

    tieBetAmount + bankerBetAmount + playerBetAmount + playerPairAmount + bankerPairAmount + eitherPairAmount + perfectPairAmount
  }

  def getTotalWonBetsValue(wonBetsList: BetsList): Int = {
    import wonBetsList.{TieBet => tieBetAmount, BankerBet => bankerBetAmount, PlayerBet => playerBetAmount}
    import wonBetsList.SideBets.{PlayerPair => playerPairAmount, BankerPair => bankerPairAmount, EitherPair => eitherPairAmount, PerfectPair => perfectPairAmount}

    tieBetAmount + bankerBetAmount + playerBetAmount + playerPairAmount + bankerPairAmount + eitherPairAmount + perfectPairAmount
  }


}

