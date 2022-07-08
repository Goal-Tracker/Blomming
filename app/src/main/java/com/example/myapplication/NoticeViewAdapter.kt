package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class NoticeViewAdapter (val context: Context, val noticeList: ArrayList<Notification>) : BaseAdapter(){
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.notice_layer_basic, null)

        val notice_basic_text = view.findViewById<TextView>(R.id.notice_text)
        val notice_profile_name_basic = view.findViewById<TextView>(R.id.notice_profile_name_basic)
        val notice_profile = view.findViewById<ImageView>(R.id.notice_profile)

        val notice = noticeList[p0]


        return view
    }

    override fun getItem(p0: Int): Any {
        return noticeList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return noticeList.size
    }

}