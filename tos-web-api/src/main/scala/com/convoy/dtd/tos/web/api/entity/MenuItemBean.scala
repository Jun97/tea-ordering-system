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

  @Column(name="tea_session_id")
  var teaSessionId: Long = _

  override def canEqual(other:Any) = other.isInstanceOf[MenuItemBean]

  override def hashCode = 41 * (41 + menuItemId.intValue())

  override def equals(other:Any) = other match
  {
    case that: MenuItemBean => this.menuItemId == that.menuItemId
    case _ => false
  }
}
