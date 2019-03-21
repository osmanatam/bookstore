package com.github.makiftutuncu.bookstore

import cats.Id
import com.github.makiftutuncu.bookstore.data.DAO
import com.github.makiftutuncu.bookstore.models.Model
import com.github.makiftutuncu.bookstore.utilities.DoobieExtras
import com.typesafe.scalalogging.LazyLogging
import doobie.implicits._
import doobie.util.fragment.Fragment

abstract class TestDataGenerator[M <: Model](dao: DAO[Id, M, _, _], table: String) extends DoobieExtras with LazyLogging {
  protected def deleteSql(m: M): Fragment

  def all: List[M]

  def truncate(): Unit = truncateTable(table)

  def insert(model: M): Unit = {
    logger.debug(s"Inserting test data $model")

    dao.insertSql(model)
      .update
      .run
      .transact(dao.db.transactor)
  }

  def insertAll(): Unit = all foreach insert

  def delete(model: M): Unit = {
    logger.debug(s"Deleting test data $model")

    deleteSql(model)
      .update
      .run
      .transact(dao.db.transactor)
  }

  private def truncateTable(table: String): Unit = {
    logger.debug(s"Truncating table $table")

    val s = s"TRUNCATE TABLE $table CASCADE"

    Fragment.const(s)
      .update
      .run
      .transact(dao.db.transactor)
  }
}
