package model.baccarat.data

import play.api.Logger

case class Seat(id: Int,
                name: String = "",
                ip: String = "",
                balance: Double = 0,
                uid: String = "-1",
                connected: Boolean = false,
                gameStatus: String = "CASH OUT",
) {
  val log = Logger(this.getClass)

}
