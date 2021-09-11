package com.convoy.dtd.tos.web.api.service

import com.convoy.dtd.tos.web.api.entity.UserBean

trait UserService
{
  def checkUserExists(email:String):Map[String,Any]
  def loginUserByEmail(email:String, password:String):Map[String,Any]
  def getUserAll():Map[String,Any]
  def updateUser(userId:Long, isEnabled:Boolean, isAdmin:Boolean): Map[String, Any]
  def createUser(email:String, password:String, isEnabled:Boolean, isAdmin:Boolean): Map[String, Any]
}
