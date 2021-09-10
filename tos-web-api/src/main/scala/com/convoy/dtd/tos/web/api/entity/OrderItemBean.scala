package com.convoy.dtd.tos.web.api.entity

import javax.persistence.{Column, Convert, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}
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

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  var orderOrderItem: OrderBean = _

  @ManyToOne
  @JoinColumn(name = "menu_item_id", nullable = false)
  var menuItemOrderItem: MenuItemBean = _

  override def canEqual(other:Any) = other.isInstanceOf[OrderItemBean]

  override def hashCode = 41 * (41 + orderItemId.intValue())

  override def equals(other:Any) = other match
  {
    case that: OrderItemBean => this.orderItemId == that.orderItemId
    case _ => false
  }
}
