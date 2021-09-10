package com.convoy.dtd.tos.web.api.entity

import javax.persistence.{Column, Convert, Embeddable, EmbeddedId, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, MapsId, OneToMany, Table}
import com.convoy.dtd.johnston.domain.api.convert.OptionLongConverter
import java.util.Set

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

  @Column(name="menu_file_path")
  var menuFilePath: String = _

  @ManyToOne
  @JoinColumn(name = "tea_session_id", nullable = false)
  var teaSessionMenuItem: TeaSessionBean = _

  @OneToMany(mappedBy = "menuItemOrderItem")
  var orderItems: Set[OrderItemBean] = _

  override def canEqual(other:Any) = other.isInstanceOf[MenuItemBean]

  override def hashCode = 41 * (41 + menuItemId.intValue())

  override def equals(other:Any) = other match
  {
    case that: MenuItemBean => this.menuItemId == that.menuItemId
    case _ => false
  }
}
