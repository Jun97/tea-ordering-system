package com.convoy.dtd.tos.web.core.dao.impl

import com.convoy.dtd.johnston.domain.jpa.dao.AbstractGenericDao
import com.convoy.dtd.tos.web.api.entity.{QTeaSessionBean, TeaSessionBean}
import com.convoy.dtd.tos.web.core.dao.TeaSessionDao
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.Calendar

import collection.JavaConverters._

@Repository
private[impl] class TeaSessionDaoImpl extends AbstractGenericDao[TeaSessionBean, Long] with TeaSessionDao{

  override def findUpcoming(): List[TeaSessionBean] =
  {
    //.where( QTeaSessionBean.cutOffDate > Calendar.getInstance().getTime )
    val q = new JPAQueryFactory((entityManager))
    q.selectFrom(QTeaSessionBean)
      .orderBy(QTeaSessionBean.treatDate.asc)
      .fetch.asScala.toList
  }


}
