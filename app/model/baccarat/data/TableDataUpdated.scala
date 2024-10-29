package model.baccarat.data


case class TableDataUpdatedMsg(MessageType: String = "tableDataUpdated", data: TableData, timestamp: String)
