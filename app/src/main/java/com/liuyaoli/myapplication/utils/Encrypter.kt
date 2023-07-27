package com.liuyaoli.myapplication.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

object Encrypter {
    fun encrypt(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray(UTF_8))
        val bigInt = BigInteger(1, hashedBytes)
        return bigInt.toString(16).padStart(64, '0')
    }
}