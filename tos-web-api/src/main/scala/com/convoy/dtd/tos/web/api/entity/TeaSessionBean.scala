package com.convoy.dtd.tos.web.api.entity

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.GeneratedValue
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.GenerationType
import javax.persistence.Convert
import java.util.Date
import com.convoy.dtd.johnston.domain.api.convert.OptionLongConverter


@SerialVersionUID(1L)
@Entity
@Table(name="tea_session")
class TeaSessionBean extends Serializable with Equals
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="tea_session_id")
  var teaSessionId: Long = _

  @Column(name="description")
  var description: String = _

  @Column(name="is_public")
  var isPublic: Boolean = _

  @Column(name="password")
  var password: String = _

  @Column(name="treat_date")
  var treatDate: Date = _

  @Column(name="cut_off_date")
  var cutOffDate: Date = _

  @Column(name="user_id")
  var userId: Long = _

  override def canEqual(other:Any) = other.isInstanceOf[TeaSessionBean]

  override def hashCode = 41 * (41 + teaSessionId.intValue())

  override def equals(other:Any) = other match
  {
    case that: TeaSessionBean => this.teaSessionId == that.teaSessionId
    case _ => false
  }
}
