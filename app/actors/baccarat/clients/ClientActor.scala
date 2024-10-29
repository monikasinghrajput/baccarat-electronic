package actors.baccarat.clients

//Standard Packages
import actors.LogManagerActor.AddLog
import akka.actor.{Actor, ActorRef, Props}
import model.baccarat.data.BetsList
import model.baccarat.message.BaccaratJsonCodecs
import play.api.Logger
import play.api.libs.json.{JsString, JsValue}

object ClientActor {

  def props(out: ActorRef, dealer: ActorRef, logManagerActor: ActorRef, remoteAddress: String): Props =
    Props(new ClientActor(out, dealer, logManagerActor, remoteAddress))
}

class ClientActor(out: ActorRef,
                  dealer: ActorRef,
                  logManagerActor: ActorRef,
                  remoteAddress: String)
  extends Actor with BaccaratJsonCodecs  {

  import actors.baccarat.BaccaratTableActor._

  val log = Logger(this.getClass)



  private var clientIp = remoteAddress

  override def preStart(): Unit = {
    logManagerActor ! AddLog(logType = "warning", content = s"Client Socket Flow Actor for ${clientIp} Started")
    super.preStart()
  }

  override def postStop(): Unit = {
    logManagerActor ! AddLog(logType = "warning", content = s"Client Socket Flow Actor for ${clientIp} Stopped")
    if (clientIp != "") dealer ! PlayerDisConnected(clientIp);
    super.postStop()
  }

  override def receive: Receive = {
    case clientMsg: JsValue => {
      if (clientMsg == JsString("PingMessage")) {
      } else {
        clientMsg("MessageType") match {
          case JsString("INITIALIZE_PLAYER") => {
            dealer ! PlayerConnected(clientIp, self, out)
          }

          case JsString("PlaceBet") => {
            val betList = clientMsg("BetsList").validate[BetsList].fold(
              invalid = {fieldsErrors =>
                /*log each fieldErrors during decode for debug*/
                fieldsErrors.foreach{ fieldsError =>
                  log.warn(s"field=${fieldsError._1} error=${fieldsError._2.toString()}")
                }
                /*Decoding Failed, so..  */
                log.warn(s"PlaceBet Decoding Failed...${clientMsg("BetsList").toString()}")
              },
              valid = {betList =>
                log.warn(s"PlaceBet Decoded...${betList.toString}")
                dealer ! PlayerBetPlaced(clientIp, betList, out)
              }
            )

          }

          case JsString("PlaceBetIntent") =>
            val betList = clientMsg("BetsList").validate[BetsList].fold(
              invalid = { fieldsErrors =>
                /*log each fieldErrors during decode for debug*/
                fieldsErrors.foreach { fieldsError =>
                  log.warn(s"field=${fieldsError._1} error=${fieldsError._2.toString()}")
                }
                /*Decoding Failed, so..  */
                log.warn(s"PlaceBetIntent Decoding Failed...${clientMsg("BetsList").toString()}")
              },
              valid = { betList =>
                log.warn(s"PlaceBetIntent Decoded...${betList.toString}")
                dealer ! PlayerBetIntentPlaced(clientIp, betList, out)
              }
            )

          case _ => log.logger.info(s"Unknown MessageType ${clientMsg("MessageType")} Received!")
        }
      }

    }
    case Reconnected =>
      log.logger.info("Client Reconnected Scenario ???!!")
      logManagerActor ! AddLog(logType = "warning", content = s"Client Reconnected Scenario ???!!")
      clientIp = ""
    case _ => log.logger.info("Unknown Message Received!")

  }

}



