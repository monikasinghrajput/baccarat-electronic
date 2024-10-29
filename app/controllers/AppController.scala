package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import controllers.Assets.Asset
import model.domain._
import play.api.Logger
import play.api.mvc._
import security.{UserAuthAction, UserAwareAction}
import services.{GameService, UserAccountsService}


class AppController(components: ControllerComponents,
                    assets: Assets,
                    actorSystem: ActorSystem,
                    gameService: GameService,
                    userAccountsService: UserAccountsService,
                    mat: Materializer,
                    userAwareAction: UserAwareAction,
                    userAuthAction: UserAuthAction)
  extends AbstractController(components) with GameViewCodec with ProviderCodec {

  val log = Logger(this.getClass)
  implicit val materializer = mat
  implicit val actorFactory = actorSystem


  def sendLobbyPage: Action[AnyContent] = userAwareAction { request =>
    Ok(views.html.pages.lobby())
  }


  def error500(): Action[AnyContent] = Action {
    InternalServerError(views.html.errorPage())
  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
