package com.convoy.dtd.tos.web.api.service

import com.convoy.dtd.tos.web.api.entity.MenuItemBean
import org.springframework.web.multipart.MultipartFile

trait MenuItemService
{

  def add(teaSessionId: Long, name: String, imagePath: Option[MultipartFile]): Map[String, Any]
  def addByBatch(teaSessionId: Long, menuList: List[MenuItemBean]): Map[String, Any]
  def addImageByBase64(menuItemId: Long, menuItemImage: String, isApiUpload: Boolean): Either[Boolean, Map[String, Any]]
  def addImageByMultipart(teaSessionId: Long, menuItemImage: MultipartFile, isApiUpload: Boolean): Either[Boolean, Map[String, Any]]
  def getImageByImageName(imageName: String): Array[Byte]
  def findByTeaSessionId(teaSessionId: Long, password: Option[String]): Map[String, Any]
  def update(menuItemId:Long, name: String, imagePath: Option[MultipartFile]): Map[String, Any]
  def deleteById(menuItemId: Long): Map[String, Any]
  def deleteImage(imagePath: String): Boolean
}
