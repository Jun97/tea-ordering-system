package com.convoy.dtd.tos.web.api.service

import com.convoy.dtd.tos.web.api.entity.MenuItemBean

trait MenuItemService
{

  def createMenuItemByBatch(teaSessionId: Long, menuList: List[MenuItemBean]): Map[String, Any]
  def addMenuItemImage(menuItemId: Long, teaSessionImage: String): Boolean
  def getMenuItemImage(imageName: String): Array[Byte]
  def getMenuItemByTeaSessionId(teaSessionId: Long, password: Option[String]): Map[String, Any]
  def updateMenuItem(menuItemId:Long, name: String): Map[String, Any]
  def deleteMenuItemById(menuItemId: Long): Map[String, Any]
}
