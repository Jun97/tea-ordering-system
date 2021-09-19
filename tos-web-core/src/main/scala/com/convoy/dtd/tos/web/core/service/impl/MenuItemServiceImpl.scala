package com.convoy.dtd.tos.web.core.service.impl


import com.convoy.dtd.tos.web.api.entity.{MenuItemBean, TeaSessionBean}
import com.convoy.dtd.tos.web.api.service.{MenuItemService}
import com.convoy.dtd.tos.web.core.dao.{MenuItemDao, TeaSessionDao}

import java.io.{ByteArrayOutputStream, IOException}
import java.net.URLConnection
import java.nio.file.{Files, Paths, StandardCopyOption}
import java.text.SimpleDateFormat
import java.util.Base64
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.inject.Inject


import org.apache.commons.io.FilenameUtils
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.util.UriComponentsBuilder

import scala.collection.JavaConverters._

@Service
private[impl] class MenuItemServiceImpl extends MenuItemService
{

  @Inject
  private var menuItemDao: MenuItemDao = _
  @Inject
  private var teaSessionDao: TeaSessionDao = _



  /* hashing */
  private val passwordEncoder: BCryptPasswordEncoder = new BCryptPasswordEncoder()

  private val IMAGE_FILE_DIRECTORY = "C:\\Users\\jiajunang\\Documents\\tos\\DATA\\db\\menu_item\\"

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")


  @Transactional
  override def createMenuItem(teaSessionId: Long, menuItemName: String, menuItemImagePath: Option[MultipartFile]): Map[String, Any] =
  {
    val toTeaSession = teaSessionDao.getById(teaSessionId)
    if(toTeaSession.isDefined)
    {
      val t = new MenuItemBean()
      t.menuItemName = menuItemName
      t.teaSessionMenuItem = toTeaSession.get
      menuItemDao.saveOrUpdate(t)

      if(menuItemImagePath.isDefined){
        addMenuItemImageByMultipart(t.menuItemId, menuItemImagePath.get, false)
      }

      val modifiedMenuItem: MenuItemBean = t.deepClone
      if(!isStringEmpty(modifiedMenuItem.menuItemImagePath)){
        modifiedMenuItem.menuItemImagePath = generateMenuItemImageUrl(modifiedMenuItem.menuItemImagePath)
      }


      Map(
        "error" -> false,
        "message" -> "Menu item created",
        "menuItem" -> modifiedMenuItem
      )
    } else {
      Map(
        "error" -> true,
        "message" -> "Invalid Tea Session reference"
      )
    }
  }


  @Transactional
  override def createMenuItemByBatch(teaSessionId: Long, menuList: List[MenuItemBean]): Map[String, Any] = //Not working
  {
    val toTeaSession = teaSessionDao.getById(teaSessionId)
    if(toTeaSession.isDefined)
    {
      menuList.foreach((menuItem: MenuItemBean) => {
        val t = new MenuItemBean()
        t.menuItemName = menuItem.menuItemName
        t.teaSessionMenuItem = toTeaSession.get
        menuItemDao.saveOrUpdate(menuItem)

        if(!isStringEmpty(menuItem.menuItemImagePath)){
          addMenuItemImageByBase64(t.menuItemId, menuItem.menuItemImagePath, false)
        }
      })

      Map(
        "error" -> false,
        "message" -> "Menu item(s) created"
      )
    } else {
      Map(
        "error" -> true,
        "message" -> "Invalid Tea Session reference"
      )
    }
  }


  @Transactional
  override def addMenuItemImageByMultipart(menuItemId: Long, menuItemImage: MultipartFile, isApiUpload: Boolean): Either[Boolean, Map[String, Any]] = {

    val extensionName: String = FilenameUtils.getExtension(menuItemImage.getOriginalFilename)
    val imageName: String = "menu_item_" + menuItemId + "." + extensionName
    val savePath = Paths.get(IMAGE_FILE_DIRECTORY + imageName)

    val to = menuItemDao.getById(menuItemId)
    if(to.isDefined){

      Files.copy(menuItemImage.getInputStream, savePath, StandardCopyOption.REPLACE_EXISTING)

      val t = to.get
      t.menuItemImagePath = imageName


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
          "message" -> "Menu item not exists"
        ))
      } else{
        Left(false)
      }
    }
  }


  @Transactional
  override def addMenuItemImageByBase64(menuItemId: Long, menuItemImage: String, isApiUpload: Boolean): Either[Boolean, Map[String, Any]] = {

    val to = menuItemDao.getById(menuItemId)

    if(to.isDefined)
    {

      try{
        val decodedImage: Array[Byte] = Base64.getDecoder.decode(menuItemImage)
        val is: InputStream = new ByteArrayInputStream(decodedImage)

        val mimeType: String = URLConnection.guessContentTypeFromStream(is)
        val fileExtension = mimeType.split("/")(1)

        val imageName: String = "menu_item_" + menuItemId + "." + fileExtension
        val savePath = Paths.get(IMAGE_FILE_DIRECTORY + imageName)

        Files.copy(is, savePath, StandardCopyOption.REPLACE_EXISTING)


        val t = to.get
        t.menuItemImagePath = imageName

        if(isApiUpload){
          Right(Map(
            "error" -> false,
            "message" -> "Image saved"
            ))
        } else {
          Left(true)
        }
      }
      catch {
        case e: IOException =>
          throw new RuntimeException(e)
      }
    } else
    {
      if(isApiUpload){
        Right(Map(
          "error" -> true,
          "message" -> "Menu item not exists"
        ))
      } else{
        Left(false)
      }

    }
  }


  @Transactional
  override def getMenuItemByTeaSessionId(teaSessionId: Long, password: Option[String]): Map[String, Any] = {

    val toTeaSession = teaSessionDao.getById(teaSessionId)
    if(toTeaSession.isDefined){
    val tTeaSession = toTeaSession.get

      if(checkVisibilityAndPassword(tTeaSession, password)) {
        val modifiedMenuItem: List[MenuItemBean] =
        menuItemDao.getMenuItemByTeaSessionId(teaSessionId).map( (menuItemBean: MenuItemBean) => {
          if(!isStringEmpty(menuItemBean.menuItemImagePath)){
            val tempMenuItemBean: MenuItemBean = menuItemBean.deepClone
            tempMenuItemBean.menuItemImagePath = generateMenuItemImageUrl(tempMenuItemBean.menuItemImagePath)
            tempMenuItemBean
          } else {
            menuItemBean
          }
        })

        Map(
          "error" -> false,
          "menuItem" -> modifiedMenuItem
        )
      }
      else
      {
        Map(
          "error" -> true,
          "message" -> "Please provide correct password for tea session menu"
        )
      }
    }
    else{
      Map(
        "error" -> true,
        "message" -> "Invalid tea session reference"
      )
    }
  }


  def checkVisibilityAndPassword(teaSession: TeaSessionBean, password: Option[String]): Boolean = {
    if (!teaSession.isPublic && password.isDefined){
      passwordEncoder.matches(password.get, teaSession.password)
    } else if (teaSession.isPublic){
      true
    } else {
      false
    }
  }


  override def getMenuItemImage(imageName: String): Array[Byte] = {

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
  override def updateMenuItem(menuItemId:Long, name: String, menuItemImagePath: Option[MultipartFile]): Map[String, Any] = {

    val to = menuItemDao.getById(menuItemId)

    if(to.isDefined)
    {
      val t = to.get
      t.menuItemName = name
      if(menuItemImagePath.isDefined){
        addMenuItemImageByMultipart(menuItemId, menuItemImagePath.get, false)
      }

      Map(
        "error" -> false,
        "message" -> "Update successfully"
      )
    }
    else
    {
      Map(
        "error" -> true,
        "message" -> "Menu item not found"
      )
    }
  }


  @Transactional
  override def deleteMenuItemById(menuItemId: Long): Map[String, Any] = {

    val to = menuItemDao.getById(menuItemId)

    if(to.isDefined)
    {
      val t = to.get
      deleteMenuItemImage(t.menuItemImagePath)
      menuItemDao.deleteById(menuItemId)

      Map(
        "error" -> false,
        "message" -> "Delete successfully"
      )
    } else
    {
      Map(
        "error" -> true,
        "message" -> "Menu item not found"
      )
    }
  }


  override def deleteMenuItemImage(menuItemImagePath: String): Boolean= {

    try{
    val deletePath = Paths.get(IMAGE_FILE_DIRECTORY + menuItemImagePath)
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


  def generateMenuItemImageUrl(imageName: String): String = {
    if(imageName != null) {
      UriComponentsBuilder.newInstance()
        .scheme("http").host("localhost").port(50001)
        .path("tos-rest/api/tea-session/menu-item/image/{imageName}")
        .buildAndExpand(imageName).toUriString
    } else {
      ""
    }
  }


}
