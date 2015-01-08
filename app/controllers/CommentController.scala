package controllers

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

    val res = boards.map { x =>
      x match {
        case f if f.id == commentData.id => {
          val newcomments = (Comment.apply(commentData.comment, new DateTime()) :: f.comment).reverse
          val newBoard = Board.apply(id = x.id, title = x.title, comment = newcomments)
          newBoard
        }
        case _ => x
      }

    }
    boards = res
    Redirect(routes.BoardController.show(commentData.id))
  }

}
