package com.example.passkeeper.viewmodels

import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import com.example.data.security.RepositorySecurity
import com.google.crypto.tink.Aead

class LoginViewModel(private val encryptedSharedPreferences: EncryptedSharedPreferences, private val aead: Aead) :
    ViewModel() {
    val securityRepo = RepositorySecurity(aead)
    fun checkPin(pin: String): Boolean {
        val hash = encryptedSharedPreferences.getString("pass", "pass") ?: "errorpass"
        val salt = encryptedSharedPreferences.getString("salt", "salt") ?: "errorsalt"
        return securityRepo.pinIsValid(pin, salt, hash)
    }
}