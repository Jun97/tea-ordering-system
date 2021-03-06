package com.convoy.dtd.tos.web.core.service.impl

import java.util.Calendar

import org.springframework.stereotype.Service
import com.convoy.dtd.tos.web.api.service.UserService
import com.convoy.dtd.tos.web.core.dao.UserDao
import javax.inject.Inject
import com.convoy.dtd.tos.web.api.entity.UserBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional

@Service
private[impl] class UserServiceImpl extends UserService
{
  /* hashing */
  private val passwordEncoder: BCryptPasswordEncoder = new BCryptPasswordEncoder()

  @Inject
  private var userDao:UserDao = _

  @Transactional
  override def checkExists(email: String): Map[String, Any]=
  {
    val to = userDao.getByEmail(email)
    if(!to.isDefined)
    {
      Map(
        "error" -> false,
        "message" -> "Email has not been used"
      )
    }
    else
    {
      Map(
        "error" -> true,
        "message" -> "Email already exists"
      )
    }
  }

  @Transactional
  override def loginByEmail(email: String, password: String): Map[String, Any] =
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
          Map(
            "error"-> false,
            "message" -> "Login successful.",
            "user" -> t
          )
        }
        else
        {
          Map(
            "error"-> true,
            "message" -> "Incorrect password."
          )
        }
      }
      else
      {
        Map(
          "error"-> true,
          "message" -> "The account has been locked."
        )
      }
    }
    else
    {
      Map(
        "error"-> true,
        "message" -> "Account does not exist."
      )
    }
  }

  @Transactional
  override def findAll(): List[UserBean] =
  {
    //Map("user" -> userDao.findAllAsScala())
    userDao.findAllAsScala()
  }


  @Transactional
  override def add(email: String, password: String, isEnabled: Boolean, isAdmin: Boolean): Map[String, Any] =
  {
    val hashedPassword = passwordEncoder.encode(password)
    val to = userDao.getByEmail(email)
    val t = new UserBean()

    if(!to.isDefined)
    {
      t.email = email
      t.password = hashedPassword
      t.isEnabled = isEnabled
      t.lastLoginDate = null
      t.isAdmin = isAdmin
      userDao.saveOrUpdate(t)
      Map(
        "error" -> false,
        "message" -> "User details registered",
        "user" -> t
      )
    }
    else
    {
      Map(
        "error" -> true,
        "message" -> "User already exists"
      )
    }

  }


  @Transactional
  override def updatePrivilege(userId: Long, enable: Boolean, isAdmin: Boolean): Map[String, Any] =
  {
    val to = userDao.getById(userId)
    if(to.isDefined)
    {
      val t = to.get
      t.isEnabled = enable
      t.isAdmin = isAdmin

      Map(
        "error" -> false,
        "message" -> "Privilege changed",
        "user" -> t
      )
    } else {
      Map(
        "error" -> true,
        "message" -> "Request malformed"
      )
    }
  }


}

