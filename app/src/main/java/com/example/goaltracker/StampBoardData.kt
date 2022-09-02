package com.example.goaltracker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StampBoardData(
    val goal_id: String,
    val num: Int,
    val stamp: Boolean,
    val participateNum: Int,
    val stampNum: Int,
    val stampThemeList: ArrayList<String>
) : Parcelable