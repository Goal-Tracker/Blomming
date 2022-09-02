package com.example.goaltracker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class TodayStampAdapter (private val context: Context) : RecyclerView.Adapter<TodayStampAdapter.ViewHolder>() {

    var todayStampDatas = ArrayList<TodayStampData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayStampAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.today_stamp_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodayStampAdapter.ViewHolder, position: Int) {
        val stamp = todayStampDatas[position]

        val listener = View.OnClickListener {
            Toast.makeText(it.context, "Clicked : ${stamp.nickname}", Toast.LENGTH_SHORT).show()
        }

        holder.apply {
            bind(listener, stamp)
        }
    }

    override fun getItemCount(): Int = todayStampDatas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var view: View = view

        private val certification_default_layout: ConstraintLayout = itemView.findViewById(R.id.certification_default_layout)
        private val certification_default_view: View = itemView.findViewById(R.id.certification_default_view)
        private val certification_default_textView: TextView = itemView.findViewById(R.id.certification_default_textView)
        var bgCertDefault : GradientDrawable = certification_default_view.background as GradientDrawable

        private val certification_imageView: ImageView = itemView.findViewById(R.id.certification_imageView)
        private val nickname_textView: TextView = itemView.findViewById(R.id.nickname_textView)
        private val comment_textView: TextView = itemView.findViewById(R.id.comment_textView)

        fun bind(listener: View.OnClickListener, Data: TodayStampData) {
            // 이미지 로드
            if (Data.image != "") {
                Glide.with(context)
                    .load(Data.image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(certification_imageView)
                certification_imageView.visibility = View.VISIBLE
                certification_imageView.clipToOutline = true

                certification_default_view.visibility = View.GONE
                certification_default_textView.visibility = View.GONE
            } else {
                bgCertDefault.setColor(Color.parseColor(Data.theme))
                certification_default_textView.text = Data.nickname
                certification_imageView.visibility = View.GONE
                certification_default_view.visibility = View.VISIBLE
                certification_default_textView.visibility = View.VISIBLE
            }

            nickname_textView.text = Data.nickname
            comment_textView.text = Data.comment

            view.setOnClickListener(listener)
        }
    }

}