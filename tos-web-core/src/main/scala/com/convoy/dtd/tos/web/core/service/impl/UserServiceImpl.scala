package com.convoy.dtd.tos.web.core.service.impl

import java.util.Calendar

import org.springframework.stereotype.Service
import com.convoy.dtd.tos.web.api.service.UserService
import com.convoy.dtd.tos.web.core.dao.UserDao
import javax.inject.Inject
import com.convoy.dtd.tos.web.api.entity.UserBean
import javax.crypto.Cipher
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}
import org.apache.commons.codec.binary.Base64
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional

@Service
private[impl] class UserServiceImpl extends UserService
{
  /* hashing */
  private val passwordEncoder: BCryptPasswordEncoder = new BCryptPasswordEncoder();

  @Inject
  private var userDao:UserDao = _

  @Transactional
  override def checkUserExists(email: String): Map[String, Any]=
  {
    val to = userDao.getByEmail(email)
    if(to.isDefined)
    {
      Map(
        "error" -> true,
        "errorMessage" -> "The username has been used."
      )
    }
    else
    {
      Map()
    }
  }

  @Transactional
  override def loginUserByEmail(email: String, password: String): Map[String, Any] =
  {


    val to = userDao.getByEmail(email)
    if(to.isDefined)
    {
      val t = to.get

      if(t.isEnabled)
      {
        if(passwordEncoder.matches(password, t.password)) //Compare user input password with db hashed pwd
        {
          t.lastLoginDate = Calendar.getInstance().getTime
          Map("userId" -> t.userId, "email" -> t.email, "isEnabled" -> t.isEnabled, "isAdmin" -> t.isAdmin)
        }
        else
        {
          Map("errorMessage" -> "Incorrect password.")
        }
      }
      else
      {
        Map("errorMessage" -> "The account has been locked.")
      }
    }
    else
    {
      Map("errorMessage" -> "Account does not exist.")
    }
  }

  @Transactional
  override def getUserAll(): Map[String,Any] =
  {
    Map("users" -> userDao.findAllAsScala())
  }

  @Transactional
  override def updateUser(userId: Long, enable: Boolean, isAdmin: Boolean): Unit =
  {
    val to = userDao.getById(userId)
    if(to.isDefined)
    {
      val t = to.get
      t.isEnabled = enable
      t.isAdmin = isAdmin
    }
  }

  @Transactional
  override def createUser(email: String, password: String, is_Enabled: Boolean, isAdmin: Boolean): Unit =
  {
    val hashedPassword = passwordEncoder.encode(password)

    val u = new UserBean()
    u.email = email
    u.password = hashedPassword
    u.isEnabled = is_Enabled
    u.lastLoginDate = null
    u.isAdmin = isAdmin
    userDao.saveOrUpdate(u)
  }
}

