package model.baccarat.message

import model.baccarat.data.Data

case class InitialDataPlayerMsg(MessageType: String = "InitialData",
                                tableId: String = "32100",
                                destination: String = "player",
                                clientId: String = "",
                                roundId: Long = 0,
                                timestamp: String = "",
                                RoundTripStartTime: String = "",
                                isOppositeBettingAllowed: Boolean = true,
                                isSuitedTieBetEnabled: Boolean = false,
                                isBetIntentStatisticsEnabled: Boolean = true,
                                data: Data = Data()
                               )
