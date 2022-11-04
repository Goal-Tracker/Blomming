package com.example.goaltracker

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class GoalRecordActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()

    private lateinit var tab_layout: TabLayout
    private lateinit var tab_viewPager: ViewPager2
    private lateinit var goal_back_imageButton: ImageButton
    private lateinit var view_top_rectangle: View

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MySharedPreferences.getTheme(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_record)

        // uid 저장
//        MySharedPreferences.setUserId(this, "QL5QEcUUl5QKxKWOKQ2J")
//        MySharedPreferences.setUserNickname(this, "임정수")
//        MySharedPreferences.setUserColor(this, "#fcdcce")

        tab_layout = findViewById(R.id.tab_layout)
        tab_viewPager = findViewById(R.id.tab_viewPager)
        goal_back_imageButton = findViewById(R.id.goal_back_imageButton)
        view_top_rectangle = findViewById(R.id.view_top_rectangle)

        tab_viewPager.adapter = ViewPagerAdapter(this)

        // 추후엔 Dataframe에서 가져다 사용하기"
        val temp_goal_lsit = arrayOf("a6jyD0k2MSJliDJq1wHb", "IyJXNQPcIx2a5EzLUoeN", "1dbd8086-cbed-42cc-a9e7-f8e579907c6a", "5a3804eb-46bd-44d2-9afd-e1ead8edd076")

        var pastNum = 0
        var tabName = arrayOf<String>("진행중  ", "완료된  ")
        temp_goal_lsit.forEach { goal_id ->
            Log.d(ContentValues.TAG, "goal id : $goal_id")
            val goal_db = db.collection("Goal").document(goal_id)

            goal_db.addSnapshotListener { snapshot, e ->
                val goal_day = snapshot?.get("day").toString().toInt()
                val start_day = snapshot?.get("startDay").toString()
                val past_date = pastCalc(start_day);
                Log.d(ContentValues.TAG, "pastNum : $pastNum")

                if (past_date <= goal_day) {
                    pastNum += 1
                }

                tabName = arrayOf<String>("진행중  ${pastNum}", "완료된  ${temp_goal_lsit.size - pastNum}")
                TabLayoutMediator(tab_layout, tab_viewPager) { tab, position ->
                    tab.text = tabName[position]
                }.attach()

            }
        }

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab_viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        goal_back_imageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        private val NUM_PAGES = 2

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> GoalRecordOngoingFragment()
                1 -> GoalRecordFinishedFragment()
                else -> GoalRecordOngoingFragment()
            }
        }
        override fun getItemCount():Int = NUM_PAGES
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