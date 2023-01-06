package com.example.goaltracker

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.databinding.ItemMemberBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_goal.*
import kotlinx.android.synthetic.main.item_member.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class EditGoalActivity : AppCompatActivity() {

    var firestore : FirebaseFirestore? = null

    lateinit var title: EditText            // 이름
    lateinit var startDay: EditText         // 시작일
    lateinit var endDay: EditText           // 종료일
    lateinit var memo: EditText             // 메모
    lateinit var save_btn: Button           // 저장
    lateinit var close_btn: ImageButton     // 메인 화면으로
    lateinit var searchBtn: ImageButton     // 검색

    // 달력
    var startDay_calendar = Calendar.getInstance()
    var endDay_calendar = Calendar.getInstance()

    lateinit var document : String

    // ArrayList 생성
    var FriendsList : ArrayList<Friend> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MySharedPreferences.getTheme(this)) // 테마적용
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_goal)

        searchWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            // 검색창에 변화가 있을때마다
            override fun afterTextChanged(editable: Editable) {
                // search 함수 호출
                (recyclerview.adapter as AddGoal.RecyclerViewAdapter).search(searchWord.text.toString())
            }
        })

        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()
        searchBtn = findViewById(R.id.searchBtn)

        title = findViewById(R.id.title)
        startDay = findViewById(R.id.startDay)
        endDay = findViewById(R.id.endDay)
        save_btn = findViewById(R.id.save_btn)
        memo = findViewById(R.id.memo)
        close_btn = findViewById(R.id.close_btn)

        recyclerview.adapter = RecyclerViewAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this)

        // goalID 값 받아오기
        val goal_id = intent.getStringExtra("goalID") as String

        // goal 데이터 받아오기
        val goal_db = firestore!!.collection("Goal").document(goal_id)

        goal_db.addSnapshotListener { snapshot, e ->
            val goal_title = snapshot?.get("title").toString()
            val goal_memo = snapshot?.get("memo").toString()
            val db_startDay = snapshot?.get("startDay").toString()
            val db_endDay = snapshot?.get("endDay").toString()

            title.setText(goal_title)
            memo.setText(goal_memo)
            startDay.setText(db_startDay)
            endDay.setText(db_endDay)
        }

        // 검색
        searchBtn.setOnClickListener {
            (recyclerview.adapter as RecyclerViewAdapter).search(searchWord.text.toString())
        }

        // 이전 화면으로 이동
        close_btn.setOnClickListener {
            onBackPressed()
        }

        // 날짜 수정
        showDatePicker()

        // 데이터 저장
        save_btn.setOnClickListener {

            val goal = hashMapOf(
                "title" to title.text.toString(),
                "startDay" to startDay.text.toString(),
                "endDay" to endDay.text.toString(),
                "memo" to memo.text.toString(),
                "action" to true,
                "stampId" to UUID.randomUUID().toString(),
                "day" to fewDay(),  //날짜 차이 계산
            )
            firestore!!.collection("Goal").document(goal_id).set(goal)

            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }

    fun showDatePicker() {

        // 종료일 직접 설정
        val dateSetListener2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            endDay_calendar.set(Calendar.YEAR, year)
            endDay_calendar.set(Calendar.MONTH, monthOfYear)
            endDay_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            endDay.setText(sdf.format(endDay_calendar.time))
        }

        // 종료일 클릭 시 달력 팝업
        endDay.setOnClickListener {

            Log.d("Clicked", "Interview Date Clicked")

            val dialog = DatePickerDialog(this, dateSetListener2,
                endDay_calendar.get(Calendar.YEAR),
                endDay_calendar.get(Calendar.MONTH),
                endDay_calendar.get(Calendar.DAY_OF_MONTH))

            // 종료일은 오늘 이후부터 선택
            dialog.datePicker.minDate = CalendarHelper.getCurrentDateInMills()
            dialog.show()
        }
    }

    // 두 날짜 차이 계산
    fun fewDay(): Long {
        //milliseconds -> day로 변환
        return TimeUnit.MILLISECONDS.toDays(endDay_calendar.timeInMillis - startDay_calendar.timeInMillis)
    }
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        init {
            firestore?.collection("Account")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                FriendsList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(Friend::class.java)
                    FriendsList.add(item!!)
                }
                notifyDataSetChanged()
            }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
            val binding = ItemMemberBinding.inflate(layoutInflater,parent,false)
            return ViewHolder(binding)
        }

        inner class ViewHolder(private val binding: ItemMemberBinding) :
            RecyclerView.ViewHolder(binding.root){

            fun setFriendsName(item: Friend){
                binding.memberName.text = item.userName
            }

            fun addFriendsBtnOnclick(item: Friend){
                binding.checkBox.setOnClickListener {

                    // 값 받아오기
                    val goal_id = intent.getStringExtra("goalID") as String

                    if ((it as CheckBox).isChecked) {
                        val team = hashMapOf(
                            "userName" to item.userName.toString(),
                            "uid" to item.uid.toString(),
                            "profileColor" to item.userColor.toString()
                        )

                        firestore!!.collection("Goal")
                            .document(goal_id)
                            .collection("team")
                            .document(item.uid.toString())
                            .set(team)
                    }
                    // 체크 해제 시 삭제
                    else {
                        firestore!!.collection("Goal")
                            .document(goal_id)
                            .collection("team")
                            .document(item.uid.toString())
                            .delete()
                    }
                }
            }
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            holder.addFriendsBtnOnclick(FriendsList[position])
            holder.setFriendsName(FriendsList[position])
            viewHolder.member_name.text = FriendsList[position].userName
            viewHolder.member_email.text = FriendsList[position].email
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return FriendsList.size
        }

        // 파이어스토어에서 데이터를 불러와서 검색어가 있는지 판단
        fun search(searchWord : String) {
            firestore?.collection("Account")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                FriendsList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.getString("userName")?.contains(searchWord) == true
                        ||snapshot.getString("email")?.contains(searchWord) == true) {
                        var item = snapshot.toObject(Friend::class.java)
                        FriendsList.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
        }
    }
}