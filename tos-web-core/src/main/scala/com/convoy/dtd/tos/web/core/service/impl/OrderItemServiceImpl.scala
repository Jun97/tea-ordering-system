package com.convoy.dtd.tos.web.core.service.impl

import com.convoy.dtd.tos.web.api.entity.{OrderBean, OrderItemBean}
import com.convoy.dtd.tos.web.api.service.OrderItemService
import com.convoy.dtd.tos.web.core.dao.{MenuItemDao, OrderDao, OrderItemDao}
import javax.inject.Inject
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
private[impl] class OrderItemServiceImpl extends OrderItemService
{

  @Inject
  private var orderItemDao: OrderItemDao = _
  @Inject
  private var orderDao: OrderDao = _
  @Inject
  private var menuItemDao: MenuItemDao = _



  @Transactional
  override def createOrderItem(orderId: Long, menuItemId: Long, quantity: Long): Map[String, Any] =
  {
    val toOrder = orderDao.getById(orderId)
    val toMenuItem = menuItemDao.getById(menuItemId)
    if(toOrder.isDefined && toMenuItem.isDefined)
    {
      val t = new OrderItemBean()
      t.orderOrderItem = toOrder.get
      t.menuItemOrderItem = toMenuItem.get
      t.quantity = quantity
      orderItemDao.saveOrUpdate(t)

      Map(
        "error" -> false,
        "message" -> "Order created",
        "orderItem" -> t
      )
    } else {
      Map(
        "error" -> true,
        "message" -> "Invalid Order or Menu Item reference"
      )
    }
  }


  @Transactional
  override def getOrderItemByOrderId(orderId: Long): Map[String, Any] = {

    val toOrder = orderDao.getById(orderId)

    if(toOrder.isDefined) {

      Map(
        "error" -> false,
        "orderItem" -> orderItemDao.getOrderItemByOrderId(orderId)
      )
    }
    else
    {
      Map(
        "error" -> true,
        "message" -> "Invalid Order ID"
      )
    }
  }


  @Transactional
  override def updateOrderItemById(orderItemId: Long, quantity: Long): Map[String, Any] = {

    val to = orderItemDao.getById(orderItemId)

    if(to.isDefined)
    {

      val t = to.get
      t.quantity = quantity

      Map(
        "error" -> false,
        "message" -> "Update successfully",
        "orderItem" -> t
      )
    } else
    {
      Map(
        "error" -> true,
        "message" -> "Invalid Order Item ID"
      )
    }
  }


  @Transactional
  override def deleteOrderItemById(orderItemId: Long): Map[String, Any] = {

    val to = orderItemDao.getById(orderItemId)

    if(to.isDefined)
    {

      orderItemDao.deleteById(orderItemId)

      Map(
        "error" -> false,
        "message" -> "Delete successfully"
      )
    } else
    {
      Map(
        "error" -> true,
        "message" -> "Invalid Order Item ID"
      )
    }
  }


  def isStringEmpty(string: String): Boolean = string == null || string.trim.isEmpty


}
