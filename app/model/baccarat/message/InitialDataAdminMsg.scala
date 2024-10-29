package model.baccarat.message

import model.baccarat.data.Data
import model.common.messages.{MoneyTransactionMsg, OperationTransactionMsg, Player, ServerLog}

case class InitialDataAdminMsg(MessageType: String = "InitialData",
                               tableId: String = "",
                               destination: String = "admin",
                               clientId: String = "",
                               roundId: Long = 0,
                               timestamp: String = "",
                               data: Data = Data(),
                               logs: Seq[ServerLog] = Seq.empty[ServerLog],
                               players: Seq[Player] = Seq.empty[Player],
                               transactions: Seq[MoneyTransactionMsg] = Seq.empty[MoneyTransactionMsg],
                               operations: Seq[OperationTransactionMsg] = Seq.empty[OperationTransactionMsg]
                              )
