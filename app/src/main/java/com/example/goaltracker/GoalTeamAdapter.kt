package com.example.goaltracker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GoalTeamAdapter (private val context: Context, private val goalTitle: String) : RecyclerView.Adapter<GoalTeamAdapter.ViewHolder>() {

    var teamDatas = ArrayList<GoalTeamData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_profile_item, parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var team = teamDatas[position]
        var dialog: PokeDialog = PokeDialog(context)

        val listener = View.OnClickListener {
            dialog.start(team, goalTitle)
        }

        holder.apply {
            bind(listener, team)
        }
    }

    override fun getItemCount(): Int = teamDatas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var view: View = view

        private val tv_profileName: TextView = itemView.findViewById(R.id.tv_profileName)
        private val view_profile: View = itemView.findViewById(R.id.view_profile)
        var bgShape : GradientDrawable = view_profile.background as GradientDrawable

        fun bind(listener: View.OnClickListener, Data: GoalTeamData) {
            tv_profileName.text = Data.name[0].toString()
            bgShape.setColor(Color.parseColor(Data.profileColor))

            view.setOnClickListener(listener)
        }
    }
}