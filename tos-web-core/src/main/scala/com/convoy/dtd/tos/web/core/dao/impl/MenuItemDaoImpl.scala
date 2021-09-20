package com.convoy.dtd.tos.web.core.dao.impl

import com.convoy.dtd.johnston.domain.jpa.dao.AbstractGenericDao
import com.convoy.dtd.tos.web.api.entity.{QMenuItemBean, MenuItemBean}
import com.convoy.dtd.tos.web.core.dao.MenuItemDao
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import collection.JavaConverters._
@Repository
private[impl] class MenuItemDaoImpl extends AbstractGenericDao[MenuItemBean, Long] with MenuItemDao{

  override def getMenuItemByTeaSessionId(teaSessionId:Long):List[MenuItemBean] =
  {
    val q = new JPAQueryFactory(entityManager)
    q.selectFrom(QMenuItemBean).where(QMenuItemBean.teaSessionMenuItem.teaSessionId === (teaSessionId)).fetch().asScala.toList
  }
}
