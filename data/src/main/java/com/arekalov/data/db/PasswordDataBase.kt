package com.arekalov.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Password::class), version = 1, exportSchema = false)
public abstract class PasswordDataBase : RoomDatabase() {

    abstract fun passwordDao(): PasswordDao

    companion object {
        @Volatile
        private var INSTANCE: PasswordDataBase? = null

        fun getDatabase(context: Context): PasswordDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PasswordDataBase::class.java,
                    "password_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}