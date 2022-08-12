package com.example.goaltracker

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlinx.android.synthetic.main.notice_view.*

class NoticeActivity:AppCompatActivity() {


    val noticeList = arrayListOf<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.notice_view)

        backtomain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}