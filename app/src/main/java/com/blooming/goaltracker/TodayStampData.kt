package com.blooming.goaltracker

// 추후 아이디 혹은 번호 등 구분 데이터 추가하기
data class TodayStampData(
    val stamp_id : String,
    val num : Int,
    val nickname : String,
    val theme : String,
    val comment : String,
    val image : String,
    val type : Boolean
)
