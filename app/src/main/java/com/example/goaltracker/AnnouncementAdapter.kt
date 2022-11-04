package com.example.goaltracker

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class AnnouncementAdapter(private val context: Context) : RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {
    var announcementDatas = ArrayList<AnnouncementData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.announcement_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementAdapter.ViewHolder, position: Int) {
        val announcement = announcementDatas[position]

        holder.apply {
            bind(announcement)
        }
    }

    override fun getItemCount(): Int = announcementDatas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val announcement_layout: LinearLayout = itemView.findViewById(R.id.announcement_layout)
        private val announcement_subject: TextView = itemView.findViewById(R.id.announcement_subject)
        private val announcement_content: TextView = itemView.findViewById(R.id.announcement_content)
        private val announcement_createDate: TextView = itemView.findViewById(R.id.announcement_createDate)

        fun bind(announcement: AnnouncementData) {
            announcement_subject.text = announcement.subject
            announcement_content.text = announcement.content
            announcement_createDate.text = announcement.createDate

            announcement_layout.setOnClickListener {
                if (announcement_content.visibility == View.GONE) {
                    announcement_content.visibility = View.VISIBLE
                } else {
                    announcement_content.visibility = View.GONE
                }
            }
        }
    }

}