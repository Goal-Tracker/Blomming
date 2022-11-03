package com.example.goaltracker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account (
    var Email: String = "",
    var UserName: String ?= null,
    var UserColor:String ?= null,
    var FriendsList:List<String>?=null,
    var MyGoalList:List<String>?=null,
    var NotificationList:List<String>?=null,
    var UserMessage:String?=null
) : Parcelable

//public val curUser = Account()

@Parcelize
data class Notifications (
    var goalName:String ?="",
    var message: String = "",
    var type: Int = 0,
    var userColor:String ?= "",
    var userName:String ?= null,
    var friendId:String ?= ""
) : Parcelable
