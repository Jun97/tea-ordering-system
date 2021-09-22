package com.convoy.dtd.tos.web.core.dao

import com.convoy.dtd.johnston.domain.api.dao.GenericDao
import com.convoy.dtd.tos.web.api.entity.{MenuItemBean}

trait MenuItemDao extends GenericDao[MenuItemBean, Long]{
  def findByTeaSessionId(teaSessionId:Long):List[MenuItemBean]
}
