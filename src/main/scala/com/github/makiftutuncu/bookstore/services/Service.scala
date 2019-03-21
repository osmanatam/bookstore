package com.github.makiftutuncu.bookstore.services

import cats.effect.Effect
import com.github.makiftutuncu.bookstore.data.DAO
import com.github.makiftutuncu.bookstore.models.Model
import com.github.makiftutuncu.bookstore.utilities.Maybe
import com.github.makiftutuncu.bookstore.views.{CreateView, UpdateView, View}

import scala.language.higherKinds

abstract class Service[F[_], M <: Model, V <: View, CV <: CreateView, UV <: UpdateView](val dao: DAO[F, M, CV, UV])(implicit F: Effect[F]) {
  def getAll: Maybe[F, List[V]]

  def getByName(name: String): Maybe[F, List[V]]

  def toView(model: M): Maybe[F, V]
}
