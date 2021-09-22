package com.convoy.dtd.tos.web.core.mvc

import com.convoy.dtd.tos.web.api.entity.OrderItemBean
import com.convoy.dtd.tos.web.api.service.OrderItemService
import javax.inject.Inject
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(value = Array("/api/tea-session/order/order-item"))
private[mvc] class OrderItemController {

  @Inject
  private var orderItemService: OrderItemService = _


  @RequestMapping(value = Array("add"), method = Array(RequestMethod.POST))
  def add(@RequestParam(required = true) orderId: Long, @RequestParam(required = true) menuItemId: Long, @RequestParam(required = true) quantity: Long): Map[String,Any] =
  {
    orderItemService.add(orderId, menuItemId, quantity)
  }


  @RequestMapping(value = Array("find-by-order-id"), method = Array(RequestMethod.POST))
  def findByOrderId(@RequestParam(required = true) orderId: Long): List[OrderItemBean] =
  {
    orderItemService.findByOrderId(orderId)
  }


  @RequestMapping(value = Array("update-by-id"), method = Array(RequestMethod.POST))
  def updateById(@RequestParam(required = true) orderItemId: Long, @RequestParam(required = true) quantity: Long): Map[String,Any] =
  {
    orderItemService.updateById(orderItemId, quantity)
  }


  @RequestMapping(value = Array("delete-by-id"), method = Array(RequestMethod.POST))
  def deleteById(@RequestParam(required = true) orderItemId: Long): Map[String,Any] =
  {
    orderItemService.deleteById(orderItemId)
  }


}
