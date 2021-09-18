package com.convoy.dtd.tos.web.core.mvc


import com.convoy.dtd.tos.web.api.service.OrderService
import javax.inject.Inject
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(value = Array("/api/tea-session/order"))
private[mvc] class OrderController {

  @Inject
  private var orderService: OrderService = _



  @RequestMapping(value = Array("add"), method = Array(RequestMethod.POST))
  def createOrder(@RequestBody(required = true) teaSessionId: Long, @RequestBody(required = true) userId: Long): Map[String,Any] =
  {
    orderService.createOrder(teaSessionId, userId)
  }


  @RequestMapping(value = Array("get-by-id"), method = Array(RequestMethod.POST))
  def getOrderById(@RequestParam(required = true) orderId: Long): Map[String,Any] =
  {
    orderService.getOrderById(orderId)
  }


  @RequestMapping(value = Array("delete"), method = Array(RequestMethod.POST))
  def deleteOrderById(@RequestParam(required = true) orderId: Long): Map[String,Any] =
  {
    orderService.deleteOrderById(orderId)
  }


}
