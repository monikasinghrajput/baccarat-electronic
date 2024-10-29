package model.baccarat.data

case class BetsList(TieBet: Int = 0,
                    BankerBet: Int = 0,
                    PlayerBet: Int = 0,
                    SideBets: SideBet = SideBet(PlayerPair = 0, BankerPair = 0, PerfectPair = 0, EitherPair = 0))
