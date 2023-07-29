package com.liuyaoli.myapplication.mvvm.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.liuyaoli.myapplication.mvvm.model.dao.NewsBriefDao
import com.liuyaoli.myapplication.mvvm.model.dao.NewsContentDao
import com.liuyaoli.myapplication.mvvm.model.entity.NewsBriefEntity
import com.liuyaoli.myapplication.mvvm.model.entity.NewsContentEntity

@Database(entities = [NewsBriefEntity::class, NewsContentEntity::class], version = 3, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract val newsBriefDao: NewsBriefDao
    abstract val newsContentDao: NewsContentDao
    companion object{
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        NewsDatabase::class.java,"news_database").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}