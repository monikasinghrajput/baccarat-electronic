package dao

import model.common.messages._
import os.Path
import play.api.Logger
import play.api.libs.json.Json

import java.text.SimpleDateFormat
import java.util.Calendar

class BaccaratPlayerDao extends PlayerCodec with MoneyTransactionCodecMsg with OperationTransactionCodecMsg {

  val log = Logger(this.getClass)
  val dateFormat =  new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss.SSS z")
  
  val fileNamePlayers: Path = os.home / "baccarat" /  "users.json"
  val fileNameOperations: Path = os.home / "baccarat" /  "operations.json"
  val fileNameTransactions: Path = os.home / "baccarat" /  "transactions.json"

  if (!os.exists(fileNamePlayers)) {
    os.write.over(
      fileNamePlayers,
      Json.prettyPrint(Json.toJson(Seq(
        Player(uid = "1", nickname = "Player 1", clientIp = "192.168.1.1", balance = 0),
        Player(uid = "2", nickname = "Player 2", clientIp = "192.168.1.2", balance = 0),
        Player(uid = "3", nickname = "Player 3", clientIp = "192.168.1.3", balance = 0),
        Player(uid = "4", nickname = "Player 4", clientIp = "192.168.1.4", balance = 0),
        Player(uid = "5", nickname = "Player 5", clientIp = "192.168.1.5", balance = 0),
        Player(uid = "6", nickname = "Player 6", clientIp = "192.168.1.6", balance = 0),
        Player(uid = "7", nickname = "Player 7", clientIp = "192.168.1.7", balance = 0),
        Player(uid = "8", nickname = "Player 8", clientIp = "192.168.1.8", balance = 0),
      ))),
      createFolders = true
    )
    log.logger.info(s"Baccarat Backup Players Missing... Created Success..")
  }

  if (!os.exists(fileNameTransactions)) {
    os.write.over(
      fileNameTransactions,
      Json.prettyPrint(Json.toJson(Seq.empty[MoneyTransactionMsg]
      )),
      createFolders = true
    )
    log.logger.info(s"Baccarat Backup Transactions Missing... Created Success..")
  }

  if (!os.exists(fileNameOperations)) {
    os.write.over(
      fileNameOperations,
      Json.prettyPrint(Json.toJson(Seq(
        OperationTransactionMsg(MessageType = "PLAYER_CREATED", uid = "1", nickname = "Player 1", client_ip = "192.168.1.1", status = "offline", usage = "unlocked", timestamp = dateFormat.format(Calendar.getInstance().getTime)),
        OperationTransactionMsg(MessageType = "PLAYER_CREATED", uid = "2", nickname = "Player 2", client_ip = "192.168.1.2", status = "offline", usage = "unlocked", timestamp = dateFormat.format(Calendar.getInstance().getTime)),
        OperationTransactionMsg(MessageType = "PLAYER_CREATED", uid = "3", nickname = "Player 3", client_ip = "192.168.1.3", status = "offline", usage = "unlocked", timestamp = dateFormat.format(Calendar.getInstance().getTime)),
        OperationTransactionMsg(MessageType = "PLAYER_CREATED", uid = "4", nickname = "Player 4", client_ip = "192.168.1.4", status = "offline", usage = "unlocked", timestamp = dateFormat.format(Calendar.getInstance().getTime)),
        OperationTransactionMsg(MessageType = "PLAYER_CREATED", uid = "5", nickname = "Player 5", client_ip = "192.168.1.5", status = "offline", usage = "unlocked", timestamp = dateFormat.format(Calendar.getInstance().getTime)),
        OperationTransactionMsg(MessageType = "PLAYER_CREATED", uid = "6", nickname = "Player 6", client_ip = "192.168.1.6", status = "offline", usage = "unlocked", timestamp = dateFormat.format(Calendar.getInstance().getTime)),
        OperationTransactionMsg(MessageType = "PLAYER_CREATED", uid = "7", nickname = "Player 7", client_ip = "192.168.1.7", status = "offline", usage = "unlocked", timestamp = dateFormat.format(Calendar.getInstance().getTime)),
        OperationTransactionMsg(MessageType = "PLAYER_CREATED", uid = "8", nickname = "Player 8", client_ip = "192.168.1.8", status = "offline", usage = "unlocked", timestamp = dateFormat.format(Calendar.getInstance().getTime)),
      ))),
      createFolders = true
    )
    log.logger.info(s"Baccarat Backup Operations Missing... Created Success..")
  }

  val playersString = os.read(fileNamePlayers);
  val playersJson = Json.parse(playersString);
  var players: Seq[Player] = playersJson.as[Seq[Player]]

  val transactionsString = os.read(fileNameTransactions);
  val transactionsJson = Json.parse(transactionsString);
  var transactions: Seq[MoneyTransactionMsg] = transactionsJson.as[Seq[MoneyTransactionMsg]]


  val operationsString = os.read(fileNameOperations);
  val operationsJson = Json.parse(operationsString);
  var operations: Seq[OperationTransactionMsg] = operationsJson.as[Seq[OperationTransactionMsg]]

  def getPlayersData: Seq[Player] = players
  def setPlayers(): Unit = os.write.over(
    fileNamePlayers,
    Json.prettyPrint(Json.toJson(players)),
    createFolders = true
  )
  def getPlayerData(player: String): Player = {
    players.find(p => p.clientIp == player).getOrElse(
      Player(
        uid = (players.size + 1).toString, //TODO now I am filling ip as uid
        clientId = player,
        clientIp = player,
        balance = 500000,
        nickname = "Guest"
      )
    )
  }
  def addPlayer(player: Player): Seq[Player] = {players = players.+:(player) ;
    os.write.over(
      fileNamePlayers,
      Json.prettyPrint(Json.toJson(players)),
      createFolders = true
    );
    players
  }
  def updatePlayer(player: Player, uid: String): Unit = {
    val foundIndex = players.indexWhere(p => p.uid == uid)
    if(foundIndex != -1) {
      players = players.updated(foundIndex, player)
    } else {
      log.logger.warn("Player not found in updatePlayer! Added a new Player")
      players = players.+:(player);
    }
    os.write.over(
      fileNamePlayers,
      Json.prettyPrint(Json.toJson(players)),
      createFolders = true
    )
  }

  def getTransactionsData(): Seq[MoneyTransactionMsg] = transactions
  def getOperationTransactionsData(): Seq[OperationTransactionMsg] = operations
  def addTransaction(transaction: MoneyTransactionMsg): Unit = {transactions = transactions.+:(transaction)}
  def addOperationTransaction(operation: OperationTransactionMsg): Unit = {operations = operations.+:(operation)}
  def setTransactions(): Unit = {
    os.write.over(
      fileNameTransactions,
      Json.prettyPrint(Json.toJson(transactions)),
      createFolders = true
    )}
  def setOperationTransactions(): Unit = {
    os.write.over(
      fileNameOperations,
      Json.prettyPrint(Json.toJson(operations)),
      createFolders = true
    )}

}
