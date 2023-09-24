package com.blooming.goaltracker

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class GoalRecordAdapter(private val context: Context) : RecyclerView.Adapter<GoalRecordAdapter.ViewHolder>() {

    var goalDatas = ArrayList<GoalRecordData>()

    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    var accountUId = firebaseAuth?.currentUser?.uid.toString()
    private lateinit var dialog: DeleteGoal  //다이얼로그

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

                val params = goal_progressBar.layoutParams as ConstraintLayout.LayoutParams
                params.endToStart = R.id.endGoal_textView
                goal_progressBar.layoutParams = params
                goal_progressBar.requestLayout()
            } else {
                endGoal_textView.visibility = View.GONE

                goalToadyDate_textView.visibility = View.VISIBLE
                slash_textView.visibility = View.VISIBLE
                goalDate_textView.visibility = View.VISIBLE

                goalToadyDate_textView.text = Data.todayNum.toString() + '일'
                goalDate_textView.text = Data.stampNum.toString() + '일'

                val params = goal_progressBar.layoutParams as ConstraintLayout.LayoutParams
                params.endToStart = R.id.goalToadyDate_textView
                goal_progressBar.layoutParams = params
                goal_progressBar.requestLayout()
            }
            goal_progressBar.max = Data.stampNum
            goal_progressBar.progress = Data.todayNum

            view.setOnClickListener(listener)


            ///////////////////////////-----골 삭제----///////////////////////////////

            // 길게 클릭하면 다이얼로그 생성
            view.setOnLongClickListener {
                DeleteGoal(view.context, "${goalTitle_textView.text}")
                false // 직후 click event 를 받기 위해 false 반환
            }
        }

        // 해당 position 에 해당하는 데이터 삭제
        val deletebtnListener = View.OnClickListener {
            val goal = goalDatas[position]
            val userUID = db!!.collection("Goal").document(goal.goalId)
            userUID.addSnapshotListener { snapshot, e ->
                val stampId = snapshot?.get("stampId").toString()
                // Account의 Goal 정보 삭제
                db?.collection("Account")?.document(accountUId)
                    ?.update("myGoalList", FieldValue.arrayRemove(goal.goalId))

                // Goal 삭제
                //db?.collection("Goal")?.document(goal.goalId).delete()

                // Stamp 삭제
                //db?.collection("Stamp")?.document(stampId).delete()
            }
            dialog?.dismiss()
        }

        val cancelbtnListener = View.OnClickListener {
            dialog?.dismiss()
        }

        fun DeleteGoal(context: Context, text: String){
            dialog = DeleteGoal(
                context = context,
                title = text,
                deletebtnListener = deletebtnListener,
                cancelbtnListener = cancelbtnListener
            )

            //다이얼로그 타이틀 및 테두리 없애기
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.onBackPressed()
            dialog.show()
        }
    }
}


