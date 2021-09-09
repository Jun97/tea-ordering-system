package com.convoy.dtd.tos.web.api.service

import com.convoy.dtd.tos.web.api.entity.TestBean

trait TestService 
{
  def findTestAll():List[TestBean]
  
  def getTestByTestId(testId:Long):Option[TestBean]
  def addTest(hello:String, world:Long):Unit
  def deleteTestByTestId(testId:Long):Unit
  def updateTestByTestId(testId:Long, hello:String, world:Long):Unit
  
}