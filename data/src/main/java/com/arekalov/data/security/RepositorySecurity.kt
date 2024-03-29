package com.arekalov.data.security

import com.arekalov.data.db.Password
import com.google.crypto.tink.Aead
import java.security.GeneralSecurityException
import java.security.SecureRandom
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class RepositorySecurity(val aead: Aead) {
    @OptIn(ExperimentalEncodingApi::class)
    fun encodePin(pin: String): Pair<String, String> {
        val salt = generateSalt()
        val secretKey = Pbkdf2Factory.createKey(pin.toCharArray(), salt)
        val encryptedPin = Base64.encode(
            aead.encrypt(
                pin.toByteArray(),
                secretKey.encoded
            )
        )
        return Pair(first = encryptedPin, second = Base64.encode(salt))
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun encodePassword(password: String): String {
        return Base64.encode(aead.encrypt(password.toByteArray(), null))
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decodePassword(encodedPasswordBase64: String): String {
        return String(aead.decrypt(Base64.decode(encodedPasswordBase64), null))
    }


    private fun generateSalt(lengthByte: Int = 32): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(lengthByte)
        random.nextBytes(salt)
        return salt
    }


    @OptIn(ExperimentalEncodingApi::class)
    fun pinIsValid(pin: String, hashedEncryptedPin: String, hashedSalt: String): Boolean {
        val salt = Base64.decode(
            hashedSalt
        )
        val secretKey = Pbkdf2Factory.createKey(pin.toCharArray(), salt)
        val token = try {
            val encryptedToken = Base64.decode(
                hashedEncryptedPin
            )
            aead.decrypt(encryptedToken, secretKey.encoded)
        } catch (e: GeneralSecurityException) {
            null
        }
        return token?.isNotEmpty() ?: false
    }

}