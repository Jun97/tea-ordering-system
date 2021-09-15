package com.convoy.dtd.tos.web.core.mvc


import javax.inject.Inject
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RequestParam, ResponseBody, RestController}
import com.convoy.dtd.tos.web.api.service.TeaSessionService
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping(value = Array("/api/tea-session"))
private[mvc] class TeaSessionController {

  @Inject
  private var teaSessionService: TeaSessionService = _


  @RequestMapping(value = Array("add"), method = Array(RequestMethod.POST))
  def createTeaSessionById(@RequestParam(required = true) name: String,
                           @RequestParam(required = true) description: String,
                           @RequestParam(required = true) treatDate: String,
                           @RequestParam(required = true) cutOffDate: String,
                           @RequestParam(required = true) isPublic: Boolean,
                           @RequestParam(required = false, defaultValue = "") password: String,
                           @RequestParam(required = true) userId:Long): Map[String,Any] =
  {
    teaSessionService.createTeaSession(name: String, description: String,treatDate: String, cutOffDate: String, isPublic: Boolean, password: String, userId:Long)
  }


  @RequestMapping(value=Array("image/add"), method = Array(RequestMethod.POST), consumes = Array(MediaType.MULTIPART_FORM_DATA_VALUE))
  def addTeaSessionImage( @RequestParam(required = true) teaSessionId: Long, @RequestParam(required = true, name="teaSessionImage") teaSessionImage: MultipartFile): Map[String, Any] =
  {
    teaSessionService.addTeaSessionImage(teaSessionId, teaSessionImage)
  }


  @RequestMapping(value = Array("get-upcoming"), method = Array(RequestMethod.GET))
  def getTeaSessionUpcoming(): Map[String,Any] =
  {
    teaSessionService.getTeaSessionUpcoming()
  }

  @RequestMapping(value = Array("get-by-id"), method = Array(RequestMethod.POST))
  def getTeaSessionById(@RequestParam(required = true) teaSessionId: Long): Map[String,Any] =
  {
    teaSessionService.getTeaSessionById(teaSessionId)
  }


  @RequestMapping(value = Array("/image/{imageName:.+}"), method = Array(RequestMethod.GET), produces = Array(MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE))
  @ResponseBody
  def getTeaSessionImage(@PathVariable(name = "imageName", required = true) imageName: String): Array[Byte] = {
    teaSessionService.getTeaSessionImage(imageName)
  }


  @RequestMapping(value = Array("update-detail"))
  def updateTeaSessionDetail(@RequestParam(required = true) teaSessionId: Long,
                             @RequestParam(required = true) userId:Long,
                             name: String,
                             description: String,
                             treatDate: String,
                             cutOffDate: String):Map[String,Any] =
  {
    teaSessionService.updateTeaSessionDetail(teaSessionId: Long, userId:Long, name: String, description: String,treatDate: String, cutOffDate: String)
  }

  @RequestMapping(value = Array("update-privacy"))
  def updateTeaSessionPrivacy( @RequestParam(required = true) teaSessionId: Long,
                               @RequestParam(required = true) userId:Long,
                               @RequestParam(required = true) isPublic: Boolean,
                               @RequestParam(required = true) password: String):Map[String,Any] =
  {
    teaSessionService.updateTeaSessionPrivacy(teaSessionId: Long, userId:Long, isPublic: Boolean, password: String)
  }


  @RequestMapping(value = Array("delete"), method = Array(RequestMethod.POST))
  def deleteTeaSessionById(teaSessionId: Long, userId: Long): Map[String,Any] =
  {
    teaSessionService.deleteTeaSessionById(teaSessionId, userId)
  }


}
