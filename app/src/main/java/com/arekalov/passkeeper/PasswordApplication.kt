package com.arekalov.passkeeper

import android.app.Application
import com.arekalov.data.db.PasswordDataBase
import com.arekalov.data.db.PasswordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PasswordApplication: Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { PasswordDataBase.getDatabase(this) }
    val repository by lazy { PasswordRepository(database.passwordDao()) }
}
