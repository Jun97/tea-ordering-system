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
  override def add(teaSessionId: Long, userId: Long): Map[String, Any] =
  {
    val toTeaSession = teaSessionDao.getById(teaSessionId)
    val toUserId = userDao.getById(userId)
    if(toTeaSession.isDefined && toUserId.isDefined)
    {
      val t: OrderBean = orderDao.checkExists(teaSessionId, userId).getOrElse[OrderBean]( {
        val t = new OrderBean()
        t.teaSessionOrder = toTeaSession.get
        t.userOrder = toUserId.get
        orderDao.saveOrUpdate(t)
        t
      })

      val modifiedT: OrderBean = t.deepClone
      modifiedT.userOrder = null

      Map(
        "error" -> false,
        "message" -> "Order created",
        "orderItem" -> modifiedT
      )
    } else {
      Map(
        "error" -> true,
        "message" -> "Invalid Tea Session or User reference"
      )
    }
  }


  @Transactional
  override def getById(orderId: Long): Option[OrderBean] = {

    val to = orderDao.getById(orderId)

//    if(to.isDefined) {
//      val t = to.get
//
//      Map(
//        "error" -> false,
//        "order" -> t
//      )
//    }
//    else
//    {
//      Map(
//        "error" -> true,
//        "message" -> "Invalid Order ID"
//      )
//    }
    to
  }


  @Transactional
  override def findByTeaSessionId(teaSessionId: Long): List[OrderBean] = {

    if(teaSessionDao.exists(teaSessionId)) {
//      Map(
//        "error" -> false,
//        "order" -> orderDao.findByTeaSessionId(teaSessionId)
//      )
      orderDao.findByTeaSessionId(teaSessionId)
    }
    else
    {
//      Map(
//        "error" -> true,
//        "message" -> "Invalid Tea Session ID"
//      )
      List()
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
  override def deleteById(orderId: Long): Map[String, Any] = {

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
