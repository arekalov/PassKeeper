package com.arekalov.passkeeper.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.data.db.Password
import com.arekalov.data.db.PasswordDataBase
import com.arekalov.data.db.PasswordRepository
import kotlinx.coroutines.launch

class PasswordsViewModel(private val repository: PasswordRepository) : ViewModel() {
    val passwordsLiveData = repository.allPasswords
    fun insertPassword(pass: Password) {
        viewModelScope.launch {
            repository.insert(pass)
        }
    }

    fun deletePassword(pass: Password) {
        viewModelScope.launch {
            repository.delete(pass)
        }
    }
}