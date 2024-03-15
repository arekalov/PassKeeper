package com.example.passkeeper.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import com.example.data.security.RepositorySecurity
import com.google.crypto.tink.Aead

class LoginViewModel(private val encryptedSharedPreferences: SharedPreferences, private val aead: Aead) :
    ViewModel() {
    val securityRepo = RepositorySecurity(aead)
    fun checkPin(pin: String): Boolean {
        val hash = encryptedSharedPreferences.getString("pass", "pass") ?: "errorpass"
        val salt = encryptedSharedPreferences.getString("salt", "salt") ?: "errorsalt"
        return securityRepo.pinIsValid(pin, salt, hash)
    }
}