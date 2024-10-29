package model.baccarat.message

case class NoMoreBetsMsg(MessageType: String = "NO_MORE_BETS", //Important
                         destination: String = "player", //
                         clientId: String = "",
                         roundId: Long = 0, //Important
                         timestamp: String = ""////Important
)
