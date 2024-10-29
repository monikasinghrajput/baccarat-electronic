package model.baccarat.message

import model.baccarat.data.ConfigData

case class InitialConfigMsg(MessageType: String = "initialConfig", //Important
                            configData: ConfigData,
                            timestamp: String = "")
