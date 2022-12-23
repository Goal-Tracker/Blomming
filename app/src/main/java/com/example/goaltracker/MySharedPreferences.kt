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

    fun setUserColorInt(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()

        var themeName = R.color.profile_color_coral
        if (input == "#f69b94") {
            themeName = R.color.profile_color_coral
        } else if (input == "#f8c8c4") {
            themeName =R.color.profile_color_babyPink
        } else if (input == "#fcdcce") {
            themeName = R.color.profile_color_lightOrange
        } else if (input == "#96b0e5") {
            themeName = R.color.profile_color_blue
        } else if (input == "#92b9e2") {
            themeName = R.color.profile_color_lightBlue
        } else if (input == "#ebc0c7") {
            themeName = R.color.profile_color_lightPink
        } else if (input == "#7bb6c8") {
            themeName = R.color.profile_color_turquoise
        } else if (input == "#aad3d7") {
            themeName = R.color.profile_color_mint
        } else if (input == "#f5f1f0") {
            themeName = R.color.profile_color_beige
        } else if (input == "#d5e3e6") {
            themeName = R.color.profile_color_lightGreen
        } else if (input == "#f2a4b1") {
            themeName = R.color.profile_color_lightCoral
        } else if (input == "#7175a5") {
            themeName = R.color.profile_color_indigo
        } else if (input == "#a1b3d7") {
            themeName = R.color.profile_color_babyBlue
        } else if (input == "#bd83cf") {
            themeName = R.color.profile_color_purple
        } else if (input == "#e5afe9") {
            themeName = R.color.profile_color_lightPurple
        }

        editor.putInt("userColorInt", themeName)
        editor.commit()
    }

    fun getUserColorInt(context: Context): Int {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getInt("userColorInt", R.color.profile_color_coral)
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