package com.example.goaltracker

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.drawer_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth =FirebaseAuth.getInstance()

    lateinit var goalRecordOngoingAdapter: GoalRecordAdapter
    val onGoingGoalDatas = ArrayList<GoalRecordData>()

    private lateinit var rv_goal : RecyclerView

    var curUser = Account()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_main)

        var accountUId : String?=""
        accountUId = firebaseAuth?.currentUser?.uid.toString()
        //accountUId = "1k8mYTUpqKVAlHBMM6sxBckaeP13"
        val curUserName = findViewById<TextView>(R.id.user_name)

        setSupportActionBar(main_toolbar) //툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayShowTitleEnabled(false)  //툴바에 타이틀 안보이게

        //네비게이션 드로어 내에 있는 화면의 이벤트를 처리하기 위해 생성
        nav_view.setNavigationItemSelectedListener(this) //Navigation 리스너

        val nav_header = nav_view.getHeaderView(0)
        val navUserName = nav_header.findViewById<TextView>(R.id.nav_userName)
        val navUserEmail = nav_header.findViewById<TextView>(R.id.nav_userId)
        var navUserProfile :GradientDrawable = nav_header.nav_profile_icon.background as GradientDrawable
        val navUserNameShort = nav_header.findViewById<TextView>(R.id.nav_profile_name)

        db?.collection("Account")?.document(accountUId)?.get()?.addOnSuccessListener {
            curUser = it.toObject(Account::class.java)!!
            curUserName.text = curUser?.userName.toString()
            navUserName.text = curUser?.userName.toString()
            navUserEmail.text = curUser?.email.toString()
            val color = curUser?.userColor.toString()
            if (color != null) {
                navUserProfile.setColor(Color.parseColor(color))
            }
            navUserNameShort.text = curUser?.userName.toString().substring(0 until 1)
        }

        val notReadNotices = arrayListOf<Notifications>()
        db?.collection("Account")
            ?.document(accountUId)
            ?.collection("Notification")
            ?.whereArrayContains("read", false)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                notReadNotices.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(Notifications::class.java)
//                    Log.d("item", item.toString())
                    notReadNotices.add(item!!)
                }

                if (notReadNotices.isNotEmpty()) {
                    alarmButton.setBackgroundResource(R.drawable.alarm_close)
                }
            }


        // --------------------------------골 추가 버튼 연결--------------------------------------- //

        goalAddButton.setOnClickListener{
            startActivity(Intent(this, AddGoal::class.java))
        }

        // ------------------------------------------------------------------------------------- //


        //버튼 클릭시 동작
        alarmButton.setOnClickListener {
            alarmButton.setBackgroundResource(R.drawable.alarmbtn)
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
            R.id.nav_friendList->{
                val intent = Intent(this, FriendActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "친구목록 클릭됨", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings-> {
                val dialog = ProfileSettingDialog(this)
                dialog.showDialog()
                onBackPressed()
                dialog.setOnClickListener(object: ProfileSettingDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        Toast.makeText(this@MainActivity, "프로필 변경됨", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            R.id.nav_changePW -> {
                val dialog = ChangePWDialog(this)
                dialog.showDialog()
                onBackPressed()
                dialog.setOnClickListener(object: ChangePWDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        Toast.makeText(this@MainActivity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            R.id.nav_logOut-> {
                if (firebaseAuth?.currentUser != null) {
                    firebaseAuth?.signOut()
                    onBackPressed()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    throw Exception ("현재 유저가 없습니다.")
                }
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawers()
//            Toast.makeText(this, "back btn clicked", Toast.LENGTH_SHORT).show()
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
}

