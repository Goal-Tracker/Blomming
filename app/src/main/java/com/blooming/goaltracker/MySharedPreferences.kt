package com.blooming.goaltracker

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException

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

    fun setUserEmail(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("email", input)
        editor.apply()
    }

    fun getUserEmail(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("email", "").toString()
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

    fun setUserMessage(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("message", input)
        editor.apply()
    }

    fun getUserMessage(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("message", "").toString()
    }

    fun setUserColorInt(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()

        var themeName = R.color.profile_color_coral
        when (input) {
            "#f69b94" -> {
                themeName = R.color.profile_color_coral
            }
            "#f8c8c4" -> {
                themeName =R.color.profile_color_babyPink
            }
            "#fcdcce" -> {
                themeName = R.color.profile_color_lightOrange
            }
            "#96b0e5" -> {
                themeName = R.color.profile_color_blue
            }
            "#92b9e2" -> {
                themeName = R.color.profile_color_lightBlue
            }
            "#ebc0c7" -> {
                themeName = R.color.profile_color_lightPink
            }
            "#7bb6c8" -> {
                themeName = R.color.profile_color_turquoise
            }
            "#aad3d7" -> {
                themeName = R.color.profile_color_mint
            }
            "#f5f1f0" -> {
                themeName = R.color.profile_color_beige
            }
            "#d5e3e6" -> {
                themeName = R.color.profile_color_lightGreen
            }
            "#f2a4b1" -> {
                themeName = R.color.profile_color_lightCoral
            }
            "#7175a5" -> {
                themeName = R.color.profile_color_indigo
            }
            "#a1b3d7" -> {
                themeName = R.color.profile_color_babyBlue
            }
            "#bd83cf" -> {
                themeName = R.color.profile_color_purple
            }
            "#e5afe9" -> {
                themeName = R.color.profile_color_lightPurple
            }
        }

        editor.putInt("userColorInt", themeName)
        editor.apply()
    }

    fun getUserColorInt(context: Context): Int {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getInt("userColorInt", R.color.profile_color_coral)
    }

    fun setTheme(context: Context, theme: String){
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()

        var themeName = R.style.Theme_Coral
        when (theme) {
            "#f69b94" -> {
                themeName = R.style.Theme_Coral
            }
            "#f8c8c4" -> {
                themeName = R.style.Theme_BabyPink
            }
            "#fcdcce" -> {
                themeName = R.style.Theme_LightOrange
            }
            "#96b0e5" -> {
                themeName = R.style.Theme_Blue
            }
            "#92b9e2" -> {
                themeName = R.style.Theme_LightBlue
            }
            "#ebc0c7" -> {
                themeName = R.style.Theme_LightPink
            }
            "#7bb6c8" -> {
                themeName = R.style.Theme_Turquoise
            }
            "#aad3d7" -> {
                themeName = R.style.Theme_Mint
            }
            "#f5f1f0" -> {
                themeName = R.style.Theme_Beige
            }
            "#d5e3e6" -> {
                themeName = R.style.Theme_LightGreen
            }
            "#f2a4b1" -> {
                themeName = R.style.Theme_LightCoral
            }
            "#7175a5" -> {
                themeName = R.style.Theme_Indigo
            }
            "#a1b3d7" -> {
                themeName = R.style.Theme_BabyBlue
            }
            "#bd83cf" -> {
                themeName = R.style.Theme_Purple
            }
            "#e5afe9" -> {
                themeName = R.style.Theme_LightPurple
            }
        }

        editor.putInt("theme", themeName)
        editor.apply()
    }

    fun getTheme(context: Context): Int {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getInt("theme", R.style.Theme_Mint)
    }

    fun setFriendList(context: Context, values: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        var a = JSONArray()
        for (value in values) {
            a.put(value)
        }
        if (values.isNotEmpty()) {
            editor.putString("friendList", a.toString())
        } else {
            editor.putString("friendList", null)
        }
        editor.apply()
    }

    fun getFriendList(context: Context) : ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val json = prefs.getString("friendList", null)
        val urls = ArrayList<String>()
        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val url = a.optString(i)
                    urls.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return urls
    }

    fun setGoalList(context: Context, values: ArrayList<String>) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        var a = JSONArray()
        for (value in values) {
            a.put(value)
        }
        if (values.isNotEmpty()) {
            editor.putString("goalList", a.toString())
        } else {
            editor.putString("goalList", null)
        }
        editor.apply()
    }

    fun getGoalList(context: Context) : ArrayList<String> {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val json = prefs.getString("goalList", null)
        val urls = ArrayList<String>()
        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val url = a.optString(i)
                    urls.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return urls
    }



}