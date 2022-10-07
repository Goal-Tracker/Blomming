package com.example.goaltracker

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ListAdapter
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.drawer_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val db = FirebaseFirestore.getInstance()

    lateinit var goalRecordOngoingAdapter: GoalRecordAdapter
    val onGoingGoalDatas = ArrayList<GoalRecordData>()

    private lateinit var rv_goal : RecyclerView

    //어댑터 연결
    //private lateinit var adapter: MainAdapter
    // goal view 모델 가져오기
    //private val viewModel by lazy { ViewModelProvider(this).get(ListViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_main)

        setSupportActionBar(main_toolbar) //툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayShowTitleEnabled(false)  //툴바에 타이틀 안보이게

        //네비게이션 드로어 내에 있는 화면의 이벤트를 처리하기 위해 생성
        nav_view.setNavigationItemSelectedListener(this) //Navigation 리스너

        //adapter = MainAdapter(this)

//        val recyclerView : RecyclerView = findViewById(R.id.goal_recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter=adapter
//        observerData()

        alarmButton.setOnClickListener {
            startActivity(Intent(this, NoticeActivity::class.java))
        }

        menubtn.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.END)
        }


        rv_goal = findViewById(R.id.rv_goal)

        goalRecordOngoingAdapter = GoalRecordAdapter(this)
        rv_goal.adapter = goalRecordOngoingAdapter

        // 추후엔 Dataframe에서 가져다 사용하기
        val temp_goal_lsit = arrayOf("a6jyD0k2MSJliDJq1wHb", "IyJXNQPcIx2a5EzLUoeN")

        temp_goal_lsit.forEach { goal_id ->
            Log.d(ContentValues.TAG, "goal id : $goal_id")
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

                if (past_date <= goal_day) {
                    // 데이터 한 번만 가져오기
                    db.collection("Goal").document(goal_id).collection("team")
                        .whereEqualTo("request", true)
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                teamNameList.add(document["userName"].toString())
                                teamThemeList.add(document["profileColor"].toString())
                            }

                            onGoingGoalDatas.add(
                                GoalRecordData(
                                    goalId = goal_id,
                                    title = title,
                                    participateNum = result.size(),
                                    startDate = start_day_str,
                                    endDate = end_day_str,
                                    todayNum = past_date,
                                    stampNum = goal_day,
                                    teamNameList = teamNameList,
                                    teamThemeList = teamThemeList
                                )
                            )

                            goalRecordOngoingAdapter.goalDatas = onGoingGoalDatas
                            goalRecordOngoingAdapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                        }
                }
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_goalAchieve-> Toast.makeText(this, "친구목록 클릭됨", Toast.LENGTH_SHORT).show()
            R.id.nav_friendList-> Toast.makeText(this, "친구목록 클릭됨", Toast.LENGTH_SHORT).show()
            R.id.nav_settings-> {
                val dialog = CustomDialog(this)
                dialog.showDialog()
                dialog.setOnClickListener(object: CustomDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        Toast.makeText(this@MainActivity, "프로필 변경됨", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            R.id.nav_notice-> Toast.makeText(this, "공지사항 클릭됨", Toast.LENGTH_SHORT).show()
            R.id.nav_logOut-> Toast.makeText(this, "로그아웃 클릭됨", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawers()
            Toast.makeText(this, "back btn clicked", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
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


//    fun observerData() {
//        viewModel.fetchData().observe(this, Observer {
//            adapter.setListData(it)
//            adapter.notifyDataSetChanged()
//        })
//    }

}

