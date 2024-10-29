package services

import java.security.MessageDigest
import java.util.{Base64, UUID}
import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration
import scala.util.{Success, Try}

import play.api.Logger
import play.api.mvc.{Cookie, RequestHeader}

import model.dao.{User, UserSession}
import dao.postgres.{SessionDao, UserDao}

class AuthService(sessionDao: SessionDao, userDao: UserDao) {

  val log = Logger(this.getClass)


  def login(userCode: String, password: String): Try[Cookie] = {
    val userT = userDao.checkUser(userCode, password)
    userT.flatMap { user =>
      createCookie(user)
    }
  }

  val cookieHeader = "X-Auth-Token"

  def checkCookie(header: RequestHeader): Try[Option[User]] = {
    val maybeCookie = header.cookies.get(cookieHeader)
    val maybeUserT = maybeCookie match {
      case Some(cookie) =>{
        val maybeUserSessionT = sessionDao.findSession(cookie.value)
        maybeUserSessionT.flatMap {
          case Some(userSession) => {
            userDao.findById(userSession.userId)
          }
          case None => Success(None)
        }
      }
      case None => Success(None)
    }
    maybeUserT
  }

  def destroySession(header: RequestHeader): Try[Unit] = {
    val maybeCookie = header.cookies.get(cookieHeader)
    maybeCookie match {
      case Some(cookie) => {
        sessionDao.deleteSession(cookie.value)
      }
      case None => Success()
    }
  }

  val mda: MessageDigest = MessageDigest.getInstance("SHA-512")

  private def createCookie(user: User): Try[Cookie] = {
    val randomPart = UUID.randomUUID().toString.toUpperCase
    val userPart = user.userId.toString.toUpperCase
    val key = s"$randomPart|$userPart"
    val token = Base64.getEncoder.encodeToString(mda.digest(key.getBytes))
    val duration = Duration.create(10, TimeUnit.HOURS)
    val userSession = UserSession.create(user, token, duration.toSeconds)
    val insertT = sessionDao.insertSession(userSession)
    insertT.map { insert =>
      Cookie(cookieHeader, token, maxAge = Some(duration.toSeconds.toInt), httpOnly = true)
    }
  }
}
