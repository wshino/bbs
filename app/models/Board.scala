package models

import scalikejdbc._

/**
 * Created by wshino on 2015/01/08.
 */
case class BoardWithComment(id: Long, title: String)
case class Board(id: Long, title: String){
  def save()(implicit session: DBSession = Board.autoSession): Board = Board.save(this)(session)
}

object Board extends SQLSyntaxSupport[Board] {

  def apply(b: SyntaxProvider[Board])(rs: WrappedResultSet): Board = apply(b.resultName)(rs)
  def apply(b: ResultName[Board])(rs: WrappedResultSet): Board = new Board(
    id = rs.get(b.id),
    title = rs.get(b.title)
  )

  val b = Board.syntax("b")

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Board] = withSQL {
    select.from(Board as b).where.eq(b.id, id)
  }.map(Board(b)).single.apply()

  def findAll()(implicit session: DBSession = autoSession): List[Board] = withSQL {
    select.from(Board as b)
      .orderBy(b.id)
  }.map(Board(b)).list.apply()

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