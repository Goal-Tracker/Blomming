package com.example.goaltracker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account (
    var email: String = "",
    var userName: String ?= null,
    var userColor:String ?= null,
    var myGoalList:List<String>?=null,
    var notificationList:List<String>?=null,
    var userMessage:String?=null
) : Parcelable

//public val curUser = Account()

@Parcelize
data class Notifications (
    var goalName:String ?="",
    var message: String = "",
    var type: Int = 0,
    var read: Boolean = false,
    var userColor:String ?= "",
    var userName:String ?= null,
    var requestUserId:String ?= "",
    var requestGoalId:String ?= ""
) : Parcelable
