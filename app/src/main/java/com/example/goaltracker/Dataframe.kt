package com.example.goaltracker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account (
    var Email: String = "",
    var UserName: String ?= null,
    var UserColor:String ?= null,
    var MyGoalList:List<String>?=null,
    var NotificationList:List<String>?=null,
    var UserMessage:String?=null
) : Parcelable

//public val curUser = Account()

@Parcelize
data class Notifications (
    var goalName:String ?="",
    var goalUid:String ?="",
    var message: String = "",
    var type: Int = 0,
    var read: Boolean = false,
    var userColor:String ?= "",
    var userName:String ?= null,
    var requestUserId:String ?= "",
    var requestGoalId:String ?= ""
) : Parcelable