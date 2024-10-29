package dao.postgres

import model.admin.{GameActivityLog, GameData}
import play.api.Logger
import scalikejdbc._

import scala.util.Try


class GameDao {

  val log: Logger = Logger(this.getClass)

  def findGameByName(name: String): Try[Option[GameData]] = Try {
    NamedDB(Symbol("auth")).readOnly { implicit session => sql"select * from games where name = $name".map(GameData.fromRS).headOption().apply()
    }
  }

  def getAllGames: Try[Seq[GameData]] = Try {
    NamedDB(Symbol("auth")).readOnly { implicit session => sql"select * from games".map(GameData.fromRS).list().apply()
    }
  }

  def updateGameData(game: String, gameData: GameData): Try[Option[String]] = Try {
    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update games set minBet = ${gameData.minBet} where name = $game""".update().apply()
    }

    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update games set maxBet = ${gameData.maxBet} where name = $game""".update().apply()
    }


    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update games set operationalState = ${gameData.operationalState} where name = $game""".update().apply()
    }

    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update games set workingState = ${gameData.workingState} where name = $game""".update().apply()
    }

    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update games set updated = ${gameData.updated} where name = $game""".update().apply()
    }


    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update games set dealer = ${gameData.dealer} where name = $game""".update().apply()
    }

    Some(game)
  }


  def insertGamesActivityLog(gameActivityLog: GameActivityLog): Try[Option[GameActivityLog]] = Try {
    NamedDB(Symbol("auth")).localTx { implicit session =>
      sql"""insert into gamesActivityHistory(id,
               action,
               name,
               minBet,
               maxBet,
               operationalState,
               workingState,
               dateTime,
               dealer)
       values (nextval('games_activity_sequence'),
               ${gameActivityLog.action},
               ${gameActivityLog.gameData.name},
               ${gameActivityLog.gameData.minBet},
               ${gameActivityLog.gameData.maxBet},
               ${gameActivityLog.gameData.operationalState},
               ${gameActivityLog.gameData.workingState},
               ${gameActivityLog.gameData.updated},
               ${gameActivityLog.gameData.dealer})""".update().apply()
    }
    Some(gameActivityLog)
  }

  def getAllGamesActivityLog: Try[Seq[GameActivityLog]] = Try {
    NamedDB(Symbol("auth")).readOnly { implicit session => sql"select * from gamesActivityHistory".map(GameActivityLog.fromRS).list().apply()
    }
  }
}
