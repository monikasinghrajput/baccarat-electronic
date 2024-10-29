package model.baccarat.message

import model.baccarat.data.Data

case class InitialDataTopperMsg(MessageType: String = "InitialData",
                                tableId: String = "",
                                destination: String = "player",
                                clientId: String = "",
                                roundId: Long = 0,
                                timestamp: String = "",
                                RoundTripStartTime: String = "",
                                isOppositeBettingAllowed: Boolean = false,
                                isSuitedTieBetEnabled: Boolean = false,
                                isBetIntentStatisticsEnabled: Boolean = true,
                                data: Data = Data()
                               )
