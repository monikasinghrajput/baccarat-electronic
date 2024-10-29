package model.baccarat.message

import model.baccarat.data.Data

case class DataUpdatedMsg(MessageType: String = "tableDataUpdated", data: Data = Data(), timestamp: String = "")
