package com.convoy.dtd.tos.web.core.mvc

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod
import com.convoy.dtd.tos.web.api.service.UserService
import javax.inject.Inject

@RestController
@RequestMapping(value = Array("/api/user"), method = Array(RequestMethod.POST))
private[mvc] class UserController
{
  @Inject
  private var userService:UserService = _

  @RequestMapping(value = Array("check-user-exists"))
  def checkUserExists(email:String):Map[String,Any] = userService.checkUserExists(email)

  @RequestMapping(value = Array("login"))
  def loginUserByEmail(email:String, password:String):Map[String,Any] = userService.loginUserByEmail(email, password)

  @RequestMapping(value = Array("get-user-all"))
  def getUserAll(): Map[String,Any] = userService.getUserAll()

  @RequestMapping(value = Array("update-privilege"))
  def updateUser(userId:Long, isEnabled:Boolean, isAdmin:Boolean):Map[String,Any] =
  {
    userService.updateUser(userId, isEnabled, isAdmin)
  }

  @RequestMapping(value = Array("register"))
  def createUser(email:String, password:String, isEnabled:Boolean, isAdmin:Boolean):Map[String,Any] =
  {
    userService.createUser(email, password, isEnabled, isAdmin)
  }
}