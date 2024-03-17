package com.arekalov.passkeeper.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arekalov.data.db.PasswordDataBase

class PasswordsViewModelFactory(private val passwordDataBase: PasswordDataBase) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasswordsViewModel(passwordDataBase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}