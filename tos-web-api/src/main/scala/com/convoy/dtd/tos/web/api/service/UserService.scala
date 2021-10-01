package com.convoy.dtd.tos.web.api.service

import com.convoy.dtd.tos.web.api.entity.UserBean

trait UserService
{
  def checkExists(email:String):Map[String,Any]
  def loginByEmail(email:String, password:String):Map[String,Any]
  def findAll():List[UserBean]
  def updatePrivilege(userId:Long, isEnabled:Boolean, isAdmin:Boolean): Map[String, Any]
  def add(email:String, password:String, isEnabled:Boolean, isAdmin:Boolean): Map[String, Any]
}
