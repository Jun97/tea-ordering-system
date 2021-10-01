package com.convoy.dtd.tos.web.core.mvc

import com.convoy.dtd.tos.web.api.entity.UserBean
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

  @RequestMapping(value = Array("check-exists"))
  def checkUserExists(email:String):Map[String,Any] = userService.checkExists(email)

  @RequestMapping(value = Array("login-by-email"))
  def loginUserByEmail(email:String, password:String):Map[String,Any] = userService.loginByEmail(email, password)

  @RequestMapping(value = Array("find-all"))
  def getUserAll(): List[UserBean] = userService.findAll()

  @RequestMapping(value = Array("update-privilege"))
  def updatePrivilege(userId:Long, isEnabled:Boolean, isAdmin:Boolean):Map[String,Any] =
  {
    userService.updatePrivilege(userId, isEnabled, isAdmin)
  }

  @RequestMapping(value = Array("add"))
  def add(email:String, password:String, isEnabled:Boolean, isAdmin:Boolean):Map[String,Any] =
  {
    userService.add(email, password, isEnabled, isAdmin)
  }
}