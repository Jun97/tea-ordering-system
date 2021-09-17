package com.convoy.dtd.tos.web.core.service.impl

import java.io.{ByteArrayOutputStream, IOException}
import java.net.URLConnection
import java.nio.file.{Files, Paths, StandardCopyOption}
import java.text.SimpleDateFormat
import java.util.{Base64}

import com.convoy.dtd.tos.web.api.entity.{MenuItemBean, TeaSessionBean}
import com.convoy.dtd.tos.web.api.service.{MenuItemService, TeaSessionService}
import com.convoy.dtd.tos.web.core.dao.{MenuItemDao, TeaSessionDao}
import javax.inject.Inject

import java.io.ByteArrayInputStream
import java.io.InputStream


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
  override def createMenuItemByBatch(teaSessionId: Long, menuList: List[MenuItemBean]): Map[String, Any] =
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
          addMenuItemImage(t.menuItemId, menuItem.menuItemImagePath)
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
  override def addMenuItemImage(menuItemId: Long, teaSessionImage: String): Boolean = {

    val to = menuItemDao.getById(menuItemId)

    if(to.isDefined)
    {

      try{
      val decodedImage: Array[Byte] = Base64.getDecoder.decode(teaSessionImage)
      val is: InputStream = new ByteArrayInputStream(decodedImage)

      val mimeType: String = URLConnection.guessContentTypeFromStream(is)
      val fileExtension = mimeType.split("/")(1)

      val imageName: String = "menu_item_" + menuItemId + "." + fileExtension
      val savePath = Paths.get(IMAGE_FILE_DIRECTORY + imageName)

      Files.copy(is, savePath, StandardCopyOption.REPLACE_EXISTING)


      val t = to.get
      t.menuItemImagePath = imageName

      true
      }
      catch {
        case e: IOException =>
          throw new RuntimeException(e)
      }
    } else
    {
      false
    }
  }


  @Transactional
  override def getMenuItemByTeaSessionId(teaSessionId: Long, password: Option[String]): Map[String, Any] = {

    val to = teaSessionDao.getById(teaSessionId)
    val t = to.get

    if(to.isDefined && checkVisibilityAndPassword(t, password.get)) {


      val modifiedTeaSession: TeaSessionBean = t.deepClone
      modifiedTeaSession.teaSessionImagePath = generateTeaSessionImageUrl(t.teaSessionImagePath)

      modifiedTeaSession.menuItems.asScala.map( (menuItemBean: MenuItemBean) => {
        if(isStringEmpty(menuItemBean.menuItemImagePath)){
          menuItemBean.menuItemImagePath = generateMenuItemImageUrl(menuItemBean.menuItemImagePath)
          menuItemBean
        } else {
          menuItemBean
        }
      })


      Map(
        "error" -> false,
        "menuItem" -> modifiedTeaSession.menuItems
      )
    }
    else
    {
      Map(
        "error" -> true,
        "message" -> "Invalid tea session reference or password"
      )
    }
  }

  def checkVisibilityAndPassword(teaSession: TeaSessionBean, password: String): Boolean = {
    if (!teaSession.isPublic && passwordEncoder.matches(password, teaSession.password)){
      true
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
  override def updateMenuItem(menuItemId:Long, name: String): Map[String, Any] = {

    val to = menuItemDao.getById(menuItemId)

    if(to.isDefined)
    {
      val t = to.get
      t.menuItemName = name

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
