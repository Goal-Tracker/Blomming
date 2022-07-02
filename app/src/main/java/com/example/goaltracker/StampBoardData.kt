package com.example.goaltracker

data class StampBoardData(
    val goal_id: String,
    val num: Int,
    val stamp: Boolean,
    val participateNum: Int,
    val stampNum: Int,
    val stampThemeList: Array<Int>
)