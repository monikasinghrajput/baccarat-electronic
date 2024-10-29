package services

import actors._
import akka.actor.{ActorRef, ActorSystem}
import dao.{ConfigDao}
import model.common.messages.{TableLimit}
import play.api.Logger

class ConfigService(configDao: ConfigDao) {

  val log: Logger = Logger(this.getClass)

  def init(): Unit = {
    log.info("Configuration Service Initialized...")
  }

}
