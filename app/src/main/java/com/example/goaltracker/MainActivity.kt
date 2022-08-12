package com.example.goaltracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.drawer_main.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //어댑터 연결
    //private lateinit var adapter: MainAdapter
    // goal view 모델 가져오기
    //private val viewModel by lazy { ViewModelProvider(this).get(ListViewModel::class.java) }

    val dialog = CustomDialog(this)

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

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_goalAchieve-> {
                dialog.showDialog()
            }
            R.id.nav_friendList-> Toast.makeText(this, "친구목록 클릭됨", Toast.LENGTH_SHORT).show()
            R.id.nav_settings-> Toast.makeText(this, "설정 클릭됨", Toast.LENGTH_SHORT).show()
            R.id.nav_notice-> Toast.makeText(this, "공지사항 클릭됨", Toast.LENGTH_SHORT).show()
            R.id.nav_logOut-> Toast.makeText(this, "로그아웃 클릭됨", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.END)){
            drawer_layout.closeDrawers()
            Toast.makeText(this, "back btn clicked", Toast.LENGTH_SHORT).show()
        } else{
            super.onBackPressed()
        }
    }

//    fun observerData() {
//        viewModel.fetchData().observe(this, Observer {
//            adapter.setListData(it)
//            adapter.notifyDataSetChanged()
//        })
//    }

}

