package com.blooming.goaltracker

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class GoalRecordAdapter(private val context: Context) : RecyclerView.Adapter<GoalRecordAdapter.ViewHolder>() {

    var goalDatas = ArrayList<GoalRecordData>()

    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    var accountUId = firebaseAuth?.currentUser?.uid.toString()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_goal_record, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal = goalDatas[position]

        val listener = View.OnClickListener {
            val intent = Intent(holder.itemView?.context, StampBoardActivity::class.java)
            intent.putExtra("goalId", goal.goalId)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }

        holder.apply {
            bind(listener, goal)
        }
    }

    override fun getItemCount(): Int = goalDatas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var view: View = view

        private val goalTitle_textView: TextView = itemView.findViewById(R.id.goalTitle_textView)
        private val first_day_textView: TextView = itemView.findViewById(R.id.first_day_textView)
        private val last_day_textView: TextView = itemView.findViewById(R.id.last_day_textView)

        private val goal_progressBar: ProgressBar = itemView.findViewById(R.id.goal_progressBar)
        private val goalToadyDate_textView: TextView = itemView.findViewById(R.id.goalToadyDate_textView)
        private val slash_textView: TextView = itemView.findViewById(R.id.slash_textView)
        private val goalDate_textView: TextView = itemView.findViewById(R.id.goalDate_textView)
        private val endGoal_textView: TextView = itemView.findViewById(R.id.endGoal_textView)

        private val goal_member1_view: View = itemView.findViewById(R.id.goal_member1_view)
        private val goal_member1_textView: TextView = itemView.findViewById(R.id.goal_member1_textView)
        private val goal_member2_view: View = itemView.findViewById(R.id.goal_member2_view)
        private val goal_member2_textView: TextView = itemView.findViewById(R.id.goal_member2_textView)
        private val goal_member3_view: View = itemView.findViewById(R.id.goal_member3_view)
        private val goal_member3_textView: TextView = itemView.findViewById(R.id.goal_member3_textView)
        private val goal_member4_view: View = itemView.findViewById(R.id.goal_member4_view)
        private val goal_member4_textView: TextView = itemView.findViewById(R.id.goal_member4_textView)
        private val goal_member5_view: View = itemView.findViewById(R.id.goal_member5_view)
        private val goal_member5_textView: TextView = itemView.findViewById(R.id.goal_member5_textView)

        var member1_bgShape : GradientDrawable = goal_member1_view.background as GradientDrawable
        var member2_bgShape : GradientDrawable = goal_member2_view.background as GradientDrawable
        var member3_bgShape : GradientDrawable = goal_member3_view.background as GradientDrawable
        var member4_bgShape : GradientDrawable = goal_member4_view.background as GradientDrawable
        var member5_bgShape : GradientDrawable = goal_member5_view.background as GradientDrawable



        fun bind(listener: View.OnClickListener, Data: GoalRecordData) {

            if (Data.participateNum == 1) {
                goal_member1_view.visibility = View.VISIBLE
                goal_member1_textView.visibility = View.VISIBLE
                goal_member1_textView.text = Data.teamNameList[0][0].toString()
                member1_bgShape.setColor(Color.parseColor(Data.teamThemeList[0]))

                goal_member2_view.visibility = View.GONE
                goal_member2_textView.visibility = View.GONE
                goal_member3_view.visibility = View.GONE
                goal_member3_textView.visibility = View.GONE
                goal_member4_view.visibility = View.GONE
                goal_member4_textView.visibility = View.GONE
                goal_member5_view.visibility = View.GONE
                goal_member5_textView.visibility = View.GONE

            } else if (Data.participateNum == 2) {
                goal_member1_view.visibility = View.VISIBLE
                goal_member1_textView.visibility = View.VISIBLE
                goal_member1_textView.text = Data.teamNameList[0][0].toString()
                member1_bgShape.setColor(Color.parseColor(Data.teamThemeList[0]))

                goal_member2_view.visibility = View.VISIBLE
                goal_member2_textView.visibility = View.VISIBLE
                goal_member2_textView.text = Data.teamNameList[1][0].toString()
                member2_bgShape.setColor(Color.parseColor(Data.teamThemeList[1]))

                goal_member3_view.visibility = View.GONE
                goal_member3_textView.visibility = View.GONE
                goal_member4_view.visibility = View.GONE
                goal_member4_textView.visibility = View.GONE
                goal_member5_view.visibility = View.GONE
                goal_member5_textView.visibility = View.GONE

            } else if (Data.participateNum == 3) {
                goal_member1_view.visibility = View.VISIBLE
                goal_member1_textView.visibility = View.VISIBLE
                goal_member1_textView.text = Data.teamNameList[0][0].toString()
                member1_bgShape.setColor(Color.parseColor(Data.teamThemeList[0]))

                goal_member2_view.visibility = View.VISIBLE
                goal_member2_textView.visibility = View.VISIBLE
                goal_member2_textView.text = Data.teamNameList[1][0].toString()
                member2_bgShape.setColor(Color.parseColor(Data.teamThemeList[1]))

                goal_member3_view.visibility = View.VISIBLE
                goal_member3_textView.visibility = View.VISIBLE
                goal_member3_textView.text = Data.teamNameList[2][0].toString()
                member3_bgShape.setColor(Color.parseColor(Data.teamThemeList[2]))

                goal_member4_view.visibility = View.GONE
                goal_member4_textView.visibility = View.GONE
                goal_member5_view.visibility = View.GONE
                goal_member5_textView.visibility = View.GONE
            } else if (Data.participateNum == 4) {
                goal_member1_view.visibility = View.VISIBLE
                goal_member1_textView.visibility = View.VISIBLE
                goal_member1_textView.text = Data.teamNameList[0][0].toString()
                member1_bgShape.setColor(Color.parseColor(Data.teamThemeList[0]))

                goal_member2_view.visibility = View.VISIBLE
                goal_member2_textView.visibility = View.VISIBLE
                goal_member2_textView.text = Data.teamNameList[1][0].toString()
                member2_bgShape.setColor(Color.parseColor(Data.teamThemeList[1]))

                goal_member3_view.visibility = View.VISIBLE
                goal_member3_textView.visibility = View.VISIBLE
                goal_member3_textView.text = Data.teamNameList[2][0].toString()
                member3_bgShape.setColor(Color.parseColor(Data.teamThemeList[2]))

                goal_member4_view.visibility = View.VISIBLE
                goal_member4_textView.visibility = View.VISIBLE
                goal_member4_textView.text = Data.teamNameList[3][0].toString()
                member4_bgShape.setColor(Color.parseColor(Data.teamThemeList[3]))


                goal_member5_view.visibility = View.GONE
                goal_member5_textView.visibility = View.GONE

            } else if (Data.participateNum == 5) {
                goal_member1_view.visibility = View.VISIBLE
                goal_member1_textView.visibility = View.VISIBLE
                goal_member1_textView.text = Data.teamNameList[0][0].toString()
                member1_bgShape.setColor(Color.parseColor(Data.teamThemeList[0]))

                goal_member2_view.visibility = View.VISIBLE
                goal_member2_textView.visibility = View.VISIBLE
                goal_member2_textView.text = Data.teamNameList[1][0].toString()
                member2_bgShape.setColor(Color.parseColor(Data.teamThemeList[1]))

                goal_member3_view.visibility = View.VISIBLE
                goal_member3_textView.visibility = View.VISIBLE
                goal_member3_textView.text = Data.teamNameList[2][0].toString()
                member3_bgShape.setColor(Color.parseColor(Data.teamThemeList[2]))

                goal_member4_view.visibility = View.VISIBLE
                goal_member4_textView.visibility = View.VISIBLE
                goal_member4_textView.text = Data.teamNameList[3][0].toString()
                member4_bgShape.setColor(Color.parseColor(Data.teamThemeList[3]))


                goal_member5_view.visibility = View.VISIBLE
                goal_member5_textView.visibility = View.VISIBLE
                goal_member5_textView.text = Data.teamNameList[4][0].toString()
                member5_bgShape.setColor(Color.parseColor(Data.teamThemeList[4]))
            }

            goalTitle_textView.text = Data.title
            first_day_textView.text = Data.startDate
            last_day_textView.text = Data.endDate
            if (Data.todayNum > Data.stampNum) {
                endGoal_textView.visibility = View.VISIBLE

                goalToadyDate_textView.visibility = View.GONE
                slash_textView.visibility = View.GONE
                goalDate_textView.visibility = View.GONE
            } else {
                endGoal_textView.visibility = View.GONE

                goalToadyDate_textView.visibility = View.VISIBLE
                slash_textView.visibility = View.VISIBLE
                goalDate_textView.visibility = View.VISIBLE

                goalToadyDate_textView.text = Data.todayNum.toString() + '일'
                goalDate_textView.text = Data.stampNum.toString() + '일'
            }
            goal_progressBar.max = Data.stampNum
            goal_progressBar.progress = Data.todayNum

            view.setOnClickListener(listener)


            ///////// 골 삭제 ////////////////////////////////////////////////////////
            view.setOnLongClickListener {
                val goal = goalDatas[position]
                // 다이얼로그를 생성하기 위해 Builder 클래스 생성자를 이용해 줍니다.
                    val builder = AlertDialog.Builder(view.context)

                builder.setTitle("선택한 목표를 삭제하시겠습니까?")
                        .setMessage("${goalTitle_textView.text}")
                        .setPositiveButton("삭제",
                            DialogInterface.OnClickListener { dialog, id ->
                                // 해당 position 에 해당하는 데이터 삭제
                                db?.collection("Account")?.document(accountUId)
                                    ?.update("myGoalList", FieldValue.arrayRemove(goal.goalId))
                            })
                        .setNegativeButton("취소",
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog?.dismiss()
                            })
                    // 다이얼로그를 띄워주기
                    builder.show()

                false // 직후 click event 를 받기 위해 false 반환
            }
        }
    }
}