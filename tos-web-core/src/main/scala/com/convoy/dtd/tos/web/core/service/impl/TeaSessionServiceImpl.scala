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
                                password: Option[String],
                                userId:Long,
                                teaSessionImagePath: Option[MultipartFile]): Map[String, Any] =
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
        val hashedPassword = passwordEncoder.encode(password.get)
        t.password = hashedPassword
      }
      t.userTeaSession = userTo.get
      teaSessionDao.saveOrUpdate(t)
      if(teaSessionImagePath.isDefined){
        addTeaSessionImageByMultipart(t.teaSessionId, teaSessionImagePath.get, false)
      }

      Map(
        "error" -> false,
        "message" -> "Tea session created",
        "teaSession" -> List(t)
      )
    } else {
      Map(
        "error" -> true,
        "message" -> "Malformed request, kindly check user referenced user ID and visibility/password combination"
      )
    }


  }


  @Transactional
  override def addTeaSessionImageByMultipart(teaSessionId: Long, teaSessionImage: MultipartFile, isApiUpload: Boolean): Either[Boolean, Map[String, Any]] = {

    val extensionName: String = FilenameUtils.getExtension(teaSessionImage.getOriginalFilename)
    val imageName: String = "tea_session_" + teaSessionId + "." + extensionName
    val savePath = Paths.get(IMAGE_FILE_DIRECTORY + imageName)

    val to = teaSessionDao.getById(teaSessionId)
    if(to.isDefined){

      Files.copy(teaSessionImage.getInputStream, savePath, StandardCopyOption.REPLACE_EXISTING)

      val t = to.get
      t.teaSessionImagePath = imageName

      if(isApiUpload){
      Right(Map(
        "error" -> false,
        "message" -> "Image saved"
        ))
      } else {
        Left(true)
      }

    } else {
      if(isApiUpload){
        Right(Map(
          "error" -> true,
          "message" -> "Tea session not exists"
        ))
      } else {
        Left(false)
      }
    }
  }

  @Transactional
  override def getTeaSessionById(teaSessionId: Long): Map[String, Any] = {

    val to = teaSessionDao.getById(teaSessionId)

    if(to.isDefined ) {
      val t = to.get

      val modifiedTeaSession: TeaSessionBean = t.deepClone
      modifiedTeaSession.teaSessionImagePath = generateTeaSessionImageUrl(modifiedTeaSession.teaSessionImagePath)

      Map(
        "error" -> false,
        "teaSession" -> List(modifiedTeaSession)
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
    teaSessionDao.getUpcomingTeaSession().map( (teaSessionBean: TeaSessionBean) => {
      val tempTeaSession: TeaSessionBean = teaSessionBean.deepClone //Leave Original Bean untouched
      tempTeaSession.teaSessionImagePath = generateTeaSessionImageUrl(teaSessionBean.teaSessionImagePath) //Convert to URL Link to get image
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
                                      name: Option[String],
                                      description: Option[String],
                                      treatDate: Option[String],
                                      cutOffDate: Option[String],
                                      teaSessionImagePath: Option[MultipartFile]): Map[String, Any] ={

    val to = teaSessionDao.getById(teaSessionId)
    val toUser = userDao.getById(userId)

    if(to.isDefined && toUser.isDefined)
    {
      val t = to.get
      val tUser = toUser.get
      if( tUser.isAdmin || t.userTeaSession.userId == tUser.userId )
      {

        if (name.isDefined){
          t.name = name.get
        }
        if (description.isDefined){
          t.description = description.get
        }
        if (treatDate.isDefined){
          t.treatDate = dateFormat.parse(treatDate.get)
        }
        if (cutOffDate.isDefined){
          t.treatDate = dateFormat.parse(cutOffDate.get)
        }

        if (teaSessionImagePath.isDefined){
          addTeaSessionImageByMultipart(t.teaSessionId, teaSessionImagePath.get, false)
        }

        Map(
          "error" -> false,
          "message" -> "Update successfully",
          "teaSession" -> List(t)
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
      if( tUser.isAdmin || t.userTeaSession.userId == tUser.userId ) //Admin or the person who created Tea Session
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
          "message" -> "Update successfully",
          "teaSession" -> List(t)
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
        deleteTeaSessionImage(t.teaSessionImagePath)
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


  override def deleteTeaSessionImage(teaSessionImagePath: String): Boolean= {

    try{
      val deletePath = Paths.get(IMAGE_FILE_DIRECTORY + teaSessionImagePath)
      Files.delete(deletePath)
      true
    }
    catch {
      case e: IOException =>
        throw new RuntimeException(e)
        false
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


  def checkVisibilityAndPassword(isPublic: Boolean, password: Option[String]): Boolean ={
    if (!isPublic && password.isDefined && !isStringEmpty(password.get)) { //If private and password is defined
      true
    } else if (isPublic && !password.isDefined )
    {
      true
    }
    else
    {
      false
    }
  }


}
