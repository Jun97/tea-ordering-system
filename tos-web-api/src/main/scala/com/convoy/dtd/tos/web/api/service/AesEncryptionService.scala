package com.convoy.dtd.tos.web.api.service

import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

trait AesEncryptionService {
  def getInstance(): AesEncryptionService
  def generateKey(n: Int): SecretKey
  def getKeyFromPassword(): SecretKey
  def generateIv: IvParameterSpec
  def encrypt( input: String, key: SecretKey, iv: IvParameterSpec): String
  def decrypt( cipherText: String, key: SecretKey, iv: IvParameterSpec): String
  def generateRandomString(size: Int): String
}
