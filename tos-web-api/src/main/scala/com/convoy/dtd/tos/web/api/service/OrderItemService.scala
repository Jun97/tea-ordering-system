package com.convoy.dtd.tos.web.api.service

trait OrderItemService
{

  def createOrderItem(orderId: Long, menuItemId: Long, quantity: Long): Map[String, Any]
  def getOrderItemByOrderId(orderId: Long): Map[String, Any]
  def updateOrderItemById(orderId: Long, quantity: Long): Map[String, Any]
  def deleteOrderItemById(orderItemId: Long): Map[String, Any]
}
