package com.arekalov.data.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class PasswordRepository(private val passwordDao: PasswordDao) {
    val allPasswords: LiveData<List<Password>> = passwordDao.getPasswords()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(pass: Password) {
        passwordDao.updateOrInsert(pass)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(pass: Password) {
        passwordDao.delete(pass)
    }
}