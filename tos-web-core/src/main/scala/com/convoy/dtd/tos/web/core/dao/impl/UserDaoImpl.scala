package com.convoy.dtd.tos.web.core.dao.impl

import com.convoy.dtd.tos.web.core.dao.UserDao
import com.convoy.dtd.johnston.domain.jpa.dao.AbstractGenericDao
import com.convoy.dtd.tos.web.api.entity.{QUserBean, UserBean}
import org.springframework.stereotype.Repository
import com.querydsl.jpa.impl.JPAQueryFactory
import collection.JavaConverters._

@Repository
private[impl] class UserDaoImpl extends AbstractGenericDao[UserBean, Long] with UserDao
{
  override def getByEmail(email:String):Option[UserBean] =
  {
    val q = new JPAQueryFactory(entityManager)
    Option(q.selectFrom(QUserBean).where(QUserBean.email === (email)).fetchOne())
  }
}
