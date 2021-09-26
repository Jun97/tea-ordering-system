package com.convoy.dtd.tos.web.core.service.impl

import com.convoy.dtd.tos.web.api.entity.OrderItemBean
import com.convoy.dtd.tos.web.api.service.OrderItemService
import com.convoy.dtd.tos.web.core.dao.{MenuItemDao, OrderDao, OrderItemDao}
import javax.inject.Inject
import org.springframework.boot.web.filter.OrderedCharacterEncodingFilter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder

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
  override def add(orderId: Long, menuItemId: Long, quantity: Long): Map[String, Any] =
  {
    val toOrder = orderDao.getById(orderId)
    val toMenuItem = menuItemDao.getById(menuItemId)
    if(toOrder.isDefined && toMenuItem.isDefined)
    {
      val t: OrderItemBean= orderItemDao.checkExists(orderId, menuItemId).getOrElse[OrderItemBean]( {
        val t = new OrderItemBean()
        t.orderOrderItem = toOrder.get
        t.menuItemOrderItem = toMenuItem.get
        t.quantity = quantity
        orderItemDao.saveOrUpdate(t)
        t
      })

      t.quantity = quantity

      val modifiedT: OrderItemBean = t.deepClone
      modifiedT.menuItemOrderItem.imagePath = generateMenuItemImageUrl(modifiedT.menuItemOrderItem.imagePath)

      Map(
        "error" -> false,
        "message" -> "Order Item created",
        "orderItem" -> modifiedT
      )
    } else {
      Map(
        "error" -> true,
        "message" -> "Invalid Order or Menu Item reference"
      )
    }
  }


  @Transactional
  override def findByOrderId(orderId: Long): List[OrderItemBean] = {

    val toOrder = orderDao.getById(orderId)

    if(toOrder.isDefined) {

//      Map(
//        "error" -> false,
//        "orderItem" -> orderItemDao.findByOrderId(orderId)
//      )
      val t = orderItemDao.findByOrderId(orderId)
      val modifiedT: List[OrderItemBean] = t.map( (orderItemBean: OrderItemBean) => {
        val modifiedBean: OrderItemBean =  orderItemBean.deepClone
        modifiedBean.menuItemOrderItem.imagePath = generateMenuItemImageUrl(modifiedBean.menuItemOrderItem.imagePath)
        modifiedBean
      })
      modifiedT
    }
    else
    {
      List()
    }
  }


  @Transactional
  override def updateById(orderItemId: Long, quantity: Long): Map[String, Any] = {

    val to = orderItemDao.getById(orderItemId)

    if(to.isDefined)
    {

      val t = to.get
      t.quantity = quantity

      val modifiedT: OrderItemBean = t.deepClone
      modifiedT.menuItemOrderItem.imagePath = generateMenuItemImageUrl(modifiedT.menuItemOrderItem.imagePath)

      Map(
        "error" -> false,
        "message" -> "Update successfully",
        "orderItem" -> modifiedT
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
  override def deleteById(orderItemId: Long): Map[String, Any] = {

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

  def generateMenuItemImageUrl(imageName: String): String = {
    if(imageName != null) {
      UriComponentsBuilder.newInstance()
        .scheme("http").host("localhost").port(50001)
        .path("tos-rest/api/tea-session/menu-item/image/{imageName}")
        .buildAndExpand(imageName).toUriString
    } else {
      ""
    }
  }


}
