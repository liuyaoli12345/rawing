package com.liuyaoli.myapplication.mvvm.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "news_content_table",
    foreignKeys = [
        ForeignKey(
            entity = NewsBriefEntity::class,
            parentColumns = ["newsId"],
            childColumns = ["newsId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class NewsContentEntity(
    @PrimaryKey(autoGenerate = false)
    val newsId: Long,
    val headImgUri: String,
    val newsAbstract: String,
    val newsContext: String
)