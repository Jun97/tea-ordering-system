package com.convoy.dtd.tos.web.core.service.impl

import java.io.ByteArrayOutputStream
import java.nio.file.{Files, Paths, StandardCopyOption}
import java.io.IOException
import java.text.SimpleDateFormat

import com.convoy.dtd.tos.web.api.entity.{TeaSessionBean, UserBean}
import com.convoy.dtd.tos.web.api.service.TeaSessionService
import com.convoy.dtd.tos.web.core.dao.{TeaSessionDao, UserDao}
import javax.inject.Inject
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.apache.commons.io.FilenameUtils
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

@Service
private[impl] class TeaSessionServiceImpl extends TeaSessionService
{

  @Inject
  private var teaSessionDao: TeaSessionDao = _
  @Inject
  private var userDao: UserDao = _


  /* hashing */
  private val passwordEncoder: BCryptPasswordEncoder = new BCryptPasswordEncoder()

  private val IMAGE_FILE_DIRECTORY = "C:\\Users\\jiajunang\\Documents\\tos\\DATA\\db\\tea_session\\"

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")

  @Transactional
  override def createTeaSession(name: String,
                                description: String,
                                treatDate: String,
                                cutOffDate: String,
                                isPublic: Boolean,
                                password: String,
                                userId:Long): Map[String, Any] =
  {
    val userTo = userDao.getById(userId)
    if(userTo.isDefined && checkVisibilityAndPassword(isPublic, password))
    {

      val t = new TeaSessionBean()
      t.name = name
      t.description = description
      t.treatDate = dateFormat.parse(treatDate)
      t.cutOffDate = dateFormat.parse(cutOffDate)
      t.isPublic = isPublic
      if (!isPublic) {
        val hashedPassword = passwordEncoder.encode(password)
        t.password = hashedPassword
      }
      t.userTeaSession = userTo.get
      teaSessionDao.saveOrUpdate(t)

      Map(
        "error" -> false,
        "message" -> "Tea session created"
      )
    } else {
      Map(
        "error" -> true,
        "message" -> "Malformed request, kindly check user referenced user ID and visibility/password combination"
      )
    }


  }


  @Transactional
  override def addTeaSessionImage(teaSessionId: Long, teaSessionImage: MultipartFile): Map[String, Any] = {

    val extensionName: String = FilenameUtils.getExtension(teaSessionImage.getOriginalFilename)
    val imageName: String = "tea_session_" + teaSessionId + "." + extensionName
    val savePath = Paths.get(IMAGE_FILE_DIRECTORY + imageName)

    val to = teaSessionDao.getById(teaSessionId)
    if(to.isDefined){

      Files.copy(teaSessionImage.getInputStream, savePath, StandardCopyOption.REPLACE_EXISTING)

      val t = to.get
      t.teaSessionImagePath = imageName

      Map(
        "error" -> false,
        "message" -> "Image saved"
      )

    } else {
      Map(
        "error" -> true,
        "message" -> "Tea session not exists"
      )
    }
  }

  @Transactional
  override def getTeaSessionById(teaSessionId: Long): Map[String, Any] = {

    val to = teaSessionDao.getById(teaSessionId)


    if(to.isDefined ) {
      val t = to.get

      val copiedTeaSession: TeaSessionBean = t.deepClone
      copiedTeaSession.teaSessionImagePath = generateTeaSessionImageUrl(t.teaSessionImagePath)

      copiedTeaSession.userTeaSession = new UserBean()
      copiedTeaSession.userTeaSession.userId = t.userTeaSession.userId

      Map(
        "error" -> false,
        "teaSession" -> List(copiedTeaSession)
      )
    }
    else
    {
      Map(
        "error" -> true,
        "message" -> "Tea session not found"
      )
    }
  }


  @Transactional
  override def getTeaSessionUpcoming(): Map[String, Any] = {

    val modifiedTeaSessions:List[TeaSessionBean] =
    teaSessionDao.getUpcomingTeaSession()
      .map( (teaSessionBean: TeaSessionBean) => {
      val tempTeaSession: TeaSessionBean = teaSessionBean.deepClone

      tempTeaSession.teaSessionImagePath = generateTeaSessionImageUrl(teaSessionBean.teaSessionImagePath) //Convert to URL Link to get image

      tempTeaSession.userTeaSession = new UserBean()
      tempTeaSession.userTeaSession.userId = teaSessionBean.userTeaSession.userId

      tempTeaSession.menuItems = null

      tempTeaSession
    })
    Map(
      "error" -> false,
      "teaSession" -> modifiedTeaSessions
    )
  }


  override def getTeaSessionImage(imageName: String): Array[Byte] = {

    try { // Retrieve image from the classpath.
      val inputStream = Paths.get(IMAGE_FILE_DIRECTORY + imageName)
      // Create a byte array output stream.
      val byteArrayOutput = new ByteArrayOutputStream()
      // Write to output stream
      Files.copy(inputStream, byteArrayOutput)
      byteArrayOutput.toByteArray
    } catch {
      case e: IOException =>
        throw new RuntimeException(e)
    }

  }


  @Transactional
  override def updateTeaSessionDetail(teaSessionId: Long,
                                      userId:Long,
                                      name: String,
                                      description: String,
                                      treatDate: String,
                                      cutOffDate: String): Map[String, Any] = {

    val to = teaSessionDao.getById(teaSessionId)
    val toUser = userDao.getById(userId)

    if(to.isDefined && toUser.isDefined)
    {
      val t = to.get
      val tUser = toUser.get
      if( tUser.isAdmin || t.userTeaSession.userId == tUser.userId )
      {

        if (!isStringEmpty(name)){
          t.name = name
        }
        if (!isStringEmpty(description)){
          t.description = description
        }
        if (!isStringEmpty(treatDate)){
          t.treatDate = dateFormat.parse(treatDate)
        }
        if (!isStringEmpty(cutOffDate)){
          t.treatDate = dateFormat.parse(cutOffDate)
        }

        Map(
          "error" -> false,
          "message" -> "Update successfully"
        )
      } else
      {
        Map(
          "error" -> true,
          "message" -> "Malformed request or wrong user privilege"
        )
      }
    }
    else
    {
      Map(
        "error" -> true,
        "message" -> "Tea session not found"
      )
    }
  }


  @Transactional
  override def updateTeaSessionPrivacy( teaSessionId: Long,
                                        userId:Long,
                                        isPublic: Boolean,
                                        password: String): Map[String, Any] = {

    val to = teaSessionDao.getById(teaSessionId)
    val toUser = userDao.getById(userId)

    if(to.isDefined && toUser.isDefined)
    {
      val t = to.get
      val tUser = toUser.get
      if( tUser.isAdmin || t.userTeaSession.userId == tUser.userId )
      {

        t.isPublic = isPublic
        if (!isPublic) {
          val hashedPassword = passwordEncoder.encode(password)
          t.password = hashedPassword
        } else if (isPublic) {
          t.password = null
        }

        Map(
          "error" -> false,
          "message" -> "Update successfully"
        )
      } else
      {
        Map(
          "error" -> true,
          "message" -> "Malformed request or wrong user privilege"
        )
      }
    }
    else
    {
      Map(
        "error" -> true,
        "message" -> "Tea session not found"
      )
    }
  }


  @Transactional
  override def deleteTeaSessionById(teaSessionId: Long, userId: Long): Map[String, Any] = {

    val to = teaSessionDao.getById(teaSessionId)
    val toUser = userDao.getById(userId)

    if(to.isDefined && toUser.isDefined)
    {
      val t = to.get
      val tUser = toUser.get
      if(tUser.isAdmin || t.userTeaSession.userId == tUser.userId)
      {
        teaSessionDao.deleteById(teaSessionId)

        Map(
          "error" -> false,
          "message" -> "Delete successfully"
        )
      }
      else
      {
        Map(
          "error" -> true,
          "message" -> "User do not have privilege to delete other user tea session"
        )
      }
    } else
    {
      Map(
        "error" -> true,
        "message" -> "Tea session not found"
      )
    }
  }


  def isStringEmpty(string: String): Boolean = string == null || string.trim.isEmpty


  def generateTeaSessionImageUrl(imageName: String): String = {
    if(imageName != null) {
      UriComponentsBuilder.newInstance()
        .scheme("http").host("localhost").port(50001)
        .path("tos-rest/api/tea-session/image/{imageName}")
        .buildAndExpand(imageName).toUriString
    } else {
      ""
    }
  }


  def checkVisibilityAndPassword(isPublic: Boolean, password: String): Boolean ={
    if (!isPublic && "".equals(password) ) {
      false
    } else if (isPublic && (!"".equals(password)) )
    {
      false
    }
    else
    {
      true
    }
  }


}
