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
  def addByBatch(@RequestBody(required = true) payload: Map[String, Any]): Map[String,Any] =
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
      menuItemService.addByBatch(tesSessionId, menuListMapped)
    } else
    {
      Map(
        "error" -> true,
        "message" -> "Malformed JSON"
      )
    }
  }


  @RequestMapping(value = Array("add"), method = Array(RequestMethod.POST))
  def add(@RequestParam(required = true) teaSessionId: Long, @RequestParam(required = true) name: String, imagePath: MultipartFile): Map[String,Any] =
  {
    menuItemService.add(teaSessionId, name , Option(imagePath) )
  }


  @RequestMapping(value=Array("add-image-by-multipart"), method = Array(RequestMethod.POST), consumes = Array(MediaType.MULTIPART_FORM_DATA_VALUE))
  def addImageByMultipart( @RequestParam(required = true) menuItemId: Long, @RequestParam(required = true, name="imagePath") imagePath: MultipartFile): Map[String, Any] =
  {
    menuItemService.addImageByMultipart(menuItemId, imagePath, true)
      .fold(left => null,
        right=> right
      )
  }


  @RequestMapping(value = Array("find-by-tea-session-id"), method = Array(RequestMethod.POST))
  def findByTeaSessionId(@RequestParam(required = true) teaSessionId: Long, password: String): Map[String,Any] =
  {
    menuItemService.findByTeaSessionId(teaSessionId, Option(password))
  }


  @RequestMapping(value = Array("/image/{imageName:.+}"), method = Array(RequestMethod.GET), produces = Array(MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE))
  @ResponseBody
  def getImageByImageName(@PathVariable(name = "imageName", required = true) imageName: String): Array[Byte] = {
    menuItemService.getImageByImageName(imageName)
  }


  @RequestMapping(value = Array("update"))
  def update(@RequestParam(required = true) menuItemId: Long,
                     @RequestParam(required = true) name: String,
                     imagePath: MultipartFile):Map[String,Any] =
  {
    menuItemService.update(menuItemId, name, Option(imagePath))
  }


  @RequestMapping(value = Array("delete-by-id"), method = Array(RequestMethod.POST))
  def deleteById(@RequestParam(required = true) menuItemId: Long): Map[String,Any] =
  {
    menuItemService.deleteById(menuItemId)
  }


}
