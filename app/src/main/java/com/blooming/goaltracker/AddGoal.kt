package com.blooming.goaltracker

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooming.goaltracker.databinding.ItemMemberBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_goal.*
import kotlinx.android.synthetic.main.item_member.view.*
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AddGoal : AppCompatActivity() {

    var firestore : FirebaseFirestore? = null
    var firebaseAuth : FirebaseAuth?= null
    private val accountUId = Firebase.auth.currentUser?.uid.toString()

    lateinit var title: EditText            // 이름
    lateinit var startDay: EditText         // 시작일
    lateinit var endDay: EditText           // 종료일
    lateinit var memo: EditText             // 메모
    lateinit var save_btn: Button           // 저장
    lateinit var close_btn: ImageButton     // 메인 화면으로
    lateinit var searchBtn: ImageButton     // 검색

    var goalID : String = UUID.randomUUID().toString()
    var stampID : String = UUID.randomUUID().toString()

    // 달력
    var startDay_calendar = Calendar.getInstance()
    var endDay_calendar = Calendar.getInstance()

    // ArrayList 생성
    var FriendsList : ArrayList<Friend> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MySharedPreferences.getTheme(this)) // 테마적용
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

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

        // 검색
        searchBtn.setOnClickListener {
            // search 함수 호출
            (recyclerview.adapter as FriendListAdapter).search(searchWord.text.toString())
            if(searchWord.text.toString() == "")
            {
                FriendListAdapter() // 친구 목록만 보이게
            }
        }

        // 메인 화면으로 이동
        close_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        // 날짜 버튼
        val themedButtonGroup = findViewById<ThemedToggleButtonGroup>(R.id.DayButtonGroup)

        // 시작일 : 현재 날짜
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        startDay.setText(sdf.format(startDay_calendar.time))

        // 종료일
        themedButtonGroup.setOnSelectListener { button : ThemedButton ->
            if (btn_7.isSelected){
                // 종료일 : 7일 뒤
                endDay_calendar.add(Calendar.DATE, +7)
            }
            if (btn_30.isSelected){
                // 종료일 : 30일 뒤
                endDay_calendar.add(Calendar.DATE, +30)
            }
            if (btn_50.isSelected){
                // 종료일 : 50일 뒤
                endDay_calendar.add(Calendar.DATE, +50)
            }
            if (btn_100.isSelected){
                // 종료일 : 100일 뒤
                endDay_calendar.add(Calendar.DATE, +100)
            }
            if (btn_etc.isSelected){
                // 기타 : 사용자 설정
                showDatePicker()
            }
            endDay.setText(sdf.format(endDay_calendar.time))
        }

        // 데이터 저장
        save_btn.setOnClickListener {

            // Goal에 저장
            val goal = hashMapOf(
                "title" to title.text.toString(),
                "startDay" to startDay.text.toString(),
                "endDay" to endDay.text.toString(),
                "action" to true,
                "stampId" to stampID,
                "day" to fewDay(),  //날짜 차이 계산
            )
            firestore!!.collection("Goal").document(goalID).set(goal)

            if(memo.text != null)
            {
                firestore!!.collection("Goal").document(goalID)?.update("memo", memo.text.toString())
            }

            // 사용자 정보 저장
            val userUID = firestore!!.collection("Account").document(accountUId)
            userUID.addSnapshotListener { snapshot, e ->
                val userName = snapshot?.get("userName").toString()
                val userUid = snapshot?.get("uid").toString()
                val profle = snapshot?.get("userColor").toString()

                val user = hashMapOf(
                    "userName" to userName,
                    "uid" to userUid,
                    "profileColor" to profle,
                    "request" to true,
                    "goalUid" to goalID
                    )
                firestore!!.collection("Goal").document(goalID)
                    .collection("team").document(userUid).set(user)
            }

            // myGoalList 추가
            firestore?.collection("Account")?.document(accountUId)
                ?.update("myGoalList", FieldValue.arrayUnion(goalID))

            // Notification에 골 정보 저장
            val notification_goal = hashMapOf(
                "goalName" to title.text.toString(),
                "goalUid" to goalID,
                "type" to 2,
                )
            firestore?.collection("Account")?.document("$accountUId")
                ?.collection("Notification")?.document()?.set(notification_goal)

            // Stamp에 저장
            val hashMap = HashMap<String, String>()
            val goal_ID = hashMapOf(
                "goalID" to goalID,
                "dayRecord" to hashMap
            )
            firestore!!.collection("Stamp").document(stampID).set(goal_ID)
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()


            // 메인 화면으로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun showDatePicker() {

        // DatePicker
        startDay.setText(SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()))
        endDay.setText(SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()))

        // 시작일 직접 설정
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            startDay_calendar.set(Calendar.YEAR, year)
            startDay_calendar.set(Calendar.MONTH, monthOfYear)
            startDay_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            startDay.setText(sdf.format(startDay_calendar.time))
        }

        // 시작일 클릭 시 달력 팝업
        startDay.setOnClickListener {

            Log.d("Clicked", "Interview Date Clicked")

            val dialog = DatePickerDialog(this, dateSetListener,
                startDay_calendar.get(Calendar.YEAR),
                startDay_calendar.get(Calendar.MONTH),
                startDay_calendar.get(Calendar.DAY_OF_MONTH))

            // 시작일은 오늘 이전부터 선택
            dialog.datePicker.maxDate = CalendarHelper.getCurrentDateInMills()
            dialog.show()
        }

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
                    if ((it as CheckBox).isChecked) {
                        // 체크한 친구만 Goal에 추가
                        val team = hashMapOf(
                            "userName" to item.userName.toString(),
                            "uid" to item.uid.toString(),
                            "profileColor" to item.userColor.toString(),
                            "goalUid" to goalID,
                            "request" to false
                            )

                        firestore!!.collection("Goal")
                            .document(goalID)
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
                                "goalUid" to goalID,
                                "type" to 2,
                                "userName" to userName,
                                "requestUserId" to userUid,
                                "userColor" to profle,
                                "timestamp" to FieldValue.serverTimestamp(),
                                "read" to false
                            )
                            firestore?.collection("Account")?.document(item.uid.toString())
                                ?.collection("Notification")?.document(goalID)?.set(notification_goal)
                        }
                    }
                    // 체크 해제 시 삭제
                    else {
                        // 체크 해제된 팀원 삭제
                        firestore!!.collection("Goal")
                            .document(goalID)
                            .collection("team")
                            .document(item.uid.toString())
                            .delete()

                        // Notification의 골 정보 삭제
                        firestore?.collection("Account")?.document(item.uid.toString())
                            ?.collection("Notification")?.document(goalID)?.delete()
                    }
                }
            }
        }


        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = (holder as ViewHolder).itemView

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
                        val item = snapshot.toObject(Friend::class.java)
                        FriendsList.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
        }
    }
}
