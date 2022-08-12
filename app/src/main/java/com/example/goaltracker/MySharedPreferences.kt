package com.example.goaltracker

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private val MY_ACCOUNT : String = "account"

    fun setUserId(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("UID", input)
        editor.commit()
    }

    fun getUserId(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("UID", "").toString()
    }

    fun setUserNickname(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("nickname", input)
        editor.commit()
    }

    fun getUserNickname(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("nickname", "").toString()
    }

    fun setUserColor(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("userColor", input)
        editor.commit()
    }

    fun getUserColor(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("userColor", "").toString()
    }
}