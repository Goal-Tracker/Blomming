package com.blooming.goaltracker

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooming.goaltracker.databinding.ItemMemberBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_goal.recyclerview
import kotlinx.android.synthetic.main.activity_edit_goal.searchWord
import kotlinx.android.synthetic.main.item_member.view.*
import java.text.SimpleDateFormat
import java.util.*


class EditGoalActivity : AppCompatActivity() {

    var firestore : FirebaseFirestore? = null
    private val accountUId = Firebase.auth.currentUser?.uid.toString()

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
                (recyclerview.adapter as FriendListAdapter).search(searchWord.text.toString())
                if(searchWord.text.toString() == "")
                {
                    FriendListAdapter() // 친구 목록만 보이게
                }
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

        recyclerview.adapter = FriendListAdapter()
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

        // 시작일 고정
        startDay.setOnClickListener {
            Toast.makeText(this, "시작일은 수정할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

        // 검색
        searchBtn.setOnClickListener {
            // search 함수 호출
            (recyclerview.adapter as FriendListAdapter).search(searchWord.text.toString())
            if(searchWord.text.toString() == "")
            {
                FriendListAdapter() // 친구 목록만 보이게
            }
        }

        // 이전 화면으로 이동
        close_btn.setOnClickListener {
            finish()
        }

        // 날짜 수정
        showDatePicker()

        // 데이터 저장
        save_btn.setOnClickListener {
            // goal 데이터 받아오기
            val userUID = firestore!!.collection("Goal").document(goal_id)
            userUID.addSnapshotListener { snapshot, e ->
                val stampId = snapshot?.get("stampId").toString()

                val goal = hashMapOf(
                    "title" to title.text.toString(),
                    "startDay" to startDay.text.toString(),
                    "endDay" to endDay.text.toString(),
                    "memo" to memo.text.toString(),
                    "action" to true,
                    "stampId" to stampId,
                    "day" to fewDay(),  //날짜 차이 계산
                )
                firestore!!.collection("Goal").document(goal_id).set(goal)
            }

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
    fun fewDay(): Int {
        var sf = SimpleDateFormat("yyyy-MM-dd 00:00:00")
        var startDay = sf.parse(startDay.text.toString() + " 00:00:00")
        var endDay = sf.parse(endDay.text.toString()+ " 00:00:00")

        var date = (endDay.time - startDay.time) / (60 * 60 * 24 * 1000)

        return (date).toInt()
    }

    inner class FriendListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        init {  // 문서를 불러온 뒤 Person으로 변환해 ArrayList에 담음
            firestore?.collection("Account")?.document(accountUId)
                ?.collection("Friend")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    FriendsList.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        val item = snapshot.toObject(Friend::class.java)
                        FriendsList.add(item!!)
                    }
                    notifyDataSetChanged()
                }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
                            "profileColor" to item.userColor.toString(),
                            "goalUid" to goal_id,
                            "request" to false
                        )

                        firestore!!.collection("Goal")
                            .document(goal_id)
                            .collection("team")
                            .document(item.uid.toString())
                            .set(team)

                        // Notification에 골 정보 저장
                        val userUID = firestore!!.collection("Account").document(accountUId)
                        userUID.addSnapshotListener { snapshot, e ->
                            val userName = snapshot?.get("userName").toString()
                            val userUid = snapshot?.get("uid").toString()
                            val profle = snapshot?.get("userColor").toString()

                            val notification_goal = hashMapOf(
                                "goalName" to title.text.toString(),
                                "goalUid" to goal_id,
                                "type" to 2,
                                "userName" to userName,
                                "requestUserId" to userUid,
                                "userColor" to profle,
                                "timestamp" to FieldValue.serverTimestamp(),
                                "read" to false
                            )
                            firestore?.collection("Account")?.document(item.uid.toString())
                                ?.collection("Notification")?.document(goal_id)?.set(notification_goal)
                        }
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