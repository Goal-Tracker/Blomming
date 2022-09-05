package com.example.goaltracker

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager


class StampBoardAdapter(private val context: Context) : RecyclerView.Adapter<StampBoardAdapter.ViewHolder>() {

    var stampDatas = ArrayList<StampBoardData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.stamp_num_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stamp = stampDatas[position]

        val listener = View.OnClickListener {
            val bottomSheet = StampBottomSheetFragment(stamp)
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            bottomSheet.show(manager, bottomSheet.tag)
        }

        holder.apply {
            bind(listener, stamp)
        }
    }

    override fun getItemCount(): Int = stampDatas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var view: View = view

        // 없을 때 변수
        private val none_stamp_layout: ConstraintLayout = itemView.findViewById(R.id.none_stamp_layout)
        private val none_stampNum_view: View = itemView.findViewById(R.id.none_stampNum_view)
        private val none_stampNum_textView: TextView = itemView.findViewById(R.id.none_stampNum_textView)

        var none_stampNum_bgShape : GradientDrawable = none_stampNum_view.background as GradientDrawable

        // 한 명 했을 때 변수
        private val one_stamp_layout: ConstraintLayout = itemView.findViewById(R.id.one_stamp_layout)
        private val one_stampNum_view: View = itemView.findViewById(R.id.one_stampNum_view)
        private val one_member_view: View = itemView.findViewById(R.id.one_member_view)

        var one_stamp_bgShape : GradientDrawable = one_stampNum_view.background as GradientDrawable
        var one_memeber_bgShape : GradientDrawable = one_member_view.background as GradientDrawable


        // 두 명 했을 때 변수
        private val two_stamp_layout: ConstraintLayout = itemView.findViewById(R.id.two_stamp_layout)
        private val two_stampNum_view: View = itemView.findViewById(R.id.two_stampNum_view)
        private val two_memeber1_view: View = itemView.findViewById(R.id.two_memeber1_view)
        private val two_memeber2_view: View = itemView.findViewById(R.id.two_memeber2_view)

        var two_stamp_bgShape : GradientDrawable = two_stampNum_view.background as GradientDrawable
        var two_memeber1_bgShape : GradientDrawable = two_memeber1_view.background as GradientDrawable
        var two_memeber2_bgShape : GradientDrawable = two_memeber2_view.background as GradientDrawable


        // 세 명 했을 때 변수
        private val three_stamp_layout: ConstraintLayout = itemView.findViewById(R.id.three_stamp_layout)
        private val three_stampNum_view: View = itemView.findViewById(R.id.three_stampNum_view)
        private val three_memeber1_view: View = itemView.findViewById(R.id.three_memeber1_view)
        private val three_memeber2_view: View = itemView.findViewById(R.id.three_memeber2_view)
        private val three_memeber3_view: View = itemView.findViewById(R.id.three_memeber3_view)

        var three_stamp_bgShape : GradientDrawable = three_stampNum_view.background as GradientDrawable
        var three_memeber1_bgShape : GradientDrawable = three_memeber1_view.background as GradientDrawable
        var three_memeber2_bgShape : GradientDrawable = three_memeber2_view.background as GradientDrawable
        var three_memeber3_bgShape : GradientDrawable = three_memeber3_view.background as GradientDrawable


        // 네 명 했을 때 변수
        private val four_stamp_layout: ConstraintLayout = itemView.findViewById(R.id.four_stamp_layout)
        private val four_stampNum_view: View = itemView.findViewById(R.id.four_stampNum_view)
        private val four_memeber1_view: View = itemView.findViewById(R.id.four_memeber1_view)
        private val four_memeber2_view: View = itemView.findViewById(R.id.four_memeber2_view)
        private val four_memeber3_view: View = itemView.findViewById(R.id.four_memeber3_view)
        private val four_memeber4_view: View = itemView.findViewById(R.id.four_memeber4_view)

        var four_stamp_bgShape : GradientDrawable = four_stampNum_view.background as GradientDrawable
        var four_memeber1_bgShape : GradientDrawable = four_memeber1_view.background as GradientDrawable
        var four_memeber2_bgShape : GradientDrawable = four_memeber2_view.background as GradientDrawable
        var four_memeber3_bgShape : GradientDrawable = four_memeber3_view.background as GradientDrawable
        var four_memeber4_bgShape : GradientDrawable = four_memeber4_view.background as GradientDrawable


        // 다섯 명 했을 때 변수
        private val five_stamp_layout: ConstraintLayout = itemView.findViewById(R.id.five_stamp_layout)
        private val five_stampNum_view: View = itemView.findViewById(R.id.five_stampNum_view)
        private val five_memeber1_view: View = itemView.findViewById(R.id.five_memeber1_view)
        private val five_memeber2_view: View = itemView.findViewById(R.id.five_memeber2_view)
        private val five_memeber3_view: View = itemView.findViewById(R.id.five_memeber3_view)
        private val five_memeber4_view: View = itemView.findViewById(R.id.five_memeber4_view)
        private val five_memeber5_view: View = itemView.findViewById(R.id.five_memeber5_view)

        var five_stamp_bgShape : GradientDrawable = five_stampNum_view.background as GradientDrawable
        var five_memeber1_bgShape : GradientDrawable = five_memeber1_view.background as GradientDrawable
        var five_memeber2_bgShape : GradientDrawable = five_memeber2_view.background as GradientDrawable
        var five_memeber3_bgShape : GradientDrawable = five_memeber3_view.background as GradientDrawable
        var five_memeber4_bgShape : GradientDrawable = five_memeber4_view.background as GradientDrawable
        var five_memeber5_bgShape : GradientDrawable = five_memeber5_view.background as GradientDrawable


        fun bind(listener: View.OnClickListener, Data: StampBoardData) {

            Log.d(TAG, "Data info : $Data")


            // 한 명 골 인증
            if (Data.stampNum == 1) {
                Log.d(TAG, "one stampNum : ${Data.stampNum}")
                Log.d(TAG, "one stampNum : ${Data.participateNum}")

                if (Data.participateNum == 1){
                    none_stamp_layout.visibility = GONE
                    one_stamp_layout.visibility = VISIBLE
                    two_stamp_layout.visibility = GONE
                    three_stamp_layout.visibility = GONE
                    four_stamp_layout.visibility = GONE
                    five_stamp_layout.visibility = GONE

                    one_member_view.visibility = GONE

                    one_stamp_bgShape.setColor(Color.parseColor(Data.stampThemeList[0]))


                } else {
                    none_stamp_layout.visibility = GONE
                    one_stamp_layout.visibility = VISIBLE
                    two_stamp_layout.visibility = GONE
                    three_stamp_layout.visibility = GONE
                    four_stamp_layout.visibility = GONE
                    five_stamp_layout.visibility = GONE

                    one_member_view.visibility = VISIBLE

                    one_stamp_bgShape.setColor(ContextCompat.getColor(context, R.color.white))
                    one_memeber_bgShape.setColor(Color.parseColor(Data.stampThemeList[0]))
                }


            } else if (Data.stampNum == 2) { // 두 명 골 인증
                Log.d(TAG, "two stampNum : ${Data.stampNum}")

                none_stamp_layout.visibility = GONE
                one_stamp_layout.visibility = GONE
                two_stamp_layout.visibility = VISIBLE
                three_stamp_layout.visibility = GONE
                four_stamp_layout.visibility = GONE
                five_stamp_layout.visibility = GONE

                two_stamp_bgShape.setColor(ContextCompat.getColor(context, R.color.white))
                two_memeber1_bgShape.setColor(Color.parseColor(Data.stampThemeList[0]))
                two_memeber2_bgShape.setColor(Color.parseColor(Data.stampThemeList[1]))


            } else if (Data.stampNum == 3) {  // 세 명 골 인증
                Log.d(TAG, "three stampNum : ${Data.stampNum}")

                none_stamp_layout.visibility = GONE
                one_stamp_layout.visibility = GONE
                two_stamp_layout.visibility = GONE
                three_stamp_layout.visibility = VISIBLE
                four_stamp_layout.visibility = GONE
                five_stamp_layout.visibility = GONE

                three_stamp_bgShape.setColor(ContextCompat.getColor(context, R.color.white))
                three_memeber1_bgShape.setColor(Color.parseColor(Data.stampThemeList[0]))
                three_memeber2_bgShape.setColor(Color.parseColor(Data.stampThemeList[1]))
                three_memeber3_bgShape.setColor(Color.parseColor(Data.stampThemeList[2]))

            } else if (Data.stampNum == 4) {  // 네 명 골 인증
                Log.d(TAG, "four stampNum : ${Data.stampNum}")

                none_stamp_layout.visibility = GONE
                one_stamp_layout.visibility = GONE
                two_stamp_layout.visibility = GONE
                three_stamp_layout.visibility = GONE
                four_stamp_layout.visibility = VISIBLE
                five_stamp_layout.visibility = GONE

                four_stamp_bgShape.setColor(ContextCompat.getColor(context, R.color.white))
                four_memeber1_bgShape.setColor(Color.parseColor(Data.stampThemeList[0]))
                four_memeber2_bgShape.setColor(Color.parseColor(Data.stampThemeList[1]))
                four_memeber3_bgShape.setColor(Color.parseColor(Data.stampThemeList[2]))
                four_memeber4_bgShape.setColor(Color.parseColor(Data.stampThemeList[3]))


            } else if (Data.stampNum == 5) {  // 네 명 골 인증
                Log.d(TAG, "four stampNum : ${Data.stampNum}")

                none_stamp_layout.visibility = GONE
                one_stamp_layout.visibility = GONE
                two_stamp_layout.visibility = GONE
                three_stamp_layout.visibility = GONE
                four_stamp_layout.visibility = GONE
                five_stamp_layout.visibility = VISIBLE

                five_stamp_bgShape.setColor(ContextCompat.getColor(context, R.color.white))
                five_memeber1_bgShape.setColor(Color.parseColor(Data.stampThemeList[0]))
                five_memeber2_bgShape.setColor(Color.parseColor(Data.stampThemeList[1]))
                five_memeber3_bgShape.setColor(Color.parseColor(Data.stampThemeList[2]))
                five_memeber4_bgShape.setColor(Color.parseColor(Data.stampThemeList[3]))
                five_memeber5_bgShape.setColor(Color.parseColor(Data.stampThemeList[4]))


            } else {
                //0 명 골 인증
                none_stamp_layout.visibility = VISIBLE
                one_stamp_layout.visibility = GONE
                two_stamp_layout.visibility = GONE
                three_stamp_layout.visibility = GONE
                four_stamp_layout.visibility = GONE
                five_stamp_layout.visibility = GONE

                if (Data.stamp == true) {
                    none_stampNum_bgShape.setColor(ContextCompat.getColor(context, R.color.stamp_item))
                    none_stampNum_textView.visibility = VISIBLE
                    none_stampNum_textView.text = Data.num.toString()
                } else {
                    none_stampNum_bgShape.setColor(ContextCompat.getColor(context, R.color.white))
                    none_stampNum_textView.visibility = GONE
                }

            }

            view.setOnClickListener(listener)
        }
    }
}