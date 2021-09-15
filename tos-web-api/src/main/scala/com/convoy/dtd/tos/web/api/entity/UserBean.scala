package com.convoy.dtd.tos.web.api.entity

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.GeneratedValue
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.GenerationType
import javax.persistence.OneToMany
import javax.persistence.Convert
import java.util.Date
import java.util.Set

import com.convoy.dtd.johnston.domain.api.convert.OptionLongConverter


@SerialVersionUID(1L)
@Entity
@Table(name="person")
class UserBean extends Serializable with Equals
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="user_id")
  var userId: Long = _

  @Column(name="email")
  var email: String = _

  @Column(name="password")
  var password: String = _

  @Column(name="is_enabled", columnDefinition="BIT")
  var isEnabled: Boolean = _

  @Column(name="last_login_date")
  var lastLoginDate: Date = _

  @Column(name="is_admin", columnDefinition="BIT")
  var isAdmin: Boolean = _

  @OneToMany(mappedBy = "userTeaSession")
  var teaSessions: Set[TeaSessionBean] = _

  @OneToMany(mappedBy = "userOrder")
  var orders: Set[OrderBean] = _

  override def canEqual(other:Any) = other.isInstanceOf[UserBean]

  override def hashCode = 41 * (41 + userId.intValue())

  override def equals(other:Any) = other match
  {
    case that: UserBean => this.userId == that.userId
    case _ => false
  }
}
