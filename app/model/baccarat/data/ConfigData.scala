package model.baccarat.data

case class ConfigData(tableLimit: TableBetLimit = TableBetLimit(),
                      tableName: String = "EMPTY",
                      tableDifferential: Int = 25000,
                      showInfoPaper: Boolean = false,
                      autoDraw: Boolean = false,
                      autoPlay: Boolean = false,
                      isSuitedTieBetEnabled: Boolean = false,
                      isBetIntentStatisticsEnabled: Boolean = false,
                      isOppositeBettingAllowed: Boolean = true)
