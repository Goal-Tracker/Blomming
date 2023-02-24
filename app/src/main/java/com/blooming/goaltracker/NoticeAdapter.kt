package com.blooming.goaltracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoticeAdapter (private val context:Context) : RecyclerView.Adapter<NoticeAdapter.ViewHolder>(){

    private var noticeDto = mutableListOf<Notifications>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notice_layer, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noticeDto[position])
    }

    override fun getItemCount(): Int = noticeDto.size

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val noticeName : TextView = itemView.findViewById(R.id.notice_profile_name)
        val noticeContent : TextView = itemView.findViewById(R.id.notice_text)
        val deleteButton : Button = itemView.findViewById(R.id.notice_delete_button)
        val noticeTitle : TextView = itemView.findViewById(R.id.notice_title)

        fun bind(item: Notifications) {
            noticeName.text = item.userName
            noticeContent.text = item.message
        }
    }

}