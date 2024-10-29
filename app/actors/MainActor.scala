package actors

import akka.actor.{Actor, ActorRef, Props}
import model.common.messages._
import play.api.Logger
import services.GameService

import java.text.SimpleDateFormat
import java.util.Calendar
import scala.language.postfixOps



object MainActor {
  val name = "main-actor"
  val path = s"/usr/$name"

  def props(gameService: GameService): Props = Props(new MainActor(gameService))

  case class GetBalance(userCode: String, info: String = "")

  case object GetNextRound

}

final class MainActor(gameService: GameService
                     ) extends Actor {

  val log: Logger = Logger(this.getClass)
  val logManagerActor: ActorRef = gameService.getLoggingActor
  val dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss.SSS z")

  private val roundReadDao = new RoundReadDao()

  import MainActor._

  override def receive: Receive = {
    case GetNextRound => sender() ! roundReadDao.getNextRound
    case msg => log.logger.info(s"Unknown Message $msg")
  }

  override def preStart(): Unit = {
    super.preStart()
  }

  override def postStop(): Unit = {
    super.postStop()
  }

}

class RoundReadDao {
  var date: Int = new SimpleDateFormat("ydM").format(Calendar.getInstance.getTime).drop(2).toInt
  var round = 0

  def getNextRound: Int = {
    val today = new SimpleDateFormat("ydM").format(Calendar.getInstance.getTime).drop(2).toInt
    if (date != today) {
      date = today
      round = 0
    }
    round += 1
    date + round
  }
}


