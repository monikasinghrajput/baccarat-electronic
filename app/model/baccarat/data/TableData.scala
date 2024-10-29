package model.baccarat.data


case class TableData(roundId: Long = 1,
                     gameStatus: String = "Waiting For Dealer",
                     dealer: String = "Sam",
                     winner: String = "",
                     winScore: String = "",
                     winCards:List[String] = List("", "", ""),
                     gameCards: Array[List[String]] = Array(List("Tie", ""),List("Player","","","",""), List("Banker","","","","")),
                     minBet: Int = 50,
                     maxBet:Int = 100000,
                     waitingCounter: Int = 0,
                     bettingCounter: Int = 0,
                     drawCardCounter: Int = 0,
                     turn: (Int,Int) = (1, 1),
                     seats: Seq[Seat] = Seq.empty[Seat]
                     )
