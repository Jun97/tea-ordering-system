package com.convoy.dtd.tos.web.api.entity

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, IOException, ObjectInputStream, ObjectOutputStream}

import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, Table}
import com.convoy.dtd.johnston.domain.api.convert.OptionLongConverter
import java.util.List


@SerialVersionUID(1L)
@Entity
@Table(name="`order`")
class OrderBean extends Serializable with Equals
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="order_id")
  var orderId: Long = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "tea_session_id", nullable = false)
  var teaSessionOrder: TeaSessionBean = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  var userOrder: UserBean = _

  @OneToMany(mappedBy = "orderOrderItem", cascade = Array(CascadeType.REMOVE), fetch = FetchType.LAZY)
  var orderItems: List[OrderItemBean] = _


  def deepClone: OrderBean = try {
    val baos = new ByteArrayOutputStream
    val oos = new ObjectOutputStream(baos)
    oos.writeObject(this)
    val bais = new ByteArrayInputStream(baos.toByteArray)
    val ois = new ObjectInputStream(bais)
    ois.readObject.asInstanceOf[OrderBean]
  } catch {
    case e: IOException =>
      throw e.getCause
    case e: ClassNotFoundException =>
      throw e.getException
  }

  override def canEqual(other:Any) = other.isInstanceOf[OrderBean]

  override def hashCode = 41 * (41 + orderId.intValue())

  override def equals(other:Any) = other match
  {
    case that: OrderBean => this.orderId == that.orderId
    case _ => false
  }
}
