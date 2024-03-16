package com.example.passkeeper.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.crypto.tink.Aead

class LoginViewModelFactory(private val encryptedSharedPreferences: SharedPreferences, private val aead: Aead) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(encryptedSharedPreferences, aead) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}