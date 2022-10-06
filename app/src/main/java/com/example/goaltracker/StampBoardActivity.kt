package com.example.goaltracker

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StampBoardActivity() : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언

    lateinit var goalTeamAdapter: GoalTeamAdapter

    lateinit var stampBoardAdapter: StampBoardAdapter
    val teamDatas = ArrayList<GoalTeamData>()

    lateinit var goal_back_imageButton: ImageButton
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stamp_board)

        // uid 저장
        MySharedPreferences.setUserId(this, "QL5QEcUUl5QKxKWOKQ2J")
        MySharedPreferences.setUserNickname(this, "임정수")
        MySharedPreferences.setUserColor(this, "#fcdcce")

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

        val goal_id = intent.getStringExtra("goalId") as String

        // 읽고 스탬프 추가 (테스트용)
        val goal_db = db.collection("Goal").document(goal_id)

        goal_db.addSnapshotListener { snapshot, e ->
            val goal_day = snapshot?.get("day").toString().toInt()
            val start_day = snapshot?.get("startDay").toString()
            val start_day_str = start_day.replace("-", ".")
            val end_day = snapshot?.get("endDay").toString()
            val end_day_str = end_day.replace("-", ".")
//            val teamList = snapshot?.get("Team") as List<String>
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

            Log.d("goal_db", "Date : $goal_day")
//            Log.d("goal_db", "teamList : $teamList")
            Log.d("goal_db", "stamp_id : $stamp_id")

            var start_date = start_day + " 00:00:00"
            var sf = SimpleDateFormat("yyyy-MM-dd 00:00:00")
            var date = sf.parse(start_date)
            var today = Calendar.getInstance()

            var calcDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)

            val pastDate = (calcDate+1).toInt()

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

                                val day_record = stamp_snapshot?.get("Day_record") as HashMap<String, List<HashMap<String, String>>>
                                Log.d(TAG, "[day_record[\"Day$i\"]] ${day_record["Day$i"]}")
                                if (day_record["Day$i"] != null){
                                    Log.d(TAG, "[day_record[\"Day$i\"]] inner ${day_record["Day$i"]}")
                                    var commentArray = day_record["Day$i"] as List<HashMap<String, String>>
                                    var themeArray = ArrayList<String>()
                                    var commentNum = commentArray.size

                                    Log.d(TAG, "[day_record[\"Day$i\"]] not null :  ${commentArray}")

                                    if (commentNum > 0) {
                                        Log.d(
                                            TAG,
                                            "[day_record[\"Day$i\"]] commentNum > 0 :  ${commentArray}"
                                        )

                                        for (commentInfo in commentArray) {
                                            Log.d(
                                                TAG,
                                                "[day_record[\"Day$i\"]] commentInfo :  ${commentInfo}"
                                            )

                                            if (commentInfo["Type"] as Boolean) {
                                                themeArray.add(commentInfo["UserColor"].toString())
                                                Log.d(TAG, "$i : [themeArray] $themeArray")
                                            }
                                        }

                                        Log.d(TAG, "$i : [inner themeArray] $themeArray")

                                        Log.d(TAG, "comment not null num : $i")
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
                                    Log.d(ContentValues.TAG, "[day_record[\"Day$i\"]] is empty")
                                    Log.d(ContentValues.TAG, "comment null num : $i")

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

                            Log.d("stampData result inner : ", stampDatas.toString())

                            stampDatas.sortBy { it.num }
                            stampBoardAdapter.stampDatas = stampDatas
                            stampBoardAdapter.notifyDataSetChanged()
                        } catch (e: Exception) {
                            Log.d(ContentValues.TAG, "[Error] $e")
                        }
                }
            }

            // 팀원
            db.collection("Goal").document(goal_id).collection("team")
                .whereEqualTo("request", true)
                .get()
                .addOnSuccessListener { result ->
                    for (member in result){
                        Log.d("Add stamp member : ", member.toString())

                        val uid: String = member.get("uid").toString()
                        val nickname: String = member.get("userName").toString()
                        val theme: String = member.get("profileColor").toString()

                        teamDatas.add(GoalTeamData(uid = uid, name = nickname, profileColor = theme))

                        Log.d("teamDatas result : ", teamDatas.toString())

                        teamDatas.distinct()
                        goalTeamAdapter.teamDatas = teamDatas
                        goalTeamAdapter.notifyDataSetChanged()
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

        Log.d("test: 날짜!!", "$calcDate 일 차이남!!")

        return (calcDate+1).toInt()
    }
}