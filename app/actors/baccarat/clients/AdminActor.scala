package actors.baccarat.clients

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import play.api.libs.json.{JsString, JsValue}
import model.common.messages.MoneyTransaction
import actors.LogManagerActor.AddLog
import model.baccarat.Card
import model.baccarat.data.{ConfigData, SqueezedCard, WinResult}
import model.baccarat.message.BaccaratJsonCodecs
import play.api.libs.functional.syntax._
import play.api.libs.json._

object AdminActor {
  val name = "baccarat-stadium-seater-admin-actor"
  val path = s"/usr/$name"

  def props(out: ActorRef,
            dealer: ActorRef,
            logActor: ActorRef,
            remoteAddress: String
           ): Props = Props(new AdminActor(out = out, dealer = dealer, logActor = logActor, remoteAddress = remoteAddress))
}

class AdminActor(out: ActorRef, dealer: ActorRef, logActor: ActorRef, remoteAddress: String)
  extends Actor
    with BaccaratJsonCodecs
    with ActorLogging {

  import actors.baccarat.BaccaratTableActor._

  private var clientIp = remoteAddress

  override def preStart(): Unit = super.preStart()

  override def postStop(): Unit = {
    if (clientIp != "") dealer ! AdminDisConnected(clientIp);
    super.postStop()
  }


  override def receive: Receive = {
    case pingMsg: JsString =>
    case clientMsg: JsValue =>
      clientMsg("MessageType") match {
        case JsString("INITIALIZE_ADMIN") => {
          log.info(s"INITIALIZE_ADMIN received from ${clientIp}!")
          dealer ! AdminConnected(clientIp, self, out)
        }

        /*Baccarat Admin Special Commands*/
        case JsString("INFO_TURN_ON_COMMAND") =>
          dealer ! InfoPaperShow(true)
        case JsString("INFO_TURN_OFF_COMMAND") =>
          dealer ! InfoPaperShow(false)

        case JsString("RESET_GAME_COMMAND") =>
          dealer ! PlaceYourBets
        case JsString("START_GAME_COMMAND") =>
          dealer ! NoMoreBets
        case JsString("CLEAR_SHOE_COMMAND") =>
          dealer ! ShuffleDeck

        case JsString("REMOVE_PREV_RESULT_COMMAND") =>
          dealer ! CancelPrevGame

        case JsString("TOGGLE_AUTO_DRAW") =>
          dealer ! ToggleAutoDraw
        case JsString("TOGGLE_AUTO_PLAY") =>
          dealer ! ToggleAutoPlay

        case JsString("CARD_DRAWN") =>
          log.info(s"Card Drawn is ${clientMsg("card")}")
          val baccaratCard: Card = Card.parseBeeTekCard(clientMsg("card").validate[String].get).getOrElse(Card())
          dealer ! CardDrawn(baccaratCard)

        case JsString("GAME_RESULT") =>
          log.info(s"GAME_RESULT received ${clientMsg("winResult")}")
          val winResult = clientMsg("winResult").validate[WinResult].fold(
            invalid = { fieldErrors =>
              //                log.info(s"GAME_RESULT Decoding failed..")
              fieldErrors.foreach { x =>
                log.info(s"field: ${x._1}, errors: ${x._2}")
              }
              WinResult()
            },
            valid = { data =>
              //                log.info(s"GAME_RESULT Decoding Success..")
              data
            }
          )
          log.info(s"Win Result Decoded => ${winResult.toString}")
          if (winResult.roundId != -1) dealer ! WinResultMsg(winResult)

        case JsString("CARD_SQUEEZED") =>
          log.info(s"CARD_SQUEEZED received")
          val squeezedCard = clientMsg("Squeeze").validate[SqueezedCard].fold(
            invalid = { fieldErrors =>
              //                log.info(s"CARD_SQUEEZED Decoding failed..")
              fieldErrors.foreach { x =>
                log.info(s"field: ${x._1}, errors: ${x._2}")
              }
              SqueezedCard()
            },
            valid = { card =>
              //                log.info(s"CARD_SQUEEZED Decoding Success..")
              card
            }
          )
          log.info(s"Card Squeezed Decoded => ${squeezedCard.toString}")
          if (squeezedCard.hand != -1) dealer ! SqueezedCardMsg(squeezedCard)

        case JsString("CONFIG_UPDATE") =>
          log.info(s"CONFIG_UPDATE received with ${clientMsg("configData")}")
          val configData = clientMsg("configData").validate[ConfigData].fold(
            invalid = { fieldErrors =>
              log.info(s"CONFIG_UPDATE Decoding failed..")
              fieldErrors.foreach { x =>
                log.info(s"field: ${x._1}, errors: ${x._2}")
              }
              ConfigData()
            },
            valid = { data =>
              log.info(s"CONFIG_UPDATE Decoding Success..")
              data
            }
          )
          log.info(s"Config Data Decoded => ${configData.toString}")
          if (configData.tableName != "EMPTY") dealer ! ConfigUpdateCommand(configData)


        case JsString("DEPOSIT_REQ") => {
          logActor ! AddLog(content = s"DEPOSIT_REQ ${clientMsg("amount")}  for ${clientMsg("clientIp")} received from ${clientIp}!")
          log.info(s"DEPOSIT_REQ ${clientMsg("amount")}  for ${clientMsg("clientIp")} received from ${clientIp}!")

          dealer ! PlayerMoneyTransaction(
            MoneyTransaction(
              transType = "DEPOSIT",
              admin = clientIp,
              playerIp = clientMsg("clientIp").validate[String].get,
              uid = clientMsg("uid").validate[String].get,
              amount = clientMsg("amount").validate[Double].get,
            ),
            self,
            out
          )
        }
        case JsString("WITHDRAW_REQ") => {
          logActor ! AddLog(content = s"WITHDRAW_REQ ${clientMsg("amount")}  for ${clientMsg("clientIp")} received from ${clientIp}!")
          log.info(s"WITHDRAW_REQ ${clientMsg("amount")}  for ${clientMsg("clientIp")}  received from ${clientIp}!")

          dealer ! PlayerMoneyTransaction(
            MoneyTransaction(
              transType = "WITHDRAW",
              admin = clientIp,
              playerIp = clientMsg("clientIp").validate[String].get,
              uid = clientMsg("uid").validate[String].get,
              amount = clientMsg("amount").validate[Double].get
            ),
            self,
            out
          )
        }

        case JsString("CASH_IN_REQ") => {
          logActor ! AddLog(content = s"CASH_IN_REQ ${clientMsg("amount")}  for ${clientMsg("uid")} received from ${clientIp}!")
          log.info(s"CASH_IN_REQ ${clientMsg("amount")}  for ${clientMsg("uid")} received from ${clientIp}!")

          dealer ! PlayerMoneyTransaction(
            MoneyTransaction(
              transType = "DEPOSIT",
              admin = clientIp,
              playerIp = clientIp,
              uid = clientMsg("uid").validate[String].get,
              amount = clientMsg("amount").validate[Double].get,
            ),
            self,
            out
          )
        }
        case JsString("CASH_OUT_REQ") => {
          logActor ! AddLog(content = s"CASH_OUT_REQ ${clientMsg("amount")}  for ${clientMsg("uid")} received from ${clientIp}!")
          log.info(s"CASH_OUT_REQ ${clientMsg("amount")}  for ${clientMsg("uid")}  received from ${clientIp}!")

          dealer ! PlayerMoneyTransaction(
            MoneyTransaction(
              transType = "WITHDRAW",
              admin = clientIp,
              playerIp = clientIp,
              uid = clientMsg("uid").validate[String].get,
              amount = clientMsg("amount").validate[Double].get
            ),
            self,
            out
          )
        }

        case _ => log.info(s"Unknown MessageType ${clientMsg("MessageType")} Received!")
      }
    case Reconnected =>
      log.info("Admin Reconnected Scenario ???!!")
      logActor ! AddLog(logType = "warning", content = s"Admin Reconnected Scenario ???!!")
      clientIp = ""

    case _ => log.info("Unknown Message Received!")
  }
}
