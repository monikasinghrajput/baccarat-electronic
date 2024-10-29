package model.baccarat.data

import akka.actor.ActorRef
import model.common.messages.{Bet, WinBet}

case class ClientData(actor:ActorRef = null,
                      client:ActorRef = null,
                      uid : String = "-1",
                      playerIp: String = "192.168.0.1",
                      betsList: BetsList = null,
                      winningBets: Seq[WinBet] = Seq.empty[WinBet],
                      balance: Double = 0.0)
