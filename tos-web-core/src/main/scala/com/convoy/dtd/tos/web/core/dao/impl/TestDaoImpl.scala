package com.convoy.dtd.tos.web.core.dao.impl

import com.convoy.dtd.tos.web.core.dao.TestDao
import com.convoy.dtd.johnston.domain.jpa.dao.AbstractGenericDao
import com.convoy.dtd.tos.web.api.entity.TestBean
import org.springframework.stereotype.Repository
import com.querydsl.jpa.impl.JPAQueryFactory
import com.convoy.dtd.tos.web.api.entity.QTestBean
import collection.JavaConverters._

@Repository
private[impl] class TestDaoImpl extends AbstractGenericDao[TestBean, Long] with TestDao
{
  override def findByHello(hello:String):List[TestBean] =
  {
    val q = new JPAQueryFactory(entityManager)
    return q.selectFrom(QTestBean).where(QTestBean.hello === hello).fetch().asScala.toList
  }
}