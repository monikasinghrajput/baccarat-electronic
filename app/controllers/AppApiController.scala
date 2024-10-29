package controllers

import play.api.libs.json.Json
import play.api.mvc._
import dao.{ConfigDao}
import model.common.messages.{TableLimit, TableLimitCodec}

class AppApiController(components: ControllerComponents, configDao: ConfigDao) 
  extends AbstractController(components) 
  with TableLimitCodec {

  def sendRequestContentJson: Action[AnyContent] = Action { request =>
    val startTime: Long = System.currentTimeMillis()

    Ok(Json.prettyPrint(
      Json.obj(
        "time_elapsed" -> Json.toJson(s"${System.currentTimeMillis() - startTime} ms"),
        "result" -> Json.obj(
          "remoteAddress" -> Json.toJson(request.remoteAddress),
          "domain" -> Json.toJson(request.domain),
          "host" -> Json.toJson(request.host),
          "charSet" -> Json.toJson(request.charset.getOrElse("")),
          "contentType" -> Json.toJson(request.contentType.getOrElse("")),
          "acceptedTypes" -> Json.toJson(request.acceptedTypes.map(mr => mr.toString())),
          "acceptLanguages" -> Json.toJson(request.acceptLanguages),
          "name" -> Json.toJson(request.getQueryString("name")),
          "id" -> Json.toJson(request.id),
          "path" -> Json.toJson(request.path),
          "qs" -> Json.toJson(request.rawQueryString),
          "version" -> Json.toJson(request.version),
          "uri" -> Json.toJson(request.uri),
          "method" -> Json.toJson(request.method),
        ),
      )
    )
    )
  }


}
