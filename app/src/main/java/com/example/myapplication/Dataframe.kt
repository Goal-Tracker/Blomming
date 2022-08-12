package com.example.myapplication

import com.google.firebase.auth.FirebaseAuth

class Account (var userId: String = "",
                    var userName: String ?= null,
                    var password: String = "",
                    var user_color:String ?= null,
                    var friends:List<String>?=null,
                    var myGoalList:List<String>?=null)

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