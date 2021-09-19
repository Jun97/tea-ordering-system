package com.convoy.dtd.tos.web.core.mvc

import com.convoy.dtd.tos.web.api.service.OrderItemService
import javax.inject.Inject
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(value = Array("/api/tea-session/order/order-item"))
private[mvc] class OrderItemController {

  @Inject
  private var orderItemService: OrderItemService = _


  @RequestMapping(value = Array("add"), method = Array(RequestMethod.POST))
  def createOrder(@RequestParam(required = true) orderId: Long, @RequestParam(required = true) menuItemId: Long, @RequestParam(required = true) quantity: Long): Map[String,Any] =
  {
    orderItemService.createOrderItem(orderId, menuItemId, quantity)
  }


  @RequestMapping(value = Array("get-by-order-id"), method = Array(RequestMethod.POST))
  def getOrderByOrderId(@RequestParam(required = true) orderId: Long): Map[String,Any] =
  {
    orderItemService.getOrderItemByOrderId(orderId)
  }


  @RequestMapping(value = Array("update"), method = Array(RequestMethod.POST))
  def updateOrderItemById(@RequestParam(required = true) orderItemId: Long, @RequestParam(required = true) quantity: Long): Map[String,Any] =
  {
    orderItemService.updateOrderItemById(orderItemId, quantity)
  }


  @RequestMapping(value = Array("delete"), method = Array(RequestMethod.POST))
  def deleteOrderItemById(@RequestParam(required = true) orderItemId: Long): Map[String,Any] =
  {
    orderItemService.deleteOrderItemById(orderItemId)
  }


}
