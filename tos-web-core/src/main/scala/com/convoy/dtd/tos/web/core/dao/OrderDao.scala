package com.convoy.dtd.tos.web.core.dao

import com.convoy.dtd.johnston.domain.api.dao.GenericDao
import com.convoy.dtd.tos.web.api.entity.OrderBean

trait OrderDao extends GenericDao[OrderBean, Long]{
  def checkExists(teaSessionId: Long, userId: Long): Option[OrderBean]
  def findByTeaSessionId(teaSessionId: Long): List[OrderBean]
  def getOrderSummaryByTeaSessionId(teaSessionId: Long): Map[String, Long]
}
