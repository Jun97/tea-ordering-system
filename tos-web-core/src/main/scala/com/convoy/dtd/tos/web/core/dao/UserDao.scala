package com.convoy.dtd.tos.web.core.dao

import com.convoy.dtd.johnston.domain.api.dao.GenericDao
import com.convoy.dtd.tos.web.api.entity.UserBean

trait UserDao extends GenericDao[UserBean, Long]
{
  def getByEmail(username:String):Option[UserBean]
}
