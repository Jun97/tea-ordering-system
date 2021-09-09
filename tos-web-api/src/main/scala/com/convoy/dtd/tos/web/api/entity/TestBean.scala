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
@Table(name="TEST")
class TestBean extends Serializable with Equals 
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TEST_ID")
	var testId: Long = _
	
	@Column(name="HELLO")
	var hello: String = _
	
	@Column(name="WORLD")
	var world: Long = _
	
	override def canEqual(other:Any) = other.isInstanceOf[TestBean]
  
  override def hashCode = 41 * (41 + testId.intValue())
  
  override def equals(other:Any) = other match
  {
    case that: TestBean => this.testId == that.testId
    case _ => false
  }
}