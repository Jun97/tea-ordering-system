package com.convoy.dtd.tos.web.core.mvc


import com.convoy.dtd.tos.web.api.entity.MenuItemBean
import com.convoy.dtd.tos.web.api.service.MenuItemService
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile

import scala.collection.JavaConverters._

@RestController
@RequestMapping(value = Array("/api/tea-session/menu-item"))
private[mvc] class MenuItemController {

  @Inject
  private var menuItemService: MenuItemService = _



  @RequestMapping(value = Array("add-by-batch"), method = Array(RequestMethod.POST), consumes = Array(MediaType.APPLICATION_JSON_VALUE)) //Not Working
  def createMenuItemByBatch(@RequestBody(required = true) payload: Map[String, Any]): Map[String,Any] =
  {
    //key: teaSessionId, menuItem
    val KEY_TEA_SESSION_ID = "teaSessionId"
    val KEY_MENU_ITEM = "menuItem"
    val gson:Gson = new Gson()

    if(payload.get(KEY_TEA_SESSION_ID).isDefined && payload.get(KEY_MENU_ITEM).isDefined)
    {
      val tesSessionId: Long = payload.get(KEY_TEA_SESSION_ID).get.asInstanceOf[Number].longValue()
      var menuList: List[Map[String, Any]] = payload.get(KEY_MENU_ITEM).get.asInstanceOf[List[Map[String, Any]]]

      System.out.println("hello", menuList.toString())

      val menuListMapped: List[MenuItemBean] = menuList.map((menuItem: Map[String,Any]) => {

        val jsonElement: JsonElement = gson.toJsonTree(menuItem)
        gson.fromJson(jsonElement, classOf[MenuItemBean])

      })
      menuItemService.createMenuItemByBatch(tesSessionId, menuListMapped)
    } else
    {
      Map(
        "error" -> true,
        "message" -> "Malformed JSON"
      )
    }
  }


  @RequestMapping(value = Array("add"), method = Array(RequestMethod.POST))
  def createMenuItem(@RequestParam(required = true) teaSessionId: Long, @RequestParam(required = true) menuItemName: String, menuItemImagePath: MultipartFile): Map[String,Any] =
  {
    menuItemService.createMenuItem(teaSessionId, menuItemName , menuItemImagePath)
  }


  @RequestMapping(value = Array("get-by-id"), method = Array(RequestMethod.POST))
  def getMenuItemByTeaSessionId(@RequestParam(required = true) teaSessionId: Long, password: String): Map[String,Any] =
  {
    menuItemService.getMenuItemByTeaSessionId(teaSessionId, Option(password))
  }


  @RequestMapping(value = Array("/image/{imageName:.+}"), method = Array(RequestMethod.GET), produces = Array(MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE))
  @ResponseBody
  def getTeaSessionImage(@PathVariable(name = "imageName", required = true) imageName: String): Array[Byte] = {
    menuItemService.getMenuItemImage(imageName)
  }


  @RequestMapping(value = Array("update-detail"))
  def updateTeaSessionDetail(@RequestParam(required = true) menuItemId: Long,
                             @RequestParam(required = true) name: String):Map[String,Any] =
  {
    menuItemService.updateMenuItem(menuItemId, name)
  }


  @RequestMapping(value = Array("delete"), method = Array(RequestMethod.POST))
  def deleteTeaSessionById(@RequestParam(required = true) menuItemId: Long): Map[String,Any] =
  {
    menuItemService.deleteMenuItemById(menuItemId)
  }


}
