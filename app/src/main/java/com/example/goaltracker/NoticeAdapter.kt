package com.example.goaltracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoticeAdapter (private val context:Context) : RecyclerView.Adapter<NoticeAdapter.ViewHolder>(){

    private var noticeList = mutableListOf<Notification>()

    fun setListData(data:MutableList<Notification>){
        noticeList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notice_layer, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.notice_profile_name)
        val content : TextView = itemView.findViewById(R.id.notice_text)

    }


}