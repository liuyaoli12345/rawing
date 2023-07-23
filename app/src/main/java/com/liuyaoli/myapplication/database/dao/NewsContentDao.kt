package com.liuyaoli.myapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.liuyaoli.myapplication.database.entity.NewsContentEntity

@Dao
interface NewsContentDao {
    @Insert
    fun insert(newsBrief: NewsContentEntity)

    @Update
    fun update(newsBrief: NewsContentEntity)

    @Query("SELECT * FROM news_content_table WHERE newsId = :key")
    fun get(key: Long): NewsContentEntity?

    @Query("SELECT * FROM news_content_table ORDER BY newsId DESC")
    fun getAllNewsContent(): LiveData<List<NewsContentEntity>>?

    @Query("DELETE FROM news_content_table")
    fun clear()
}