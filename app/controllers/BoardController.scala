package controllers

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
      Ok(views.html.Boards.index(boards)(boardForm))
  }

  def create = Action { implicit request =>
    val boardData = boardForm.bindFromRequest.get

    val created = Board.apply(boards.size + 1, boardData.title, List())
    boards = created :: boards
    Redirect(routes.BoardController.show(created.id))
  }

  def delete(id: Long) = Action {
    boards = boards.filter(_.id != id)
    Ok(boards.toString())
  }

  def show(id: Long) = Action {
    val res = boards.filter(_.id == id).headOption
    res match {
      case Some(x) => Ok(views.html.Boards.show(x)(commentForm))
      case None => Redirect(routes.BoardController.index())
    }
  }


}