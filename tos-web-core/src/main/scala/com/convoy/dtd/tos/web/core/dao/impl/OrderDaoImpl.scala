package com.convoy.dtd.tos.web.core.dao.impl

import java.util.Calendar

import com.convoy.dtd.johnston.domain.jpa.dao.AbstractGenericDao
import com.convoy.dtd.tos.web.api.entity.{OrderBean, QOrderBean, QOrderItemBean}
import com.convoy.dtd.tos.web.core.dao.OrderDao
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import collection.JavaConverters._

@Repository
private[impl] class OrderDaoImpl extends AbstractGenericDao[OrderBean, Long] with OrderDao{

  override def checkExists(teaSessionId: Long, userId: Long): Option[OrderBean] =
  {
    val q = new JPAQueryFactory((entityManager))
    Option(q.selectFrom(QOrderBean).where(QOrderBean.teaSessionOrder.teaSessionId === teaSessionId && QOrderBean.userOrder.userId === userId)
      .fetchOne())
  }

  override def findByTeaSessionId(teaSessionId: Long): List[OrderBean] =
  {
    val q = new JPAQueryFactory(entityManager)
    q.selectFrom(QOrderBean).where(QOrderBean.teaSessionOrder.teaSessionId === teaSessionId).fetch().asScala.toList
  }

  override def getOrderSummaryByTeaSessionId(teaSessionId: Long): Map[String, Long] =
  {
    val q = new JPAQueryFactory(entityManager)
    q.select(QOrderItemBean.menuItemOrderItem.name, QOrderItemBean.quantity.sum).from(QOrderItemBean).groupBy(QOrderItemBean.menuItemOrderItem.name).where(QOrderItemBean.orderOrderItem.teaSessionOrder.teaSessionId === teaSessionId).fetch().asScala.map( res => {
      res.get(QOrderItemBean.menuItemOrderItem.name) -> res.get(QOrderItemBean.quantity.sum)
    }).toMap
  }
}
