package com.example.goaltracker

import com.google.firebase.auth.FirebaseAuth

class Account (var Email: String = "",
                    var UserName: String ?= null,
                    var UserColor:String ?= null,
                    var FriendsList:List<String>?=null,
                    var MyGoalList:List<String>?=null,
                    var NotificationList:List<String>?=null)

public val curUser = Account()

//class Friends (var Email: String = "",
//                    var UserName: String ?= null,
//                    var UserColor: String ?= null)

//public class Goal (var action: Boolean?=false,
//                 var day: Int?=null,
//                 var start_day: String?=null,
//                 var end_day: String?=null,
//                 var memo: String?=null,
//                 var stamp_id: String?=null,
//                 var team: List<String>?=null,
//                 var title: String="")

class Notification (var goalType: Int = 0,
                    var message: String = "",
                    var name_short:String ?= null,
                    var profile_color:String ?= "",
                    var friendId:String ?= "",
                    var goalId:String ?="")



class Auth {
    companion object {
        private val TAG = Auth::class.java.simpleName
        private lateinit var auth: FirebaseAuth
        private var curUser = Account()

        fun getUid():String {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }

        fun getCurUser():Account{


            return curUser
        }
    }
}