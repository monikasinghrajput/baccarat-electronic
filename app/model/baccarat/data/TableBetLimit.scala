package model.baccarat.data

case class TableBetLimit(Min_Bet: Int = 10,
                         Max_Bet: Int = 20000,
                         Min_SideBet: Int = 10,
                         Max_SideBet: Int = 2000,
                         Min_Tie:Int = 10,
                         Max_Tie: Int = 5000)
