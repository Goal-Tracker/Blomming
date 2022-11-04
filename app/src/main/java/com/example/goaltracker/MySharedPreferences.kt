package com.example.goaltracker

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources

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

    fun setTheme(context: Context, theme: String){
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()

        var themeName = R.style.Theme_Coral
        if (theme == "#f69b94") {
            themeName = R.style.Theme_Coral
        } else if (theme == "#96b0e5") {
            themeName = R.style.Theme_Blue
        } else if (theme == "#f8c8c4") {
            themeName = R.style.Theme_BabyPink
        } else if (theme == "#fcdcce") {
            themeName = R.style.Theme_LightOrange
        } else if (theme == "#92b9e2") {
            themeName = R.style.Theme_LightBlue
        } else if (theme == "#aad3d7") {
            themeName = R.style.Theme_Mint
        }

        editor.putInt("theme", themeName)
        editor.commit()
    }

    fun getTheme(context: Context): Int {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getInt("theme", R.style.Theme_Mint)
    }
}