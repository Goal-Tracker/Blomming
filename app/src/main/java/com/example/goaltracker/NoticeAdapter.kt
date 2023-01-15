package com.example.goaltracker

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoticeAdapter (private val context:Context) : RecyclerView.Adapter<NoticeAdapter.ViewHolder>(){

    private var noticeDto = mutableListOf<Notifications>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notice_layer, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeAdapter.ViewHolder, position: Int) {
        holder.bind(noticeDto[position])
    }

    override fun getItemCount(): Int = noticeDto.size

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val noticeName : TextView = itemView.findViewById(R.id.notice_profile_name)
        val noticeContent : TextView = itemView.findViewById(R.id.notice_text)
        val acceptButton : Button = itemView.findViewById(R.id.notice_button)
        val noticeTitle : TextView = itemView.findViewById(R.id.notice_title)

        fun bind(item: Notifications) {
            noticeName.text = item.userName
            noticeContent.text = item.message
        }
    }

}