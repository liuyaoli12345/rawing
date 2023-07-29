package com.liuyaoli.myapplication.mvvm.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_table", indices = [Index(value = ["user_name"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    @ColumnInfo(name = "user_name")
    val userName: String,
    val avatarUri: String
)