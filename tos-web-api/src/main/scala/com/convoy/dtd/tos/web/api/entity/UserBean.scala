package com.convoy.dtd.tos.web.api.entity

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, IOException, ObjectInputStream, ObjectOutputStream}

import javax.persistence.{Column, Convert, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToMany, Table}
import java.util.Date
import java.util.List

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

  @OneToMany(mappedBy = "userTeaSession", fetch = FetchType.LAZY)
  var teaSessions: List[TeaSessionBean] = _

  @OneToMany(mappedBy = "userOrder", fetch = FetchType.LAZY)
  var orders: List[OrderBean] = _


  def deepClone: UserBean = try {
    val baos = new ByteArrayOutputStream
    val oos = new ObjectOutputStream(baos)
    oos.writeObject(this)
    val bais = new ByteArrayInputStream(baos.toByteArray)
    val ois = new ObjectInputStream(bais)
    ois.readObject.asInstanceOf[UserBean]
  } catch {
    case e: IOException =>
      throw e.getCause
    case e: ClassNotFoundException =>
      throw e.getException
  }

  override def canEqual(other:Any) = other.isInstanceOf[UserBean]

  override def hashCode = 41 * (41 + userId.intValue())

  override def equals(other:Any) = other match
  {
    case that: UserBean => this.userId == that.userId
    case _ => false
  }
}
