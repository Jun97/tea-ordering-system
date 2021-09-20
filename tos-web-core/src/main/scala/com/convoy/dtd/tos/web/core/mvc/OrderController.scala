package com.convoy.dtd.tos.web.core.mvc


import com.convoy.dtd.tos.web.api.service.OrderService
import javax.inject.Inject
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(value = Array("/api/tea-session/order"))
private[mvc] class OrderController {

  @Inject
  private var orderService: OrderService = _



  @RequestMapping(value = Array("add"), method = Array(RequestMethod.POST))
  def createOrder(@RequestParam(required = true) teaSessionId: Long, @RequestParam(required = true) userId: Long): Map[String,Any] =
  {
    orderService.createOrder(teaSessionId, userId)
  }


  @RequestMapping(value = Array("get-by-id"), method = Array(RequestMethod.POST))
  def getOrderById(@RequestParam(required = true) orderId: Long): Map[String,Any] =
  {
    orderService.getOrderById(orderId)
  }


  @RequestMapping(value = Array("order-summary"), method = Array(RequestMethod.POST))
  def getOrderSummaryByTeaSessionId(@RequestParam(required = true) teaSessionId: Long): Map[String,Any] =
  {
    orderService.getOrderSummaryByTeaSessionId(teaSessionId)
  }


  @RequestMapping(value = Array("delete"), method = Array(RequestMethod.POST))
  def deleteOrderById(@RequestParam(required = true) orderId: Long): Map[String,Any] =
  {
    orderService.deleteOrderById(orderId)
  }


}
