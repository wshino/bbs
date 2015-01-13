package models

import org.joda.time.DateTime
import scalikejdbc._

/**
 * Created by wshino on 2015/01/08.
 */
case class Comment(id: Long, text: String, boardId: Long, createdAt: DateTime){
  def save()(implicit session: DBSession = Comment.autoSession): Comment = Comment.save(this)(session)
}

object Comment extends SQLSyntaxSupport[Comment] {

  def apply(c: SyntaxProvider[Comment])(rs: WrappedResultSet): Comment = apply(c.resultName)(rs)
  def apply(c: ResultName[Comment])(rs: WrappedResultSet): Comment = new Comment(
    id = rs.get(c.id),
    text = rs.get(c.text),
    boardId = rs.get(c.boardId),
    createdAt = rs.get(c.createdAt)
  )

  val c = Comment.syntax("c")

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Comment] = withSQL {
    select.from(Comment as c).where.eq(c.id, id)
  }.map(Comment(c)).single.apply()

  def findByBoardId(id: Long)(implicit session: DBSession = autoSession): List[Comment] = withSQL {
    select.from(Comment as c).where.eq(c.boardId, id)
  }.map(Comment(c)).list().apply()

  def findAll()(implicit session: DBSession = autoSession): List[Comment] = withSQL {
    select.from(Comment as c)
      .orderBy(c.id)
  }.map(Comment(c)).list.apply()

  def save(m: Comment)(implicit session: DBSession = autoSession): Comment = {
    withSQL {
      update(Comment).set(
        column.text -> m.text,
        column.boardId -> m.boardId,
        column.createdAt -> m.createdAt
      ).where.eq(column.id, m.id)
    }.update.apply()
    m
  }

  def create(m: Comment)(implicit session: DBSession = autoSession): Comment = {
    val id = withSQL {
      insert.into(Comment).namedValues(
        column.text -> m.text,
        column.boardId -> m.boardId,
        column.createdAt -> m.createdAt
      )
    }.updateAndReturnGeneratedKey.apply()
    Comment(id, m.text, m.boardId, m.createdAt)
  }
}