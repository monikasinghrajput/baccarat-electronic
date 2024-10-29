package model.baccarat.message

case class ShuffleMsg(MessageType: String = "SHUFFLE", //Important
                      destination: String = "player", //
                      clientId: String = "",
                      roundId: Long = 0,
                      timestamp: String = ""
)
