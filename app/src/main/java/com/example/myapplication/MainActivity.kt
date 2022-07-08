package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.view.MenuItem
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.drawer_main.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //lateinit var menubtn: ImageButton
    //lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_main)

        setSupportActionBar(main_toolbar) //툴바를 액티비티의 앱바로 지정
        //supportActionBar?.setDisplayHomeAsUpEnabled(true) //드로어를 꺼낼 홈버튼 활성화
        //supportActionBar?.setHomeAsUpIndicator(R.drawable.menubtn) //홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false)  //툴바에 타이틀 안보이게

        //menubtn=findViewById(R.id.menu)
        //네비게이션 드로어 생성
        //drawerLayout = findViewById(R.id.drawer_layout)

        //네비게이션 드로어 내에 있는 화면의 이벤트를 처리하기 위해 생성
        nav_view.setNavigationItemSelectedListener(this) //Navigation 리스너

        alarmButton.setOnClickListener {
            startActivity(Intent(this, NoticeActivity::class.java))
        }

        menubtn.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.END)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_goalAchieve-> Toast.makeText(this, "골 기록 클릭됨", Toast.LENGTH_SHORT).show()
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


}

