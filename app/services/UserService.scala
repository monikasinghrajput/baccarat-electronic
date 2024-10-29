package services

import java.util.UUID
import scala.util.{Failure, Success}
import play.api.Logger

import dao.postgres.UserDao
import model.admin.{AccountsActivityLog, MoneyTransactionsLog, UserData}
import model.dao.User

/*
  Allows an alternative to TITO (Ticket in/ Ticket out)
  Use it to enable account based play for users
    - First Name
    - Last Name
    - email
    - phone
    - 
*/

class UserAccountsService(userDao: UserDao) {
  val log = Logger(this.getClass)

  def init():Unit = {

  }

  def getUserFullName(userId: UUID): Option[String] = {
    userDao.findById(userId) match {
      case Success(maybeUser) => maybeUser.map(_.fullName)
      case Failure(_) => None
    }
  }

  def updateUserBalance(userCode: String,balance: Double) : Option[String] = {
    userDao.updateUser(userCode,balance) match {
      case Success(maybeUserCode) => Some(maybeUserCode)
      case Failure(_) => None
    }
  }

  def updateUserData(userCode: String, userData: UserData) : Option[String]  = {
    userDao.updateUserData(userCode, userData) match {
      case Success(maybeUserCode) => Some(maybeUserCode)
      case Failure(_) => None
    }
  }

  def insertUser(user: User): Option[User] = {
    userDao.insertUser(user) match {
      case Success(maybeUser) => Some(maybeUser)
      case Failure(_) => None
    }
  }


  def getUserBalance(userCode: String): Option[Double] = {
    userDao.findByUserCode(userCode) match {
      case Success(maybeUser) => maybeUser.map(_.balance)
      case Failure(_) => None
    }
  }

  def getUsers:Seq[User] = {
    userDao.getUsers match {
      case Success(users) => users
      case Failure(_) => Seq.empty[User]
    }
  }

  //Insert Accounts Activity Log
  def insertAccountsActivityLog(accountsActivityLog: AccountsActivityLog): Option[AccountsActivityLog] = {
    userDao.insertAccountsActivityLog(accountsActivityLog) match {
      case Success(maybeAccountsActivityLog) => Some(maybeAccountsActivityLog)
      case Failure(_) => None
    }
  }

  //Insert Money Transaction Log
  def insertMoneyTransactionLog(moneyTransactionsLog: MoneyTransactionsLog): Option[MoneyTransactionsLog] = {
    userDao.insertMoneyTransactionLog(moneyTransactionsLog) match {
      case Success(maybeMoneyTransactionsLog) => Some(maybeMoneyTransactionsLog)
      case Failure(_) => None
    }
  }

  //Get All Money Transaction Logs
  def getAllMoneyTransactionsLog: Seq[MoneyTransactionsLog] = {
    userDao.getAllMoneyTransactionsLog match {
      case Success(logs) => logs.reverse
      case Failure(_) => Seq.empty[MoneyTransactionsLog]
    }
  }

  //Get All Accounts Activity Logs
  def getAllAccountsActivityLog:Seq[AccountsActivityLog] = {
    userDao.getAllAccountsActivityLog match {
      case Success(logs) => logs.reverse
      case Failure(_) => Seq.empty[AccountsActivityLog]
    }
  }


}
