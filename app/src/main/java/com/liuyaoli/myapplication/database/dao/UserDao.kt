package com.liuyaoli.myapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.liuyaoli.myapplication.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    fun insert(user: UserEntity)
    @Update
    fun update(user: UserEntity)
    @Query("SELECT * FROM user_table WHERE userId = :key")
    fun findByUserId(key: String): UserEntity?
    @Query("DELETE FROM user_table")
    fun clear()
    @Query("SELECT * FROM user_table WHERE user_name = :key")
    fun findByUserName(key: String): UserEntity?
    @Query("SELECT * FROM user_table ORDER BY user_name DESC")
    fun getAllOnceLoggedInUser(): LiveData<List<UserEntity>>?
}