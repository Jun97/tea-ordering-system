package com.convoy.dtd.tos.web.api.entity

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, IOException, ObjectInputStream, ObjectOutputStream}

import javax.persistence.{Column, Embeddable, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, Table}
import com.convoy.dtd.johnston.domain.api.convert.OptionLongConverter
import java.util.List

@SerialVersionUID(1L)
@Entity
@Embeddable
@Table(name="menu_item")
class MenuItemBean extends Serializable with Equals
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="menu_item_id")
  var menuItemId: Long = _

  @Column(name="menu_item_name")
  var menuItemName: String = _

  @Column(name="menu_item_image_path")
  var menuItemImagePath: String = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "tea_session_id", nullable = false)
  var teaSessionMenuItem: TeaSessionBean = _

  @OneToMany(mappedBy = "menuItemOrderItem", fetch = FetchType.LAZY)
  var orderItems: List[OrderItemBean] = _


  def deepClone: MenuItemBean = try {
    val baos = new ByteArrayOutputStream
    val oos = new ObjectOutputStream(baos)
    oos.writeObject(this)
    val bais = new ByteArrayInputStream(baos.toByteArray)
    val ois = new ObjectInputStream(bais)
    ois.readObject.asInstanceOf[MenuItemBean]
  } catch {
    case e: IOException =>
      throw e.getCause
    case e: ClassNotFoundException =>
      throw e.getException
  }

  override def canEqual(other:Any) = other.isInstanceOf[MenuItemBean]

  override def hashCode = 41 * (41 + menuItemId.intValue())

  override def equals(other:Any) = other match
  {
    case that: MenuItemBean => this.menuItemId == that.menuItemId
    case _ => false
  }

  @Override
  override def toString(): String = {
    return "name" + this.menuItemName + "base64" + this.menuItemImagePath
  }

}
