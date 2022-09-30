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
    var NotificationList:List<String>?=null
) : Parcelable

//public val curUser = Account()

@Parcelize
data class Notification (
    var GoalName:String ?="",
    var Message: String = "",
    var GoalType: Int = 0,
    var UserColor:String ?= "",
    var UserName:String ?= null,
    var FriendId:String ?= ""
) : Parcelable
