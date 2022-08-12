package com.example.goaltracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//class MainAdapter(private val context:Context) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
//
//    private var goalList = mutableListOf<Goal>()
//
//    fun setListData(data:MutableList<Goal>){
//        goalList = data
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.goals_layer, parent, false)
//
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val goal:Goal = goalList[position]
//        holder.title.text = goal.title
//        holder.startDay.text = goal.start_day
//        holder.endDay.text = goal.end_day
//        holder.nowDay.text = "5"
//        holder.totalDay.text = goal.day.toString()
//    }
//
//    override fun getItemCount(): Int {
//        return goalList.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val title : TextView = itemView.findViewById(R.id.goal_title)
//        val startDay : TextView = itemView.findViewById(R.id.goal_start)
//        val endDay : TextView = itemView.findViewById(R.id.goal_end)
//        val nowDay : TextView = itemView.findViewById(R.id.goal_now)
//        val totalDay : TextView = itemView.findViewById(R.id.goal_day)
//    }
//}