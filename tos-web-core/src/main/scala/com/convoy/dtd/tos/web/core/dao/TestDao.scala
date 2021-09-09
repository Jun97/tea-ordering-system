package com.convoy.dtd.tos.web.core.dao

import com.convoy.dtd.johnston.domain.api.dao.GenericDao
import com.convoy.dtd.tos.web.api.entity.TestBean

trait TestDao extends GenericDao[TestBean, Long] 
{
  def findByHello(hello:String):List[TestBean]
}