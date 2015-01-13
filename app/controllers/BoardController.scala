package controllers

import models.{BoardWithCommentsResponse, Board}
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import controllers.CommentController._
import models.Storage._

object BoardController extends Controller {

  case class BoardData(title: String)

  val boardForm = Form(
    mapping(
      "title" -> text
    )(BoardData.apply)(BoardData.unapply)
  )

  def index = Action {
    implicit request =>
      Ok(views.html.Boards.index(Board.findAll())(boardForm))
  }

  def create = Action { implicit request =>
    val boardData = boardForm.bindFromRequest.get
    val created = Board.create(Board.apply(0, boardData.title))
    Redirect(routes.BoardController.show(created.id))
  }

  def delete(id: Long) = Action {
    boards = boards.filter(_.id != id)
    Ok(boards.toString())
  }

  def show(id: Long) = Action {
    BoardWithCommentsResponse.find(id) match {
      case Some(x) => Ok(views.html.Boards.show(x)(commentForm))
      case None => Redirect(routes.BoardController.index())
    }
  }


}