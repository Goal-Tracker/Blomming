package com.example.myapplication

public data class Account (var userId: String ?= null,
                    var userName: String ?= null,
                    var password: String ?= null,
                    var user_color:String ?= null,
                    var friends:List<String>?=null)

public data class Goal (var action: Boolean?=false,
                 var day: Int?=null,
                 var start_day: String?=null,
                 var end_day: String?=null,
                 var memo: String?=null,
                 var stamp_id: String?=null,
                 var team: List<String>?=null,
                 var title: String?=null)

public data class Notification (var friend: Boolean ?= false,
                    var goal: Boolean ?= false,
                    var message: String ?= null,
                    var name:String ?= null,
                    var profile_color:String ?= "#f8c8c4")