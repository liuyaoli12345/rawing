package com.liuyaoli.myapplication.mvvm.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.liuyaoli.myapplication.mvvm.model.entity.NewsBriefEntity

@Dao
interface NewsBriefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsBrief: NewsBriefEntity): Long

    @Update
    fun update(newsBrief: NewsBriefEntity)

    @Query("SELECT * FROM news_brief_table WHERE newsId = :key")
    fun get(key: Long): NewsBriefEntity?

    @Query("SELECT * FROM news_brief_table ORDER BY newsId DESC")
    fun getAllNewsBrief(): List<NewsBriefEntity>?

    @Query("SELECT * FROM news_brief_table WHERE author = :userName")
    fun findNewsBriefByUserName(userName: String): List<NewsBriefEntity>?

    @Query("DELETE FROM news_brief_table")
    fun clear()
}