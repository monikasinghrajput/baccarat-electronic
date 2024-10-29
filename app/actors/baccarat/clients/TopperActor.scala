package actors.baccarat.clients

import actors.LogManagerActor.AddLog
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import play.api.libs.json.{JsString, JsValue}

object TopperActor {
  val name = "stadium-baccarat-topper"
  val path = s"/usr/$name"

  def props(out: ActorRef,
            dealer: ActorRef,
            logActor: ActorRef,
            remoteAddress: String): Props
  = Props(new TopperActor(out, dealer, logActor, remoteAddress))

}

class TopperActor(out: ActorRef,
                  dealer: ActorRef,
                  logActor: ActorRef,
                  remoteAddress: String)
  extends Actor
  with ActorLogging  {

  import actors.baccarat.BaccaratTableActor._

  private var clientIp = remoteAddress


  override def preStart(): Unit = {
    logActor ! AddLog(logType = "warning", content = s"Topper Socket Flow Actor for ${clientIp} Started")
    super.preStart()
  }


  override def postStop(): Unit = {
    logActor ! AddLog(logType = "warning", content = s"Topper Socket Flow Actor for ${clientIp} Stopped")

    if (clientIp != "") dealer ! TopperDisconnected(clientIp);
    super.postStop()
  }

  override def receive: Receive = {
    case clientMsg: JsString =>
    case clientMsg: JsValue =>
      clientMsg("MessageType") match {

        case JsString("INITIALIZE_TOPPER") => {
          log.info(s"INITIALIZE_TOPPER received from ${clientIp}!")
          dealer ! TopperConnected(clientIp, self, out)
        }

        case _ => log.warning(s"Unknown MessageType ${clientMsg("MessageType")} Received!")
      }
    case Reconnected =>
      log.info("Topper Reconnected Scenario ???!!")
      logActor ! AddLog(logType = "warning", content = s"Topper Reconnected Scenario ???!!")
      clientIp = ""

    case _ => log.warning("Unknown Message Received!")
  }

}
