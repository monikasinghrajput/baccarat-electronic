package controllers.baccarat


import actors.baccarat.BaccaratTableActor.CardDrawn
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsError, JsPath, JsValue, Json, Reads, Writes}
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import services.{BaccaratSSeaterTableService, GameService}
import model.baccarat.data.TableLimit
import model.common.messages.{Chip, ChipCodec}
import actors.baccarat.clients.{AdminActor, ClientActor, TopperActor}
import model.baccarat.Card

case class Draw(card: String)

class BaccaratSSeaterController(components: ControllerComponents,
                                actorSystem: ActorSystem,
                                gameService: GameService,
                                baccaratSSeaterTableService: BaccaratSSeaterTableService,
                                mat: Materializer)
  extends AbstractController(components) with ChipCodec  {

  val log = Logger(this.getClass)

  implicit val reads: Reads[Draw] = Json.reads[Draw]

  implicit val tableLimitReads: Reads[TableLimit] = (
    (JsPath \ "chips").read[Seq[Chip]] and
      (JsPath \ "Min_Bet").read[Int] and
      (JsPath \ "Max_Bet").read[Int] and
      (JsPath \ "Min_SideBet").read[Int] and
      (JsPath \ "Max_SideBet").read[Int] and
      (JsPath \ "Min_StraightUpBet").read[Int] and
      (JsPath \ "Max_StraightUpBet").read[Int] and
      (JsPath \ "Min_SplitBet").read[Int] and
      (JsPath \ "Max_SplitBet").read[Int] and
      (JsPath \ "Min_StreetBet").read[Int] and
      (JsPath \ "Max_StreetBet").read[Int] and
      (JsPath \ "Min_CornerBet").read[Int] and
      (JsPath \ "Max_CornerBet").read[Int] and
      (JsPath \ "Min_LineBet").read[Int] and
      (JsPath \ "Max_LineBet").read[Int] and
      (JsPath \ "Min_Dozen_ColumnBet").read[Int] and
      (JsPath \ "Max_Dozen_ColumnBet").read[Int] and
      (JsPath \ "Min_OutsideBet").read[Int] and
      (JsPath \ "Max_OutsideBet").read[Int] and
      (JsPath \ "Min_Tie").read[Int] and
      (JsPath \ "Max_Tie").read[Int]
    ) (TableLimit.apply _)

  implicit val tableLimitWrites: Writes[TableLimit] = (
    (JsPath \ "chips").write[Seq[Chip]] and
      (JsPath \ "Min_Bet").write[Int] and
      (JsPath \ "Max_Bet").write[Int] and
      (JsPath \ "Min_SideBet").write[Int] and
      (JsPath \ "Max_SideBet").write[Int] and
      (JsPath \ "Min_StraightUpBet").write[Int] and
      (JsPath \ "Max_StraightUpBet").write[Int] and
      (JsPath \ "Min_SplitBet").write[Int] and
      (JsPath \ "Max_SplitBet").write[Int] and
      (JsPath \ "Min_StreetBet").write[Int] and
      (JsPath \ "Max_StreetBet").write[Int] and
      (JsPath \ "Min_CornerBet").write[Int] and
      (JsPath \ "Max_CornerBet").write[Int] and
      (JsPath \ "Min_LineBet").write[Int] and
      (JsPath \ "Max_LineBet").write[Int] and
      (JsPath \ "Min_Dozen_ColumnBet").write[Int] and
      (JsPath \ "Max_Dozen_ColumnBet").write[Int] and
      (JsPath \ "Min_OutsideBet").write[Int] and
      (JsPath \ "Max_OutsideBet").write[Int] and
      (JsPath \ "Min_Tie").write[Int] and
      (JsPath \ "Max_Tie").write[Int]
    ) (unlift(TableLimit.unapply))

  implicit val materializer = mat
  implicit val actorFactory = actorSystem


  def sendPlayerPage: Action[AnyContent] = Action { request =>
    log.logger.info(s"sending baccarat player page for ${request.remoteAddress}")
    Ok(views.html.pages.baccarat.player())
  }

  def sendTopperPage: Action[AnyContent] = Action { request =>
    log.logger.info(s"sending baccarat topper page for ${request.remoteAddress}")
    Ok(views.html.pages.baccarat.topper())
  }

  def sendAdminPage: Action[AnyContent] = Action { request =>
    log.logger.info(s"sending baccarat admin page for ${request.remoteAddress}")
    Ok(views.html.pages.baccarat.admin())
  }


  def player: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
    log.logger.info(s"player socket request from ${request.remoteAddress}");

    ActorFlow.actorRef { out =>
      ClientActor.props(out, baccaratSSeaterTableService.getBaccaratTableActor, gameService.getLoggingActor, request.remoteAddress)
    }
  }


  def topper: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
    log.logger.info(s"Baccarat Client: topper socket request from ${request.remoteAddress}");

    ActorFlow.actorRef { out =>
      TopperActor.props(out, baccaratSSeaterTableService.getBaccaratTableActor, gameService.getLoggingActor, request.remoteAddress)
    }
  }

  def admin: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
    log.logger.info(s"admin socket request from ${request.remoteAddress}");

    ActorFlow.actorRef { out =>
      AdminActor.props(out, baccaratSSeaterTableService.getBaccaratTableActor, gameService.getLoggingActor, request.remoteAddress)
    }
  }


  //API Services for Roulette Stadium Seater Table

  def sendInitialDataJson: Action[AnyContent] = Action(
    Ok(baccaratSSeaterTableService.getInitialDataJsonString).withHeaders("Access-Control-Allow-Origin" -> "*",
      "Allow" -> "*",
      "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept, Referrer, User-Agent, X-Auth-Token, X-Api-Key")
  )

  def sendAuthenticateJson: Action[AnyContent] = Action(
    Ok(baccaratSSeaterTableService.authenticateJsonString).withHeaders("Access-Control-Allow-Origin" -> "*",
      "Allow" -> "*",
      "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept, Referrer, User-Agent, X-Auth-Token, X-Api-Key")
  )

  def sendStreamsJson: Action[AnyContent] = Action(
    Ok(baccaratSSeaterTableService.sendStreamsJsonString).withHeaders("Access-Control-Allow-Origin" -> "*",
      "Allow" -> "*",
      "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept, Referrer, User-Agent, X-Auth-Token, X-Api-Key")
  )


  def sendTableLimitsJson: Action[AnyContent] = Action(
    Ok(baccaratSSeaterTableService.sendTableLimitsJsonString)
  )

  /*

  curl --include --request POST --header "Content-type: application/json" --data '{
          "id": 992712,
          "chips": [
            {
              "color": "cyan",
              "value": 1,
              "img": "https://conf.livetables.io/CustomAssets//chips/default_cyan.png",
              "default": false
            },
            {
              "color": "red",
              "value": 5,
              "img": "https://conf.livetables.io/CustomAssets//chips/default_red.png",
              "default": false
            },
            {
              "color": "blue",
              "value": 10,
              "img": "https://conf.livetables.io/CustomAssets//chips/default_blue.png",
              "default": false
            },
            {
              "color": "violet",
              "value": 50,
              "img": "https://conf.livetables.io/CustomAssets//chips/default_violet.png",
              "default": false
            },
            {
              "color": "green",
              "value": 100,
              "img": "https://conf.livetables.io/CustomAssets//chips/default_green.png",
              "default": false
            },
            {
              "color": "yellow",
              "value": 200,
              "img": "https://conf.livetables.io/CustomAssets//chips/default_yellow.png",
              "default": false
            },
            {
              "color": "black",
              "value": 500,
              "img": "https://conf.livetables.io/CustomAssets//chips/default_black.png",
              "default": false
            }
          ],
          "Min_Bet": 1,
          "Max_Bet": 2000,
          "Min_SideBet": 1,
          "Max_SideBet": 200,
          "Min_StraightUpBet": 0,
          "Max_StraightUpBet": 0,
          "Min_SplitBet": 0,
          "Max_SplitBet": 0,
          "Min_StreetBet": 0,
          "Max_StreetBet": 0,
          "Min_CornerBet": 0,
          "Max_CornerBet": 0,
          "Min_LineBet": 0,
          "Max_LineBet": 0,
          "Min_Dozen_ColumnBet": 0,
          "Max_Dozen_ColumnBet": 0,
          "Min_OutsideBet": 0,
          "Max_OutsideBet": 0,
          "Min_Tie": 1,
          "Max_Tie": 500
        }'
   http://localhost:5000/api/baccarat/tableLimits

   */
  def saveTableLimitsJson = Action(parse.json) { request =>
    val tableLimitResult = request.body.validate[TableLimit]
    tableLimitResult.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      tableLimit => {
        baccaratSSeaterTableService.saveTableLimits(tableLimit)
        Ok(Json.obj("message" -> ("Table Limits saved.")))
      }
    )
  }

  def handleCardDrawn = Action(parse.json) { request =>
    log.info(s"${request.body} received")

    val drawCard = request.body.validate[Draw]
    drawCard.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      draw => {
        val baccratCard: Card = Card.parseBeeTekCard(draw.card).getOrElse(Card())
        baccaratSSeaterTableService.getBaccaratTableActor ! CardDrawn(baccratCard)
        Ok(Json.obj("message" -> ("Card is Forwarded Successfully.")))
      }
    )
  }

}
