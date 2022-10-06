package com.example.goaltracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GoalRecordActivity : AppCompatActivity() {
    private lateinit var tab_layout: TabLayout
    private lateinit var tab_viewPager: ViewPager2
    private lateinit var goal_back_imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_record)

        // uid 저장
        MySharedPreferences.setUserId(this, "QL5QEcUUl5QKxKWOKQ2J")
        MySharedPreferences.setUserNickname(this, "임정수")
        MySharedPreferences.setUserColor(this, "#fcdcce")

        tab_layout = findViewById(R.id.tab_layout)
        tab_viewPager = findViewById(R.id.tab_viewPager)
        goal_back_imageButton = findViewById(R.id.goal_back_imageButton)

        tab_viewPager.adapter = ViewPagerAdapter(this)

        val tabName = arrayOf<String>("진행중인  3", "완료된  10")

        TabLayoutMediator(tab_layout, tab_viewPager) { tab, position ->
            tab.text = tabName[position]
        }.attach()

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
}