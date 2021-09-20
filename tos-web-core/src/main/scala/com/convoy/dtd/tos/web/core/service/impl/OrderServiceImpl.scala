package com.convoy.dtd.tos.web.core.service.impl


import com.convoy.dtd.tos.web.api.entity.OrderBean
import com.convoy.dtd.tos.web.api.service.OrderService
import com.convoy.dtd.tos.web.core.dao.{OrderDao, TeaSessionDao, UserDao}
import javax.inject.Inject
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
private[impl] class OrderServiceImpl extends OrderService
{

  @Inject
  private var orderDao: OrderDao = _
  @Inject
  private var userDao: UserDao = _
  @Inject
  private var teaSessionDao: TeaSessionDao = _



  @Transactional
  override def createOrder(teaSessionId: Long, userId: Long): Map[String, Any] =
  {
    val orderExists: Option[OrderBean] = orderDao.checkOrderExists(teaSessionId, userId)
    if(!orderExists.isDefined){
      val toTeaSession = teaSessionDao.getById(teaSessionId)
      val toUser = userDao.getById(userId)

      if(toTeaSession.isDefined && toUser.isDefined)
      {
        val t = new OrderBean()
        t.teaSessionOrder = toTeaSession.get
        t.userOrder = toUser.get
        orderDao.saveOrUpdate(t)

        val modifiedOrder: OrderBean = t.deepClone //Prevent user info leaked by creating order
        modifiedOrder.userOrder = null

        Map(
          "error" -> false,
          "message" -> "Order created",
          "order" -> modifiedOrder
        )
      } else {
        Map(
          "error" -> true,
          "message" -> "Invalid User/Tea Session Reference"
        )
      }
    }else {
      Map(
        "error" -> true,
        "message" -> "Order already exists",
        "order" -> orderExists.get
      )
    }
  }


  @Transactional
  override def getOrderById(orderId: Long): Map[String, Any] = {

    val to = orderDao.getById(orderId)

    if(to.isDefined) {
      val t = to.get

      Map(
        "error" -> false,
        "order" -> t
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
  override def getOrderSummaryByTeaSessionId(teaSessionId: Long): Map[String, Any] = {

    val toTeaSession = teaSessionDao.getById(teaSessionId)

    if(toTeaSession.isDefined) {
      val tTeaSession = toTeaSession.get

      Map(
        "error" -> false,
        "orderSummary" -> orderDao.getOrderSummaryByTeaSessionId(teaSessionId)
      )
    }
    else
    {
      Map(
        "error" -> true,
        "message" -> "Invalid Tea Session ID"
      )
    }
  }


  @Transactional
  override def deleteOrderById(orderId: Long): Map[String, Any] = {

    if(orderDao.exists(orderId))
    {

      orderDao.deleteById(orderId)

      Map(
        "error" -> false,
        "message" -> "Delete successfully"
      )
    } else
    {
      Map(
        "error" -> true,
        "message" -> "Invalid Order ID"
      )
    }
  }


  def isStringEmpty(string: String): Boolean = string == null || string.trim.isEmpty


}
