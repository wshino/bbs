package models

import models.Storage.Comment
import org.joda.time.DateTime
import scalikejdbc._
/**
 * Created by wshino on 2015/01/08.
 */

object Storage {

  var boards: List[Board] = List()

  case class Comment(text: String, createdAt: DateTime)


}