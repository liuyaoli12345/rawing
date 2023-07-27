package com.liuyaoli.myapplication.utils

import android.content.Context
import com.liuyaoli.myapplication.constants.UserConstants.SHARED_PREFS_NAME

object LoggedInUserManager {

    fun saveUserLoginStatus(context: Context, UserName: String, UserId: String, AvatarUri: String, isLoggedIn: Boolean) {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("LoggedInUserName", UserName)
        editor.putString("LoggedInUserId", UserId)
        editor.putString("LoggedInUserAvatarUri", AvatarUri)
        editor.apply()
    }

    fun clearUserLoginStatus(context: Context){
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()
    }

    fun checkIfLoggedIn(context: Context): Boolean{
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isLoggedIn", false)
    }

    fun getLoggedInUserName(context: Context): String{
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString("LoggedInUserName", "")!!
    }

    fun getLoggedInUserId(context: Context): String{
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString("LoggedInUserId", "")!!
    }

    fun getLoggedInUserAvatarUri(context: Context): String{
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString("LoggedInUserAvatarUri", "")!!
    }
}