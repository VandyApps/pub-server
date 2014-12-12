package com.vandyapps.pubserver

trait RequestValidation {
  private val hasher =
    java.security.MessageDigest.getInstance("SHA-1")

  def apiKey: String

  def isValid(key: String, data: Int) = {
    val inputString: String = apiKey + data
    hasher.update(inputString.getBytes("UTF-8"))
    val hash = hasher.digest()
    key.equalsIgnoreCase(toHex(hash))
  }

  private def toHex(bytes: Array[Byte]) =
    javax.xml.bind.DatatypeConverter.printHexBinary(bytes)

}
