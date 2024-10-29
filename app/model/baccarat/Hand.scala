package model.baccarat

import model.baccarat.syntax._
import baccarat.Ranking

case class Hand(cards: Vector[Card]) {
  def +(card: Card): Hand = copy(cards = cards :+ card)
  def -(card: Card): Hand = copy(cards = cards.patch(cards.indexOf(card),Nil,1))
  // def -(card: Card): Hand = copy(cards = cards.filterNot(_ == card))
  def toList: List[Card] = cards.toList

  def chooseCard: Vector[Selection[Card]] = cards.map(card => Selection(card, this - card))

  def choosePair: Vector[Selection[(Card, Card)]] = {
    for {
      Selection(a, rest) <- this.chooseCard
      Selection(b, rest) <- rest.chooseCard if a.rank.toString == b.rank.toString
    } yield Selection((a, b), rest)
  }

  def score: Int = this.cards.map(x => x.rank.value).fold(0)((a , b) => a + b) % 10;

  def isNatural: Boolean = {
    if((this.cards.length == 2) && (this.score > 7)) true
    else false
  } 

  def hasPair: Boolean = {
    if (choosePair.nonEmpty) true
    else false
  }

}

object Hand {
  val empty: Hand = Hand()

  def apply(cards: Card*): Hand = Hand(cards.toVector)
  def apply(cards: List[Card]): Hand = Hand(cards.toVector)

}
