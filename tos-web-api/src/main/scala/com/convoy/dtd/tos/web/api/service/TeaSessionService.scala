package com.convoy.dtd.tos.web.api.service

import org.springframework.web.multipart.MultipartFile

trait TeaSessionService
{
  def createTeaSession(name: String,
                       description: String,
                       treatDate: String,
                       cutOffDate: String,
                       isPublic: Boolean,
                       password: Option[String],
                       userId:Long,
                       teaSessionImagePath: Option[MultipartFile]): Map[String, Any]
  def addTeaSessionImageByMultipart(teaSessionId: Long, teaSessionImage: MultipartFile, isApiUpload: Boolean): Either[Boolean, Map[String, Any]]
  def getTeaSessionById(teaSessionId: Long): Map[String, Any]
  def getTeaSessionUpcoming(): Map[String, Any]
  def getTeaSessionImage(imageName: String): Array[Byte]
  def updateTeaSessionDetail(teaSessionId: Long,
                             userId:Long,
                             name: Option[String],
                             description: Option[String],
                             treatDate: Option[String],
                             cutOffDate: Option[String],
                             teaSessionImagePath: Option[MultipartFile]): Map[String, Any]
  def updateTeaSessionPrivacy(teaSessionId: Long,
                              userId:Long,
                              isPublic: Boolean,
                              password: String): Map[String, Any]
  def deleteTeaSessionById(teaSessionId: Long, userId: Long): Map[String, Any]
  def deleteTeaSessionImage(teaSessionImagePath: String): Boolean
  def generateTeaSessionImageUrl(imageName: String): String


}
