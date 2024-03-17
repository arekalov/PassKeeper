package com.arekalov.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PasswordDao {
    @Query("SELECT * FROM password_table")
    fun getPasswords(): LiveData<List<Password>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(password: Password)

    @Delete
    suspend fun delete(password: Password)
}