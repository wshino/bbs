package controllers

import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

object Board extends Controller {

  case class CommentData(id: Long, comment: String)

  val commentForm = Form(
    mapping(
      "id" -> longNumber,
      "comment" -> text
    )(CommentData.apply)(CommentData.unapply)
  )

  case class BoardData(title: String)

  val boardForm = Form(
    mapping(
      "title" -> text
    )(BoardData.apply)(BoardData.unapply)
  )

  case class Comment(text: String, createdAt: org.joda.time.DateTime)
  case class Board(id: Long, title: String, comment: List[Comment])

  var boards: List[Board] = List()

  def index = Action {
    implicit request =>
      Ok(views.html.Boards.index(boards)(boardForm))
  }

  def create = Action { implicit request =>
    val boardData = boardForm.bindFromRequest.get

    val created = Board.apply(boards.size + 1, boardData.title, List())
    boards = created :: boards
    Redirect(routes.Board.show(created.id))
  }

  def delete(id: Long) = Action {
    boards = boards.filter(_.id != id)
    Ok(boards.toString())
  }

  def show(id: Long) = Action {
    val res = boards.filter(_.id == id).headOption
    res match {
      case Some(x) => Ok(views.html.Boards.show(x)(commentForm))
      case None => Redirect(routes.Board.index())
    }
  }

  //  def add(boardId: Long, comment: String) = Action {
  def add = Action { implicit request =>

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
    Redirect(routes.Board.show(commentData.id))
  }

}