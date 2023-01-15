package com.example.goaltracker

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StampBoardActivity() : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언

    lateinit var goalTeamAdapter: GoalTeamAdapter

    lateinit var stampBoardAdapter: StampBoardAdapter
    val teamDatas = ArrayList<GoalTeamData>()

    lateinit var goal_back_imageButton: ImageButton
    lateinit var edit_goal: ImageButton

    lateinit var goalTitle_textView : TextView
    lateinit var first_day_textView : TextView
    lateinit var last_day_textView : TextView
    lateinit var subtitle_textView : TextView
    lateinit var goalDate_textView : TextView
    lateinit var goal_progressBar : ProgressBar
    lateinit var goalToadyDate_textView : TextView

    lateinit var rv_stampBoard : RecyclerView
    lateinit var rv_team : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MySharedPreferences.getTheme(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stamp_board)

        edit_goal = findViewById(R.id.edit_goal)
        goal_back_imageButton = findViewById(R.id.goal_back_imageButton)

        goal_back_imageButton.setOnClickListener {
            finish()
        }

        goalTitle_textView = findViewById(R.id.goalTitle_textView)
        first_day_textView = findViewById(R.id.first_day_textView)
        last_day_textView = findViewById(R.id.last_day_textView)
        subtitle_textView = findViewById(R.id.subtitle_textView)
        goalDate_textView = findViewById(R.id.goalDate_textView)
        goal_progressBar = findViewById(R.id.goal_progressBar)
        goalToadyDate_textView = findViewById(R.id.goalToadyDate_textView)

        rv_team = findViewById(R.id.rv_team)
        rv_stampBoard = findViewById(R.id.rv_stampBoard)

        stampBoardAdapter = StampBoardAdapter(this)
        rv_stampBoard.adapter = stampBoardAdapter

        // var goal_id = "1c1e364c-b9b6-4c4e-b016-50a5909eb6b4"
        val goal_id = intent.getStringExtra("goalId") as String

        // 읽고 스탬프 추가 (테스트용)
        val goal_db = db.collection("Goal").document(goal_id)

        // ------------------------------------------------------------------------
        // 골 수정 화면으로 이동
        edit_goal.setOnClickListener {
            val intent = Intent(this, EditGoalActivity::class.java)
            intent.putExtra("goalID", goal_id) // 데이터 보내기
            startActivity(intent)
        }
        //------------------------------------------------------------------------

        goal_db.addSnapshotListener { snapshot, e ->
            val goal_day = snapshot?.get("day").toString().toInt()
            val start_day = snapshot?.get("startDay").toString()
            val start_day_str = start_day.replace("-", ".")
            val end_day = snapshot?.get("endDay").toString()
            val end_day_str = end_day.replace("-", ".")
            val stamp_id = snapshot?.get("stampId") as String
            val past_date = pastCalc(start_day);

            val title = snapshot.get("title") as String
            val subtitle = snapshot.get("memo") as String

            goalTeamAdapter = GoalTeamAdapter(this, title)
            rv_team.adapter = goalTeamAdapter

            goalTitle_textView.text = title
            first_day_textView.text = start_day_str
            last_day_textView.text = end_day_str
            subtitle_textView.text = subtitle
            goalDate_textView.text = goal_day.toString() + "일"
            if (past_date > goal_day) {
                goalToadyDate_textView.text = "종료"
            } else {
                goalToadyDate_textView.text = past_date.toString() + "일"
            }
            goal_progressBar.max = goal_day
            goal_progressBar.progress = past_date

            var start_date = start_day + " 00:00:00"
            var sf = SimpleDateFormat("yyyy-MM-dd 00:00:00")
            var date = sf.parse(start_date)
            var today = Calendar.getInstance()

            var calcDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)

            val pastDate = (calcDate + 1).toInt()

            db.collection("Goal").document(goal_id).collection("team")
                .whereEqualTo("request", true)
                .get()
                .addOnSuccessListener { result ->
                    val participateNum = result.size()

                    // 판에 스탬프 개수 초기화
                    val stamp_db = db.collection("Stamp").document(stamp_id)
                    stamp_db.addSnapshotListener { stamp_snapshot, e ->
                        try {
                            val stampDatas = ArrayList<StampBoardData>()

                            for (i in 1..goal_day) {
                                val notYet: Boolean = pastDate <= i

                                val dayRecord = stamp_snapshot?.get("dayRecord") as HashMap<String, List<HashMap<String, String>>>
                                if (dayRecord["day$i"] != null) {
                                    var commentArray = dayRecord["day$i"] as List<HashMap<String, String>>
                                    var themeArray = ArrayList<String>()
                                    var commentNum = commentArray.size

                                    if (commentNum > 0) {
                                        for (commentInfo in commentArray) {
                                            themeArray.add(commentInfo["userColor"].toString())
                                        }

                                        stampDatas.add(
                                            StampBoardData(
                                                goal_id = goal_id,
                                                num = i,
                                                stamp = notYet,
                                                participateNum = participateNum,
                                                stampNum = themeArray.size,
                                                stampThemeList = themeArray
                                            )
                                        )
                                    }
                                } else {
                                    stampDatas.add(
                                        StampBoardData(
                                            goal_id = goal_id,
                                            num = i,
                                            stamp = notYet,
                                            participateNum = participateNum,
                                            stampNum = 0,
                                            stampThemeList = ArrayList<String>()
                                        )
                                    )
                                }
                            }

                            stampDatas.sortBy { it.num }
                            stampBoardAdapter.stampDatas = stampDatas
                            stampBoardAdapter.notifyDataSetChanged()
                        } catch (e: Exception) {
                            Log.d(TAG, "[Error] $e")
                        }
                    }
                }

            // 팀원
            db.collection("Goal").document(goal_id).collection("team")
                .whereEqualTo("request", true)
                .get()
                .addOnSuccessListener { result ->
                    for (member in result) {
                        val uid: String = member.get("uid").toString()

                        db.collection("Account").document(uid).get().addOnSuccessListener {
                            val nickname: String = it.get("userName").toString()
                            val theme: String = it.get("userColor").toString()
                            val message: String = it.get("userMessage").toString()

                            teamDatas.add(
                                GoalTeamData(
                                    uid = uid,
                                    name = nickname,
                                    profileColor = theme,
                                    message = message,
                                    request = true
                                )
                            )

                            Log.d("teamDatas result : ", teamDatas.toString())

                            teamDatas.distinct()
                            goalTeamAdapter.teamDatas = teamDatas
                            goalTeamAdapter.notifyDataSetChanged()

                        }
                    }
                }
        }

    }

    private fun pastCalc(first_day: String): Int {
        var today = Calendar.getInstance()

        var first_date = first_day + " 00:00:00"
        var sf = SimpleDateFormat("yyyy-MM-dd 00:00:00")
        var date = sf.parse(first_date)

        var calcDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)

        return (calcDate + 1).toInt()
    }
}