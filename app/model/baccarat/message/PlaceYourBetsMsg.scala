package model.baccarat.message


case class PlaceYourBetsMsg(MessageType: String = "PLACE_YOUR_BETS", //Important
                            destination: String = "player", //
                            clientId: String = "", //
                            roundId: Long = 0, //Important
                            TimerTimeLeft: Int = 30, //Important
                            TimerTime: Int = 30, //Important
                            timestamp: String = ""
)
