package com.convoy.dtd.tos.web.api.service

trait OrderService
{

  def createOrder(teaSessionId: Long, userId: Long): Map[String, Any]
  def getOrderById(orderId: Long): Map[String, Any]
  def deleteOrderById(menuItemId: Long): Map[String, Any]
}
