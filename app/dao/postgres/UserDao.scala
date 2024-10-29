package dao.postgres

import model.admin.{AccountsActivityLog, MoneyTransactionsLog, UserData}
import model.dao.User
import org.mindrot.jbcrypt.BCrypt
import play.api.Logger
import scalikejdbc._

import java.util.UUID
import scala.util.Try


class UserDao {

  val log = Logger(this.getClass)

  def findById(userId: UUID): Try[Option[User]] = Try {
    NamedDB(Symbol("auth")).readOnly { implicit session => sql"select * from users where user_id = $userId".map(User.fromRS).headOption().apply()
    }
  }

  def findByUserCode(userCode: String): Try[Option[User]] = Try {
    NamedDB(Symbol("auth")).readOnly { implicit session => sql"select * from users where user_code = $userCode".map(User.fromRS).headOption().apply()
    }
  }

  def getUsers: Try[Seq[User]] = Try {
    NamedDB(Symbol("auth")).readOnly { implicit session => sql"select * from users".map(User.fromRS).list().apply()
    }
  }


  def insertUser(user: User): Try[User] = Try {
    //    val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
    //    val user = User(UUID.randomUUID(), userCode, fullName, country, account, usage, phone, created, status, passwordHash, balance, exposure, isAdmin = false)
    NamedDB(Symbol("auth")).localTx { implicit session =>
      sql"""insert into users(user_id, user_code, full_name, country, account, usage, phone, created, status, password, balance, exposure, is_admin)
       values (${user.userId}, ${user.userCode}, ${user.fullName}, ${user.country}, ${user.account}, ${user.usage}, ${user.phone}, ${user.created}, ${user.status}, ${user.password}, ${user.balance} , ${user.exposure}, ${user.isAdmin})""".update().apply()
    }
    user
  }

  def insertAccountsActivityLog(accountsActivityLog: AccountsActivityLog): Try[AccountsActivityLog] = Try {
    NamedDB(Symbol("auth")).localTx { implicit session =>
      sql"""insert into usersAccountsActivityHistory(id,
               action,
               code,
               name,
               country,
               account,
               usage,
               phone,
               created,
               updated,
               status)
       values (nextval('accounts_activity_sequence'),
               ${accountsActivityLog.action},
               ${accountsActivityLog.userData.code},
               ${accountsActivityLog.userData.name},
               ${accountsActivityLog.userData.country},
               ${accountsActivityLog.userData.account},
               ${accountsActivityLog.userData.usage},
               ${accountsActivityLog.userData.phone},
               ${accountsActivityLog.userData.created},
               ${accountsActivityLog.userData.updated},
               ${accountsActivityLog.userData.status})""".update().apply()
    }
    accountsActivityLog
  }

  def getAllAccountsActivityLog: Try[Seq[AccountsActivityLog]] = Try {
    NamedDB(Symbol("auth")).readOnly { implicit session => sql"select * from usersAccountsActivityHistory".map(AccountsActivityLog.fromRS).list().apply()
    }
  }
  def insertMoneyTransactionLog(moneyTransactionsLog: MoneyTransactionsLog): Try[MoneyTransactionsLog] = Try {
    NamedDB(Symbol("auth")).localTx { implicit session =>
      sql"""insert into usersMoneyTransactionsHistory(id,
               action,
               userCode,
               name,
               amount,
               dateTime)
       values (nextval('money_transaction_sequence'),
               ${moneyTransactionsLog.action},
               ${moneyTransactionsLog.userMoneyData.userCode},
               ${moneyTransactionsLog.userMoneyData.name},
               ${moneyTransactionsLog.userMoneyData.amount},
               ${moneyTransactionsLog.userMoneyData.dateTime})""".update().apply()
    }
    moneyTransactionsLog
  }

  def getAllMoneyTransactionsLog: Try[Seq[MoneyTransactionsLog]] = Try {
    NamedDB(Symbol("auth")).readOnly { implicit session => sql"select * from usersMoneyTransactionsHistory".map(MoneyTransactionsLog.fromRS).list().apply()
    }
  }

  def updateUserData(userCode: String, userData: UserData): Try[String] = Try {
    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update users set full_name = ${userData.name} where user_code = $userCode""".update().apply()
    }
    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update users set country = ${userData.country} where user_code = $userCode""".update().apply()
    }
    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update users set account = ${userData.account} where user_code = $userCode""".update().apply()
    }
    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update users set usage = ${userData.usage} where user_code = $userCode""".update().apply()
    }
    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update users set phone = ${userData.phone} where user_code = $userCode""".update().apply()
    }
    userCode
  }
  def updateUser(userCode: String,balance: Double): Try[String] = Try {
    NamedDB(Symbol("auth")).localTx { implicit session =>
      val result = sql"""update users set balance = $balance where user_code = $userCode""".update().apply()
    }
    userCode
  }

  def checkUser(userCode: String, password: String): Try[User] = Try {
    NamedDB(Symbol("auth")).readOnly { implicit session =>
      val maybeUser = sql"select * from users where user_code = $userCode".
        map(User.fromRS).single().apply()
      maybeUser match {
        case Some(user) =>
          if (BCrypt.checkpw(password, user.password)) user
          else throw new Exception("Incorrect Password!")
        case None => throw new Exception("User Not Authorized!")
      }
    }
  }
}
