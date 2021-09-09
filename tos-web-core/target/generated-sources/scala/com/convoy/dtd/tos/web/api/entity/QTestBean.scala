package com.convoy.dtd.tos.web.api.entity

import com.querydsl.core.types._
import com.querydsl.scala._

import com.querydsl.core.types.PathMetadataFactory._;

object QTestBean extends QTestBean("testBean") {
  override def as(variable: String) = new QTestBean(variable)
  
}

class QTestBean(cl: Class[_ <: TestBean], md: PathMetadata) extends EntityPathImpl[TestBean](cl, md) {
  def this(variable: String) = this(classOf[TestBean], forVariable(variable))

  def this(parent: Path[_], variable: String) = this(classOf[TestBean], forProperty(parent, variable))

  val hello = createString("hello")

  val testId = createNumber[Long]("testId")

  val world = createNumber[Long]("world")

}

