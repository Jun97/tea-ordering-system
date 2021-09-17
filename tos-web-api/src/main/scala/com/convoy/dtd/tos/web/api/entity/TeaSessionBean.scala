package com.convoy.dtd.tos.web.api.entity

import javax.persistence.{CascadeType, Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, Table}
import java.util.Date
import java.util.List

import com.convoy.dtd.johnston.domain.api.convert.OptionLongConverter

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


@SerialVersionUID(1L)
@Entity
@Table(name="tea_session")
class TeaSessionBean extends Serializable with Equals
{

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="tea_session_id")
  var teaSessionId: Long = _

  @Column(name="name")
  var name: String = _

  @Column(name="description")
  var description: String = _

  @Column(name="is_public", columnDefinition="BIT")
  var isPublic: Boolean = _

  @Column(name="password", nullable = true)
  var password: String = _

  @Column(name="treat_date")
  var treatDate: Date = _

  @Column(name="cut_off_date")
  var cutOffDate: Date = _

  @Column(name="tea_session_image_path")
  var teaSessionImagePath: String = _

  @OneToMany(mappedBy = "teaSessionMenuItem", cascade = Array(CascadeType.REMOVE))
  var menuItems: List[MenuItemBean] = _

  @OneToMany(mappedBy = "teaSessionOrder", cascade = Array(CascadeType.REMOVE))
  var orders: List[OrderBean] = _

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  var userTeaSession: UserBean = _

  def deepClone: TeaSessionBean = try {
    val baos = new ByteArrayOutputStream
    val oos = new ObjectOutputStream(baos)
    oos.writeObject(this)
    val bais = new ByteArrayInputStream(baos.toByteArray)
    val ois = new ObjectInputStream(bais)
    ois.readObject.asInstanceOf[TeaSessionBean]
  } catch {
    case e: IOException =>
      throw e.getCause
    case e: ClassNotFoundException =>
      throw e.getException
  }

  override def canEqual(other:Any) = other.isInstanceOf[TeaSessionBean]

  override def hashCode = 41 * (41 + teaSessionId.intValue())

  override def equals(other:Any) = other match
  {
    case that: TeaSessionBean => this.teaSessionId == that.teaSessionId
    case _ => false
  }
}
