package com.arekalov.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_table")
data class Password(
    @PrimaryKey @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "favicon") val favicon: String = "$url/favicon.ico"
)