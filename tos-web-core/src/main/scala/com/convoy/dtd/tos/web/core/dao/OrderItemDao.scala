package com.convoy.dtd.tos.web.core.dao

import com.convoy.dtd.johnston.domain.api.dao.GenericDao
import com.convoy.dtd.tos.web.api.entity.{OrderItemBean}

trait OrderItemDao extends GenericDao[OrderItemBean, Long]{
  def getOrderItemByOrderId(orderId: Long): List[OrderItemBean]
}
