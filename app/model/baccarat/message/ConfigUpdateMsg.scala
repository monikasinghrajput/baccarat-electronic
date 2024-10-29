package model.baccarat.message

import model.baccarat.data.ConfigData

case class ConfigUpdateMsg(MessageType: String = "configUpdate", //Important
                           configData: ConfigData,
                           timestamp: String = "")
