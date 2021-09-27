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
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager
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
  override def add(teaSessionId: Long, name: String, imagePath: Option[MultipartFile]): Map[String, Any] =
  {
    val toTeaSession = teaSessionDao.getById(teaSessionId)
    if(toTeaSession.isDefined)
    {
      val t = new MenuItemBean()
      t.name = name
      t.teaSessionMenuItem = toTeaSession.get
      menuItemDao.saveOrUpdate(t)

      if(imagePath.isDefined){
        addImageByMultipart(t.menuItemId, imagePath.get, false)
      }

      val modifiedMenuItem: MenuItemBean = t.deepClone
      if(!isStringEmpty(modifiedMenuItem.imagePath)){
        modifiedMenuItem.imagePath = generateImageUrl(modifiedMenuItem.imagePath)
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
  override def addByBatch(teaSessionId: Long, menuList: List[MenuItemBean]): Map[String, Any] = //Not working
  {
    val toTeaSession = teaSessionDao.getById(teaSessionId)
    if(toTeaSession.isDefined)
    {
      menuList.foreach((menuItem: MenuItemBean) => {
        val t = new MenuItemBean()
        t.name = menuItem.name
        t.teaSessionMenuItem = toTeaSession.get
        menuItemDao.saveOrUpdate(menuItem)

        if(!isStringEmpty(menuItem.imagePath)){
          addImageByBase64(t.menuItemId, menuItem.imagePath, false)
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
  override def addImageByMultipart(menuItemId: Long, menuItemImage: MultipartFile, isApiUpload: Boolean): Either[Boolean, Map[String, Any]] = {

    val extensionName: String = FilenameUtils.getExtension(menuItemImage.getOriginalFilename)
    val imageName: String = "menu_item_" + menuItemId + "." + extensionName
    val savePath = Paths.get(IMAGE_FILE_DIRECTORY + imageName)

    val to = menuItemDao.getById(menuItemId)
    if(to.isDefined){

      Files.copy(menuItemImage.getInputStream, savePath, StandardCopyOption.REPLACE_EXISTING)

      val t = to.get
      t.imagePath = imageName


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
  override def addImageByBase64(menuItemId: Long, menuItemImage: String, isApiUpload: Boolean): Either[Boolean, Map[String, Any]] = {

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
        t.imagePath = imageName

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
  override def findByTeaSessionId(teaSessionId: Long, password: Option[String]): Map[String, Any] = {

    val toTeaSession = teaSessionDao.getById(teaSessionId)
    if(toTeaSession.isDefined){
    val tTeaSession = toTeaSession.get

      if(checkVisibilityAndPassword(tTeaSession, password)) {
        val modifiedMenuItem: List[MenuItemBean] =
        menuItemDao.findByTeaSessionId(teaSessionId).map( (menuItemBean: MenuItemBean) => {
          if(!isStringEmpty(menuItemBean.imagePath)){
            val tempMenuItemBean: MenuItemBean = menuItemBean.deepClone
            tempMenuItemBean.imagePath = generateImageUrl(tempMenuItemBean.imagePath)
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


  override def getImageByImageName(imageName: String): Array[Byte] = {

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
  override def update(menuItemId:Long, name: String, imagePath: Option[MultipartFile]): Map[String, Any] = {

    val to = menuItemDao.getById(menuItemId)

    if(to.isDefined)
    {
      val t = to.get
      t.name = name
      if(imagePath.isDefined){
        addImageByMultipart(menuItemId, imagePath.get, false)
      }
      val modifiedMenuItem: MenuItemBean = t.deepClone
      if(!isStringEmpty(modifiedMenuItem.imagePath)){
        modifiedMenuItem.imagePath = generateImageUrl(modifiedMenuItem.imagePath)
      }

      Map(
        "error" -> false,
        "message" -> "Update successfully",
        "menuItem" -> modifiedMenuItem
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
  override def deleteById(menuItemId: Long): Map[String, Any] = {

    val to = menuItemDao.getById(menuItemId)

    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
      override def afterCommit(): Unit = { //do stuff right after commit
        val t = to.get
        if(!isStringEmpty(t.imagePath)){
          deleteImage(t.imagePath)
        }
      }
    })

    if(to.isDefined)
    {
      val t = to.get
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


  override def deleteImage(imagePath: String): Boolean= {

    try{
      val deletePath = Paths.get(IMAGE_FILE_DIRECTORY + imagePath)
      if(Files.exists(deletePath)){
      Files.delete(deletePath)
      true
      }
      else {
        false
      }
    }
    catch {
      case e: IOException =>
        throw new RuntimeException(e)
        false
    }
  }


  def isStringEmpty(string: String): Boolean = string == null || string.trim.isEmpty


  def generateImageUrl(imageName: String): String = {
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
