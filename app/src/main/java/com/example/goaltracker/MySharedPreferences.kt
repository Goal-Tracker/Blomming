package com.example.goaltracker

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

object MySharedPreferences {
    private val MY_ACCOUNT : String = "account"

    fun setUserId(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("UID", input)
        editor.apply()
    }

    fun getUserId(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("UID", "").toString()
    }

    fun setUserNickname(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("nickname", input)
        editor.apply()
    }

    fun getUserNickname(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("nickname", "").toString()
    }

    fun setUserColor(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("userColor", input)
        editor.apply()
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
        } else if (theme == "#f8c8c4") {
            themeName = R.style.Theme_BabyPink
        } else if (theme == "#fcdcce") {
            themeName = R.style.Theme_LightOrange
        } else if (theme == "#96b0e5") {
            themeName = R.style.Theme_Blue
        } else if (theme == "#92b9e2") {
            themeName = R.style.Theme_LightBlue
        } else if (theme == "#ebc0c7") {
            themeName = R.style.Theme_LightPink
        } else if (theme == "#7bb6c8") {
            themeName = R.style.Theme_Turquoise
        } else if (theme == "#aad3d7") {
            themeName = R.style.Theme_Mint
        } else if (theme == "#f5f1f0") {
            themeName = R.style.Theme_Beige
        } else if (theme == "#d5e3e6") {
            themeName = R.style.Theme_LightGreen
        } else if (theme == "#f2a4b1") {
            themeName = R.style.Theme_LightCoral
        } else if (theme == "#7175a5") {
            themeName = R.style.Theme_Indigo
        } else if (theme == "#a1b3d7") {
            themeName = R.style.Theme_BabyBlue
        } else if (theme == "#bd83cf") {
            themeName = R.style.Theme_Purple
        } else if (theme == "#e5afe9") {
            themeName = R.style.Theme_LightPurple
        }

        editor.putInt("theme", themeName)
        editor.apply()
    }

    fun getTheme(context: Context): Int {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getInt("theme", R.style.Theme_Mint)
    }
}