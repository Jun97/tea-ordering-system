package com.convoy.dtd.tos.web.api.service

import com.convoy.dtd.tos.web.api.entity.MenuItemBean
import org.springframework.web.multipart.MultipartFile

trait MenuItemService
{

  def createMenuItem(teaSessionId: Long, menuItemName: String, menuItemImagePath: MultipartFile): Map[String, Any]
  def createMenuItemByBatch(teaSessionId: Long, menuList: List[MenuItemBean]): Map[String, Any]
  def addMenuItemImageByBase64(menuItemId: Long, menuItemImage: String, isApiUpload: Boolean): Either[Boolean, Map[String, Any]]
  def addMenuItemImageByMultipart(teaSessionId: Long, menuItemImage: MultipartFile, isApiUpload: Boolean): Either[Boolean, Map[String, Any]]
  def getMenuItemImage(imageName: String): Array[Byte]
  def getMenuItemByTeaSessionId(teaSessionId: Long, password: Option[String]): Map[String, Any]
  def updateMenuItem(menuItemId:Long, name: String): Map[String, Any]
  def deleteMenuItemById(menuItemId: Long): Map[String, Any]
}
