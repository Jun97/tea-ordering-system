package com.convoy.dtd.tos.web.core.service.impl

import org.springframework.stereotype.Service
import com.convoy.dtd.tos.web.api.service.TestService
import com.convoy.dtd.tos.web.core.dao.TestDao
import javax.inject.Inject
import com.convoy.dtd.tos.web.api.entity.TestBean
import org.springframework.transaction.annotation.Transactional



@Service
private[impl] class TestServiceImpl extends TestService
{
  @Inject
  private var testDao:TestDao = _
  
  @Transactional(readOnly=true)
  override def findTestAll():List[TestBean] = testDao.findAllAsScala()
  
  @Transactional(readOnly=true)
  override def getTestByTestId(testId:Long):Option[TestBean] = testDao.getById(testId)
  
  @Transactional
  override def addTest(hello:String, world:Long):Unit =
  {
    val t = new TestBean()
    t.hello = hello
    t.world = world
    testDao.saveOrUpdate(t)
  }
  
  @Transactional
  override def deleteTestByTestId(testId:Long):Unit = testDao.deleteById(testId)
  
  @Transactional
  override def updateTestByTestId(testId:Long, hello:String, world:Long):Unit =
  {
    val to = testDao.getById(testId)
    if(to.isDefined)
    {
      val t = to.get
      t.hello = hello
      t.world = world
    }
  }
  
}