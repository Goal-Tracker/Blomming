package com.example.goaltracker

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GoalRecordFinishedFragment : Fragment() {
    val db = FirebaseFirestore.getInstance()

    lateinit var goalRecordOngoingAdapter: GoalRecordAdapter

    val finishedGoalDatas = ArrayList<GoalRecordData>()

    private lateinit var rv_goal : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_goal_record_finished, container, false)

        rv_goal = view.findViewById(R.id.rv_goal)

        goalRecordOngoingAdapter = GoalRecordAdapter(requireContext())
        rv_goal.adapter = goalRecordOngoingAdapter

        // 추후엔 Dataframe에서 가져다 사용하기

        db.collection("Account").document(MySharedPreferences.getUserId(requireContext())).get().addOnSuccessListener {
            val curUser = it.toObject(Account::class.java)!!
            curUser.myGoalList?.forEach { goal_id ->
                Log.d(TAG, "goal id : $goal_id")
                val goal_db = db.collection("Goal").document(goal_id)

                goal_db.addSnapshotListener { snapshot, e ->
                    var teamNameList = arrayListOf<String>()
                    var teamThemeList = arrayListOf<String>()
                    val goal_day = snapshot?.get("day").toString().toInt()
                    val start_day = snapshot?.get("startDay").toString()
                    val start_day_str = start_day.replace("-", ".")
                    val end_day = snapshot?.get("endDay").toString()
                    val end_day_str = end_day.replace("-", ".")
                    val past_date = pastCalc(start_day);

                    val title = snapshot?.get("title") as String

                    if (past_date > goal_day) {
                        // 데이터 한 번만 가져오기
                        db.collection("Goal").document(goal_id).collection("team")
                            .whereEqualTo("request", true)
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    teamNameList.add(document["userName"].toString())
                                    teamThemeList.add(document["profileColor"].toString())
                                }

                                finishedGoalDatas.add(
                                    GoalRecordData(
                                        goalId =  goal_id,
                                        title =  title,
                                        participateNum = result.size(),
                                        startDate = start_day_str,
                                        endDate = end_day_str,
                                        todayNum = past_date,
                                        stampNum = goal_day,
                                        teamNameList = teamNameList,
                                        teamThemeList = teamThemeList
                                    )
                                )

                                goalRecordOngoingAdapter.goalDatas = finishedGoalDatas
                                goalRecordOngoingAdapter.notifyDataSetChanged()
                            }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                            }
                    }

                }
            }
        }

        return view
    }

    private fun pastCalc(first_day: String): Int {
        var today = Calendar.getInstance()

        var first_date = first_day + " 00:00:00"
        var sf = SimpleDateFormat("yyyy-MM-dd 00:00:00")
        var date = sf.parse(first_date)

        var calcDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)

        Log.d("test: 날짜!!", "$calcDate 일 차이남!!")

        return (calcDate+1).toInt()
    }
}