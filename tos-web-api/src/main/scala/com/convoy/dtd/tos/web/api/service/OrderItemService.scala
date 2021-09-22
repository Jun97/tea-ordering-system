package com.convoy.dtd.tos.web.api.service

import com.convoy.dtd.tos.web.api.entity.OrderItemBean

trait OrderItemService
{

  def add(orderId: Long, menuItemId: Long, quantity: Long): Map[String, Any]
  def findByOrderId(orderId: Long): List[OrderItemBean]
  def updateById(orderId: Long, quantity: Long): Map[String, Any]
  def deleteById(orderItemId: Long): Map[String, Any]
}
