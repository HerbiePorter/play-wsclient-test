package wsclient

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.{Logger, LoggerLike}

import scala.concurrent.ExecutionContext

@Singleton
class EchoSessionController @Inject()()
  (cc: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  protected val logger: LoggerLike = Logger(this.getClass)

  val echoSession: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    if (request.session.get("session-exists").isDefined) {
      logger.warn(s"Session exists. [$request] [${request.headers.get("Cookie")}]")
      Ok(Json.toJson(request.session.data))
    } else {
      logger.warn(s"Session does not exist. [$request] [${request.headers.get("Cookie")}]")
      Ok(Json.toJson(request.session.data)).addingToSession("session-exists" -> "true")
    }
  }
}
