package actors


import akka.actor.{Actor, Props}
import dao.LogDao
import model.common.messages.ServerLog
import play.api.Logger

import java.text.SimpleDateFormat
import java.util.Calendar

object LogManagerActor {
  val name = "log-manager-actor"
  val path = s"/usr/${name}"
  case object GetAllLogs
  case class AddLog(logType: String = "info", runtimeClass: String = "General", content: String)

  def props(logDao: LogDao): Props = Props(new LogManagerActor(logDao))
}

class LogManagerActor(logDao: LogDao) extends Actor {

  import actors.LogManagerActor._

  val log: Logger = Logger(this.getClass);
  private var logs: Seq[ServerLog] = logDao.getLogs
  val dateFormat =  new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss.SSS z")

  override def receive: Receive = {
    case GetAllLogs => sender() ! logs.take(200)
    case AddLog(logType, runtimeClass, content) =>
      logs = ServerLog(logType, runtimeClass, content, dateFormat.format(Calendar.getInstance().getTime)) +: logs
    case _ => log.logger.info("Unknown message!")
  }

  override def preStart(): Unit = {
    super.preStart()
    log.logger.info(s"Log Manager preStart Called")
  }

  override def postStop(): Unit = {
    logDao.setLogs(logs)
    super.postStop()
  }
}

