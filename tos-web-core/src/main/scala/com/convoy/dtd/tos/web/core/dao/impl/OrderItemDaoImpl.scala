package com.convoy.dtd.tos.web.core.dao.impl

import com.convoy.dtd.johnston.domain.jpa.dao.AbstractGenericDao
import com.convoy.dtd.tos.web.api.entity.{OrderItemBean, QOrderItemBean}
import com.convoy.dtd.tos.web.core.dao.OrderItemDao
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import collection.JavaConverters._

@Repository
private[impl] class OrderItemDaoImpl extends AbstractGenericDao[OrderItemBean, Long] with OrderItemDao{

  override def checkExists(orderId: Long, menuItemId: Long): Option[OrderItemBean] =
  {
    val q = new JPAQueryFactory((entityManager))
    Option(q.selectFrom(QOrderItemBean).where(QOrderItemBean.orderOrderItem.orderId === orderId && QOrderItemBean.menuItemOrderItem.menuItemId === menuItemId)
      .fetchOne())
  }

  override def findByOrderId(orderId: Long): List[OrderItemBean] =
  {
    val q = new JPAQueryFactory((entityManager))
    q.selectFrom(QOrderItemBean).where(QOrderItemBean.orderOrderItem.orderId === orderId).fetch().asScala.toList
  }
}
