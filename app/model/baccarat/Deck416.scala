package model.baccarat

import model.baccarat.Card.Deck

import scala.collection.mutable.ListBuffer

case object Deck416 {
  val freshCards: Deck = for {
    _ <- List(1,2,3,4,5,6,7,8)
    s <- Card.suits
    r <- Card.ranks
  } yield Card(s,r)

  var cards: ListBuffer[Card] = freshCards.to(ListBuffer)

  def shuffleCards(): Unit = {
    cards = scala.util.Random.shuffle(cards)
  }

  def reShuffle(): Unit = {
    cards = freshCards.to(ListBuffer)
  }

  def drawCard: Card = {
    shuffleCards()
    cards.remove(0)
  }

}
