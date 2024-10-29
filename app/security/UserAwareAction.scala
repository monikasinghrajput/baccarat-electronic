package security

import model.dao.User
import play.api.Logger
import play.api.mvc._
import services.AuthService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class UserAwareRequest[A](user: Option[User],
                               request: Request[A]
                              ) extends WrappedRequest[A](request)

class UserAwareAction(authService: AuthService, ec: ExecutionContext,
                      playBodyParsers: PlayBodyParsers)
  extends ActionBuilder[UserAwareRequest, AnyContent] {

  override implicit val executionContext = ec

  override def parser = playBodyParsers.defaultBodyParser

  def invokeBlock[A](request: Request[A],
                     block: UserAwareRequest[A] => Future[Result]): Future[Result] = {
    val maybeUserD = authService.checkCookie(request)
    maybeUserD match {
      case Success(None) => block(UserAwareRequest(None, request))
      case Success(Some(user)) => block(UserAwareRequest(Some(user), request))
      case Failure(th) => UserAwareAction.exception(th)
    }
  }
}

object UserAwareAction {
  val log = Logger("security.UserAwareAction")

  def exception(exc: Throwable): Future[Result] = {
    log.error("Exception occurred while invoking authenticated action", exc)
    Future.successful(Results.Redirect("/500"))
  }
}