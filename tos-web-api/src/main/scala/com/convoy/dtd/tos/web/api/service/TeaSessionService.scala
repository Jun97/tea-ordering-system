package com.convoy.dtd.tos.web.api.service

import com.convoy.dtd.tos.web.api.entity.TeaSessionBean
import org.springframework.web.multipart.MultipartFile

trait TeaSessionService
{
  def add( name: String,
                     description: String,
                     treatDate: String,
                     cutOffDate: String,
                     isPublic: Boolean,
                     password: Option[String],
                     userId:Long,
                     imagePath: Option[MultipartFile]): Map[String, Any]
  def addImageByMultipart(teaSessionId: Long, teaSessionImage: MultipartFile, isApiUpload: Boolean): Either[Boolean, Map[String, Any]]
  def getById(teaSessionId: Long): Option[TeaSessionBean]
  def findUpcoming(): List[TeaSessionBean]
  def getImageByImageName(imageName: String): Array[Byte]
  def updateDetail(teaSessionId: Long,
                             userId:Long,
                             name: Option[String],
                             description: Option[String],
                             treatDate: Option[String],
                             cutOffDate: Option[String],
                             teaSessionImagePath: Option[MultipartFile]): Map[String, Any]
  def updatePrivacy(teaSessionId: Long,
                              userId:Long,
                              isPublic: Boolean,
                              password: String): Map[String, Any]
  def deleteById(teaSessionId: Long, userId: Long): Map[String, Any]
  def deleteImage(imagePath: String): Boolean
  def generateImageUrl(imageName: String): String


}
