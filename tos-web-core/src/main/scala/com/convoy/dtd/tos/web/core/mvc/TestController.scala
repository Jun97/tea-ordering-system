package com.convoy.dtd.tos.web.core.mvc

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod
import com.convoy.dtd.tos.web.api.service.TestService
import javax.inject.Inject


@RestController
@RequestMapping(value = Array("/rest/test"), method = Array(RequestMethod.POST))
private[mvc] class TestController 
{
  @Inject
  private var testService:TestService = _
  
  @RequestMapping(value = Array("find-test-all"))
  def findTestAll() = testService.findTestAll()
  
  @RequestMapping(value = Array("add-test"))
  def addTest(hello:String, world:Long):Map[String,Object] = {
   testService.addTest(hello, world)
   return Map()
  }
  
  @RequestMapping(value = Array("delete-test-by-test-id"))
  def deleteTestByTestId(testId:Long):Map[String,Object] = {
   testService.deleteTestByTestId(testId)
   return Map()
  }
  
  @RequestMapping(value = Array("update-test-by-test-id"))
  def updateTestByTestId(testId:Long, hello:String, world:Long):Map[String,Object] = {
   testService.updateTestByTestId(testId, hello, world)
   return Map()
  }
  
  @RequestMapping(value = Array("get-test-by-test-id"))
  def getTestByTestId(testId:Long) = testService.getTestByTestId(testId)
  
}