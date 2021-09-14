package com.convoy.dtd.tos.web.api.service

import org.springframework.web.multipart.MultipartFile

trait TeaSessionService
{
  def createTeaSession(name: String,
                       description: String,
                       treatDate: String,
                       cutOffDate: String,
                       isPublic: Boolean,
                       password: String,
                       userId:Long): Map[String, Any]
  def addTeaSessionImage(teaSessionId: Long, teaSessionImage: MultipartFile): Map[String, Any]
  def getTeaSessionById(teaSessionId: Long): Map[String, Any]
  def getTeaSessionImage(imageName: String): Array[Byte]
  def getTeaSessionUpcoming(): Map[String, Any]
  def updateTeaSession(teaSessionId: Long,
                       userId:Long,
                       name: String,
                       description: String,
                       treatDate: String,
                       cutOffDate: String,
                       isPublic: Boolean,
                       password: String): Map[String, Any]
  def deleteTeaSessionById(teaSessionId: Long, userId: Long): Map[String, Any]


}
