package models

import controllers.BoardController._
import controllers.CommentController._
import controllers.routes

/**
 * Created by wshino on 2015/01/13.
 */
case class BoardWithCommentsResponse(board: Board, comments: List[Comment])
object BoardWithCommentsResponse {

  def find(id: Long) = Board.find(id).map( b => BoardWithCommentsResponse(b, Comment.findByBoardId(b.id)))

}
