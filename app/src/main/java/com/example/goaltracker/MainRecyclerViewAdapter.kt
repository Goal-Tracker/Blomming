package com.example.goaltracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainRecyclerViewAdapterAdapter(private val context:Context) : RecyclerView.Adapter<MainRecyclerViewAdapterAdapter.ViewHolder>() {

    private var goalList = mutableListOf<ModelGoals>()

    fun setListData(data:MutableList<ModelGoals>){
        goalList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.goals_layer, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal:ModelGoals = goalList[position]
        holder.title.text = goal.GoalName
        holder.startDay.text = goal.first_day
        holder.endDay.text = goal.last_day
//        holder.nowDay.text = "5"
//        holder.totalDay.text = goal.
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.goal_title)
        val startDay : TextView = itemView.findViewById(R.id.goal_start)
        val endDay : TextView = itemView.findViewById(R.id.goal_end)
        val nowDay : TextView = itemView.findViewById(R.id.goal_now)
        val totalDay : TextView = itemView.findViewById(R.id.goal_day)
    }
}