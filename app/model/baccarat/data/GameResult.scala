package model.baccarat.data

case class GameResult(roundId: Long = 0,
                      isPlayerPair: Boolean = false,
                      isBankerPair: Boolean = false,
                      isNaturalHand: Boolean = false,
                      isSuitedTie: Boolean = false,
                      BankerCards: Seq[GameCard] = Seq.empty[GameCard],
                      PlayerCards: Seq[GameCard] = Seq.empty[GameCard],
                      playerHandValue: Int = 0,
                      bankerHandValue: Int = 0,
                      CardHandValue: Int = 0,
                      WinningHand: String = "")