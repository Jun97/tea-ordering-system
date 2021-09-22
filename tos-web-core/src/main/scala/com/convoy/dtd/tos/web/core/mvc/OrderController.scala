package com.convoy.dtd.tos.web.core.mvc


import com.convoy.dtd.tos.web.api.entity.OrderBean
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
  def add(@RequestParam(required = true) teaSessionId: Long, @RequestParam(required = true) userId: Long): Map[String,Any] =
  {
    orderService.add(teaSessionId, userId)
  }


  @RequestMapping(value = Array("get-by-id"), method = Array(RequestMethod.POST))
  def getById(@RequestParam(required = true) orderId: Long): Option[OrderBean] =
  {
    orderService.getById(orderId)
  }


  @RequestMapping(value = Array("find-by-tea-session-id"), method = Array(RequestMethod.POST))
  def findByTeaSessionId(@RequestParam(required = true) teaSessionId: Long): List[OrderBean] =
  {
    orderService.findByTeaSessionId(teaSessionId)
  }


  @RequestMapping(value = Array("get-order-summary-by-tea-session-id"), method = Array(RequestMethod.POST))
  def getOrderSummaryByTeaSessionId(@RequestParam(required = true) teaSessionId: Long): Map[String,Any] =
  {
    orderService.getOrderSummaryByTeaSessionId(teaSessionId)
  }


  @RequestMapping(value = Array("delete-by-id"), method = Array(RequestMethod.POST))
  def deleteById(@RequestParam(required = true) orderId: Long): Map[String,Any] =
  {
    orderService.deleteById(orderId)
  }


}
