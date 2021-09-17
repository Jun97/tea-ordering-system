package com.convoy.dtd.tos.web.core.dao.impl

import com.convoy.dtd.johnston.domain.jpa.dao.AbstractGenericDao
import com.convoy.dtd.tos.web.api.entity.OrderBean
import com.convoy.dtd.tos.web.core.dao.OrderDao
import org.springframework.stereotype.Repository

@Repository
private[impl] class OrderDaoImpl extends AbstractGenericDao[OrderBean, Long] with OrderDao{


}
