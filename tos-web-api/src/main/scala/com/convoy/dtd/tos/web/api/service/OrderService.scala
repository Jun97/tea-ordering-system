package com.convoy.dtd.tos.web.api.service

import com.convoy.dtd.tos.web.api.entity.OrderBean

trait OrderService
{

  def add(teaSessionId: Long, userId: Long): Map[String, Any]
  def getById(orderId: Long): Option[OrderBean]
  def findByTeaSessionId(teaSessionId: Long): List[OrderBean]
  def getOrderSummaryByTeaSessionId(teaSessionId: Long): Map[String, Any]
  def deleteById(menuItemId: Long): Map[String, Any]
}
