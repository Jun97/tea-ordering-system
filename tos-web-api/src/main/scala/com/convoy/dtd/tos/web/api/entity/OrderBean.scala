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
@Table(name="order")
class OrderBean extends Serializable with Equals
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="order_id")
  var orderId: Long = _

  @Column(name="tea_session_id")
  var teaSessionId: Long = _

  @Column(name="user_id")
  var userId: Long = _

  override def canEqual(other:Any) = other.isInstanceOf[OrderBean]

  override def hashCode = 41 * (41 + orderId.intValue())

  override def equals(other:Any) = other match
  {
    case that: OrderBean => this.orderId == that.orderId
    case _ => false
  }
}
