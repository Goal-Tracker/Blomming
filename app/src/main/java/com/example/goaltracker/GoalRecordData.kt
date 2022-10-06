package com.example.goaltracker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoalRecordData(
    val goalId: String,
    val title: String,
    val participateNum: Int,
    val startDate : String,
    val endDate : String,
    val todayNum: Int,
    val stampNum: Int,
    val teamNameList: ArrayList<String>,
    val teamThemeList: ArrayList<String>
) : Parcelable
