package com.example.goaltracker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account (
    var uid:String = "",
    var email: String = "",
    var userName: String ?= null,
    var userColor:String ?= null,
    var myGoalList:ArrayList<String> = arrayListOf(),
    var userMessage:String?=null
) : Parcelable

//public val curUser = Account()

@Parcelize
data class Notifications (
    var goalName:String ?="",
    var goalUid:String ?="",
    var message: String ?= "",
    var type: Int = 0,
    var read: Boolean = false,
    var userColor:String ?= "",
    var userName:String ?= null,
    var requestUserId:String ?= ""
) : Parcelable