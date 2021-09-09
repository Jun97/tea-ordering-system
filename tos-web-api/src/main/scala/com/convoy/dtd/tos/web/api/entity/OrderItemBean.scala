package com.convoy.dtd.tos.web.api.entity

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.GeneratedValue
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.GenerationType
import javax.persistence.Convert
import com.convoy.dtd.johnston.domain.api.convert.OptionLongConverter


@SerialVersionUID(1L)
@Entity
@Table(name="order_item")
class OrderItemBean extends Serializable with Equals
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="order_item_id")
  var orderItemId: Long = _

  @Column(name="quantity")
  var quantity: Long = _

  @Column(name="menu_item_id")
  var menuItemId: Long = _

  @Column(name="order_id")
  var orderId: Long = _

  override def canEqual(other:Any) = other.isInstanceOf[OrderItemBean]

  override def hashCode = 41 * (41 + orderItemId.intValue())

  override def equals(other:Any) = other match
  {
    case that: OrderItemBean => this.orderItemId == that.orderItemId
    case _ => false
  }
}
