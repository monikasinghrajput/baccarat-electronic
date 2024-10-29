package model.baccarat.data

import model.common.messages.Chip

case class TableLimit(chips: Seq[Chip] = Seq.empty[Chip],
                      Min_Bet: Int = 10,
                      Max_Bet: Int = 20000,
                      Min_SideBet: Int = 10,
                      Max_SideBet: Int = 2000,
                      Min_StraightUpBet: Int = 0,
                      Max_StraightUpBet: Int = 0,
                      Min_SplitBet: Int = 0,
                      Max_SplitBet: Int = 0,
                      Min_StreetBet: Int = 0,
                      Max_StreetBet: Int = 0,
                      Min_CornerBet: Int = 0,
                      Max_CornerBet: Int = 0,
                      Min_LineBet: Int = 0,
                      Max_LineBet: Int = 0,
                      Min_Dozen_ColumnBet: Int = 0,
                      Max_Dozen_ColumnBet: Int = 0,
                      Min_OutsideBet: Int = 0,
                      Max_OutsideBet: Int = 0,
                      Min_Tie: Int = 10,
                      Max_Tie: Int = 5000)