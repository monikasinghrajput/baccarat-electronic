package model.baccarat.message

import model.baccarat.data.{GameResult}

case class GameResultMsg(MessageType: String = "GAME_RESULT",//Important
                         destination: String = "player", //
                         clientId: String = "",
                         roundId: Long = 0,//Important
                         timestamp: String = "",//Important
                         WinAmount: Int = 0, //Important
                         GameResults: GameResult = GameResult() //Important
)
