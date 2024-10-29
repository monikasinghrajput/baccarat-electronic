package model.baccarat.data

case class Data(History: Seq[WinResult] = Seq.empty[WinResult],
                ColdNumbers: Seq[Int] = Seq.empty[Int],
                HotNumbers: Seq[Int] = Seq.empty[Int],
                BankerCards: Seq[GameCard] = Seq.empty[GameCard],
                PlayerCards: Seq[GameCard] = Seq.empty[GameCard],
                bankerHandValue: Int = 0,
                playerHandValue: Int = 0,
                playerBetOfThisRound: BetsList = BetsList())
