package com.convoy.dtd.tos.web.api.entity

import javax.persistence.{Column, Convert, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, Table}
import com.convoy.dtd.johnston.domain.api.convert.OptionLongConverter

import java.util.Set


@SerialVersionUID(1L)
@Entity
@Table(name="order")
class OrderBean extends Serializable with Equals
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="order_id")
  var orderId: Long = _

  @ManyToOne
  @JoinColumn(name = "tea_session_id", nullable = false)
  var teaSessionOrder: TeaSessionBean = _

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  var userOrder: UserBean = _

  @OneToMany(mappedBy = "orderOrderItem")
  var orderItems: Set[OrderItemBean] = _

  override def canEqual(other:Any) = other.isInstanceOf[OrderBean]

  override def hashCode = 41 * (41 + orderId.intValue())

  override def equals(other:Any) = other match
  {
    case that: OrderBean => this.orderId == that.orderId
    case _ => false
  }
}
