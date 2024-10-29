package services


import actors.LogManagerActor.AddLog
import actors._
import akka.actor.{ActorRef, ActorSystem}
import dao.LogDao
import model.common.messages.TableLimitCodec
import play.api.Logger

class GameService(actorSystem: ActorSystem,
                  logDao: LogDao) extends TableLimitCodec {

  val log: Logger = Logger(this.getClass)

  var actorMain: ActorRef = _
  val actorLogging: ActorRef = actorSystem.actorOf(LogManagerActor.props(logDao))

  def init(): Unit = {
    actorLogging ! AddLog(content =  s"Actor System is : ${actorSystem.name}");
    actorLogging ! AddLog(content =  s"Actor System Uptime : ${actorSystem.uptime}");
    actorMain = actorSystem.actorOf(MainActor.props(gameService = this), MainActor.name)

  }

  def getMainActor: ActorRef = actorMain
  def getLoggingActor: ActorRef = actorLogging

}
