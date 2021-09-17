package com.convoy.dtd.tos.web.core.dao.impl

import com.convoy.dtd.johnston.domain.jpa.dao.AbstractGenericDao
import com.convoy.dtd.tos.web.api.entity.MenuItemBean
import com.convoy.dtd.tos.web.core.dao.MenuItemDao
import org.springframework.stereotype.Repository

@Repository
private[impl] class MenuItemDaoImpl extends AbstractGenericDao[MenuItemBean, Long] with MenuItemDao{


}
