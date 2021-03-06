package com.convoy.dtd.tos.web.core.mvc


import java.util.Date

import com.convoy.dtd.tos.web.api.entity.TeaSessionBean
import javax.inject.Inject
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RequestParam, ResponseBody, RestController}
import com.convoy.dtd.tos.web.api.service.TeaSessionService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping(value = Array("/api/tea-session"))
private[mvc] class TeaSessionController {

  @Inject
  private var teaSessionService: TeaSessionService = _


  @RequestMapping(value = Array("add"), method = Array(RequestMethod.POST))
  def add(@RequestParam(required = true) name: String,
                           @RequestParam(required = false) description: String,
                           @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") treatDate: Date,
                           @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") cutOffDate: Date,
                           @RequestParam(required = true) isPublic: Boolean,
                           @RequestParam(required = false) password: String,
                           @RequestParam(required = true) userId:Long,
                           @RequestParam(required = false) teaSessionImagePath: MultipartFile): Map[String,Any] =
  {
    teaSessionService.add(name, Option(description),treatDate, cutOffDate, isPublic, Option(password) , userId, Option(teaSessionImagePath))
  }


  @RequestMapping(value=Array("image/add-by-multipart"), method = Array(RequestMethod.POST), consumes = Array(MediaType.MULTIPART_FORM_DATA_VALUE))
  def addImageByMultipart( @RequestParam(required = true) teaSessionId: Long, @RequestParam(required = true, name="teaSessionImagePath") teaSessionImagePath: MultipartFile): Map[String, Any] =
  {
    teaSessionService.addImageByMultipart(teaSessionId, teaSessionImagePath, true)
      .fold(left => null,
            right=> right
    )
  }


  @RequestMapping(value = Array("find-upcoming"), method = Array(RequestMethod.GET))
  def findUpcoming(): List[TeaSessionBean] =
  {
    teaSessionService.findUpcoming()
  }

  @RequestMapping(value = Array("get-by-id"), method = Array(RequestMethod.POST))
  def getById(@RequestParam(required = true) teaSessionId: Long): Option[TeaSessionBean] =
  {
    teaSessionService.getById(teaSessionId)
  }


  @RequestMapping(value = Array("/image/{imageName:.+}"), method = Array(RequestMethod.GET), produces = Array(MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE))
  @ResponseBody
  def getImage(@PathVariable(name = "imageName", required = true) imageName: String): Array[Byte] = {
    teaSessionService.getImageByImageName(imageName)
  }


  @RequestMapping(value = Array("update"))
  def updateDetail(@RequestParam(required = true) teaSessionId: Long,
                   @RequestParam(required = true) userId:Long,
                   name: String,
                   description: String,
                   @DateTimeFormat(pattern = "yyyy-MM-dd") treatDate: Date,
                   @DateTimeFormat(pattern = "yyyy-MM-dd") cutOffDate: Date,
                   teaSessionImagePath: MultipartFile,
                   isPublic: Boolean,
                   password: String):Map[String,Any] =
  {
    teaSessionService.update(teaSessionId,
                                    userId,
                                    name,
                                    Option(description),
                                    treatDate,
                                    cutOffDate,
                                    Option(teaSessionImagePath),
                                    isPublic,
                                    Option(password))
  }

//  @RequestMapping(value = Array("update-privacy"))
//  def updatePrivacy( @RequestParam(required = true) teaSessionId: Long,
//                               @RequestParam(required = true) userId:Long,
//                               @RequestParam(required = true) isPublic: Boolean,
//                               @RequestParam(required = true) password: String):Map[String,Any] =
//  {
//    teaSessionService.updatePrivacy(teaSessionId: Long, userId:Long, isPublic: Boolean, password: String)
//  }


  @RequestMapping(value = Array("delete-by-id"), method = Array(RequestMethod.POST))
  def deleteTeaSessionById(@RequestParam(required = true) teaSessionId: Long, @RequestParam(required = true) userId: Long): Map[String,Any] =
  {
    teaSessionService.deleteById(teaSessionId, userId)
  }


  @RequestMapping(value = Array("get-share-link"), method = Array(RequestMethod.POST))
  def getShareLink(@RequestParam(required = true) teaSessionId: Long): Map[String,Any] =
  {
    teaSessionService.getShareLink(teaSessionId)
  }
}
