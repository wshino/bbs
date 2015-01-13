package controllers

import models.{Comment, Board}
import org.joda.time.DateTime
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import models.Storage._

/**
 * Created by wshino on 2015/01/08.
 */
object CommentController extends Controller {

  case class CommentData(id: Long, comment: String)

  val commentForm = Form(
    mapping(
      "id" -> longNumber,
      "comment" -> text
    )(CommentData.apply)(CommentData.unapply)
  )

  def create = Action { implicit request =>

    val commentData = commentForm.bindFromRequest.get
    val res = Comment.create(Comment.apply(0, commentData.comment, commentData.id, new DateTime()))

    Redirect(routes.BoardController.show(res.boardId))
  }

}
