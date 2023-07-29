package com.liuyaoli.myapplication.mvvm.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_brief_table")
data class NewsBriefEntity(
    @PrimaryKey(autoGenerate = true)
    val newsId: Long?,
    val title: String,
    val coverUri: String,
    val author: String,
    val status: String
)