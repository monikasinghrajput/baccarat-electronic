package model.common.messages


case class TableLimit(chips: Seq[Chip] = Seq(Chip(color = "cyan", value = 10, img = "http://localhost:5000/assets/CustomAssets/chips/default_cyan.png", default = false)),
                      Min_Bet: Int = 10,
                      Max_Bet: Int = 10000000,
                      Min_SideBet: Int = 0,
                      Max_SideBet: Int = 0,
                      Min_StraightUpBet: Int = 10,
                      Max_StraightUpBet: Int = 25000,
                      Min_SplitBet: Int = 10,
                      Max_SplitBet: Int = 50000,
                      Min_StreetBet: Int = 10,
                      Max_StreetBet: Int = 75000,
                      Min_CornerBet: Int = 10,
                      Max_CornerBet: Int = 100000,
                      Min_LineBet: Int = 10,
                      Max_LineBet: Int = 150000,
                      Min_Dozen_ColumnBet: Int = 10,
                      Max_Dozen_ColumnBet: Int = 300000,
                      Min_OutsideBet: Int = 10,
                      Max_OutsideBet: Int = 500000)
