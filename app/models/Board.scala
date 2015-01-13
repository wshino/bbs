package models

import models.Storage.Comment
import scalikejdbc._

/**
 * Created by wshino on 2015/01/08.
 */
case class BoardWithComment(id: Long, title: String)
case class Board(id: Long, title: String){
  def save()(implicit session: DBSession = Board.autoSession): Board = Board.save(this)(session)
}

object Board extends SQLSyntaxSupport[Board] {

  def apply(c: SyntaxProvider[Board])(rs: WrappedResultSet): Board = apply(c.resultName)(rs)
  def apply(c: ResultName[Board])(rs: WrappedResultSet): Board = new Board(
    id = rs.get(c.id),
    title = rs.get(c.title)
  )

  val c = Board.syntax("c")

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Board] = withSQL {
    select.from(Board as c).where.eq(c.id, id)
  }.map(Board(c)).single.apply()

  def findAll()(implicit session: DBSession = autoSession): List[Board] = withSQL {
    select.from(Board as c)
      .orderBy(c.id)
  }.map(Board(c)).list.apply()

  def save(m: Board)(implicit session: DBSession = autoSession): Board = {
    withSQL {
      update(Board).set(
        column.title -> m.title
      ).where.eq(column.id, m.id)
    }.update.apply()
    m
  }

  def create(m: Board)(implicit session: DBSession = autoSession): Board = {
    val id = withSQL {
      insert.into(Board).namedValues(
        column.title -> m.title
      )
    }.updateAndReturnGeneratedKey.apply()
    Board(id, m.title)
  }
}