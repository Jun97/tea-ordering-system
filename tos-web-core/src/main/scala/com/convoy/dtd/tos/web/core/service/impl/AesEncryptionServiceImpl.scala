package com.convoy.dtd.tos.web.core.service.impl

import java.nio.charset.Charset
import java.security.{InvalidAlgorithmParameterException, InvalidKeyException, NoSuchAlgorithmException, SecureRandom}
import java.security.spec.InvalidKeySpecException
import java.util.Base64

import com.convoy.dtd.tos.web.api.service.AesEncryptionService
import javax.crypto.spec.{IvParameterSpec, PBEKeySpec, SecretKeySpec}
import javax.crypto.{BadPaddingException, Cipher, IllegalBlockSizeException, KeyGenerator, NoSuchPaddingException, SecretKey, SecretKeyFactory}
import org.springframework.stereotype.Service

@Service
private[impl] class AesEncryptionServiceImpl extends AesEncryptionService {
  private val encryptionAlgorithm: String = "AES/CBC/PKCS5Padding"

  private val salt: String = "98kGq3719G98g49rGcQ8b3r"
  private val password: String = "snxA9-78Ja9-nCdu2L-zmK1O-0q1v6"


  override def getInstance():AesEncryptionService ={
    this
  }


  @throws[NoSuchAlgorithmException]
  override def generateKey(size: Int): SecretKey = {
    val keyGenerator = KeyGenerator.getInstance("AES")
    keyGenerator.init(size)
    val key = keyGenerator.generateKey
    key
  }


  @throws[NoSuchAlgorithmException]
  @throws[InvalidKeySpecException]
  override def getKeyFromPassword(): SecretKey = {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val spec = new PBEKeySpec(password.toCharArray, salt.getBytes, 65536, 256)
    val secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded, "AES")
    secret
  }


  override def generateIv: IvParameterSpec = {
    val iv: Array[Byte] = new Array[Byte](16)
    new SecureRandom().nextBytes(iv)
    new IvParameterSpec(iv)
  }


  @throws[NoSuchPaddingException]
  @throws[NoSuchAlgorithmException]
  @throws[InvalidAlgorithmParameterException]
  @throws[InvalidKeyException]
  @throws[BadPaddingException]
  @throws[IllegalBlockSizeException]
  override def encrypt( input: String, key: SecretKey, iv: IvParameterSpec): String = {
    val cipher = Cipher.getInstance(encryptionAlgorithm)
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)
    val cipherText = cipher.doFinal(input.getBytes)
    Base64.getEncoder.encodeToString(cipherText)
  }


  @throws[NoSuchPaddingException]
  @throws[NoSuchAlgorithmException]
  @throws[InvalidAlgorithmParameterException]
  @throws[InvalidKeyException]
  @throws[BadPaddingException]
  @throws[IllegalBlockSizeException]
  override def decrypt( cipherText: String, key: SecretKey, iv: IvParameterSpec): String = {
    val cipher = Cipher.getInstance(encryptionAlgorithm)
    cipher.init(Cipher.DECRYPT_MODE, key, iv)
    val plainText = cipher.doFinal(Base64.getDecoder.decode(cipherText))
    new String(plainText)
  }

  override def generateRandomString(size: Int): String = {
    val byteArray: Array[Byte] = new Array[Byte](size)
    new SecureRandom().nextBytes(byteArray)

    new String(byteArray, Charset.forName("UTF-8"))
  }
}
