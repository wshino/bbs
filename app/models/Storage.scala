package models

import org.joda.time.DateTime

/**
 * Created by wshino on 2015/01/08.
 */

object Storage {

  var boards: List[Board] = List()

  case class Comment(text: String, createdAt: DateTime)
  case class Board(id: Long, title: String, comment: List[Comment])



}
