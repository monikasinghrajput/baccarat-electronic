package model.baccarat.message

case class CardMsg(MessageType: String = "Card", //"MessageType": "GAME_RESULT"
                   destination: String = "player", //
                   clientId: String = "", //"ClientId": "20000181|91205|32100",
                   roundId: Long = 0, //"roundId": 13,
                   CardHand: String = "", //"player", "banker"
                   CardHandValue: Int = 0,
                   CardName: String = "",
                   CardValue: Int = 0,
                   squeezed: Boolean = false,//Additional Field Added to support Squeeze Feature
                   timestamp: String = ""
)
