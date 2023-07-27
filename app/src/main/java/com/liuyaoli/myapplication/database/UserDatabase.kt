package com.liuyaoli.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.liuyaoli.myapplication.database.dao.UserDao
import com.liuyaoli.myapplication.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 4, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,UserDatabase::class.java,"news_database").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}