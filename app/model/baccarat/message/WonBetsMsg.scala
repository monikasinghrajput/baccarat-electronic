package model.baccarat.message

import model.baccarat.data.BetsList

case class WonBetsMsg(MessageType: String = "WonBets", //Important
                      destination: String = "player", //
                      clientId: String = "", //
                      RoundTripStartTime: Long = 0,
                      WinningBets: BetsList = BetsList()
                      )
