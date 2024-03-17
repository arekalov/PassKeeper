package com.arekalov.passkeeper.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.data.db.Password
import com.arekalov.data.db.PasswordDataBase
import kotlinx.coroutines.launch

class PasswordsViewModel(private val passwordDataBase: PasswordDataBase) : ViewModel() {
    private val passwordsLiveData = passwordDataBase.passwordDao().getPasswords()
    fun insertMeal(pass: Password) {
        viewModelScope.launch {
            passwordDataBase.passwordDao().updateOrInsert(pass)
        }
    }

    fun deleteMeal(pass: Password) {
        viewModelScope.launch {
            passwordDataBase.passwordDao().delete(pass)
        }
    }
}