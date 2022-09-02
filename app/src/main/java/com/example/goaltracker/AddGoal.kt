package com.example.goaltracker

import android.accounts.Account
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.databinding.ItemMemberBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_goal.*
import kotlinx.android.synthetic.main.item_member.view.*
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AddGoal : AppCompatActivity() {

    var firestore : FirebaseFirestore? = null
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    private val currentUser = Firebase.auth.currentUser?.uid // 현재

    lateinit var goalName: EditText         // 이름
    lateinit var first_day: EditText        // 시작일
    lateinit var last_day: EditText         // 종료일
    lateinit var Memo: EditText             // 메모
    lateinit var save_btn: Button           // 저장
    lateinit var close_btn: ImageButton     // 메인 화면으로
    lateinit var searchBtn: ImageButton     // 검색

    // 달력
    var first_calendar = Calendar.getInstance()
    var last_calendar = Calendar.getInstance()


    lateinit var document : String

    // ArrayList 생성
    var FriendsList : ArrayList<Friends> = arrayListOf()
    val teamList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        searchWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            // 검색창에 변화가 있을때마다
            override fun afterTextChanged(editable: Editable) {
                // search 함수 호출
                (recyclerview.adapter as RecyclerViewAdapter).search(searchWord.text.toString())
            }
        })

        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()
        searchBtn = findViewById(R.id.searchBtn)

        goalName = findViewById(R.id.goalName)
        first_day = findViewById(R.id.first_day)
        last_day = findViewById(R.id.last_day)
        save_btn = findViewById(R.id.save_btn)
        Memo = findViewById(R.id.Memo)
        close_btn = findViewById(R.id.close_btn)

        recyclerview.adapter = RecyclerViewAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this)

        // 검색
        searchBtn.setOnClickListener {
            (recyclerview.adapter as RecyclerViewAdapter).search(searchWord.text.toString())
        }

        // 메인 화면으로 이동
        close_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        // 날짜 버튼
        showDatePicker()
        val themedButtonGroup = findViewById<ThemedToggleButtonGroup>(R.id.DayButtonGroup)

        // 시작일 : 현재 날짜
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        first_day.setText(sdf.format(first_calendar.time))

        // 종료일
        themedButtonGroup.setOnSelectListener { button : ThemedButton ->
            if (btn_7.isSelected){
                // 종료일 : 7일 뒤
                last_calendar.add(Calendar.DATE, +7)
            }
            if (btn_30.isSelected){
                // 종료일 : 30일 뒤
                last_calendar.add(Calendar.DATE, +30)
            }
            if (btn_50.isSelected){
                // 종료일 : 50일 뒤
                last_calendar.add(Calendar.DATE, +50)
            }
            if (btn_100.isSelected){
                // 종료일 : 100일 뒤
                last_calendar.add(Calendar.DATE, +100)
            }
            last_day.setText(sdf.format(last_calendar.time))
        }

        // 데이터 저장
        save_btn.setOnClickListener {

            val goal = hashMapOf(
                "GoalName" to goalName.text.toString(),
                "StartDay" to first_day.text.toString(),
                "EndDay" to last_day.text.toString(),
                "Memo" to Memo.text.toString(),
                "Action" to true,
                "Day" to fewDay(),  //날짜 차이 계산
                "Team" to teamList // 팀원 uid (배열)
            )

            firestore!!.collection("Goal")
                .add(goal)
                .addOnSuccessListener { documentReference -> Log.d("DatabaseTest", documentReference.id)
                    document =  documentReference.id}
                .addOnFailureListener { exception -> Log.d("DatabaseTest", exception.message!!) }


            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun showDatePicker() {

        // DatePicker
        first_day.setText(SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()))
        last_day.setText(SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()))


        // 시작일 직접 설정
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            first_calendar.set(Calendar.YEAR, year)
            first_calendar.set(Calendar.MONTH, monthOfYear)
            first_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            first_day.setText(sdf.format(first_calendar.time))
        }

        // 시작일 클릭 시 달력 팝업
        first_day.setOnClickListener {

            Log.d("Clicked", "Interview Date Clicked")

            val dialog = DatePickerDialog(this, dateSetListener,
                first_calendar.get(Calendar.YEAR),
                first_calendar.get(Calendar.MONTH),
                first_calendar.get(Calendar.DAY_OF_MONTH))

            // 시작일은 오늘 이전부터 선택
            dialog.datePicker.maxDate = CalendarHelper.getCurrentDateInMills()
            dialog.show()
        }


        // 종료일 직접 설정
        val dateSetListener2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            last_calendar.set(Calendar.YEAR, year)
            last_calendar.set(Calendar.MONTH, monthOfYear)
            last_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            last_day.setText(sdf.format(last_calendar.time))
        }

        // 종료일 클릭 시 달력 팝업
        last_day.setOnClickListener {

            Log.d("Clicked", "Interview Date Clicked")

            val dialog = DatePickerDialog(this, dateSetListener2,
                last_calendar.get(Calendar.YEAR),
                last_calendar.get(Calendar.MONTH),
                last_calendar.get(Calendar.DAY_OF_MONTH))

            // 종료일은 오늘 이후부터 선택
            dialog.datePicker.minDate = CalendarHelper.getCurrentDateInMills()
            dialog.show()
        }
    }

    // 두 날짜 차이 계산
    fun fewDay(): Long {
        //milliseconds -> day로 변환
        return TimeUnit.MILLISECONDS.toDays(last_calendar.timeInMillis - first_calendar.timeInMillis)
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        init {  // 문서를 불러온 뒤 Person으로 변환해 ArrayList에 담음

            firestore?.collection("Account")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                FriendsList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(Friends::class.java)
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

            fun setFriendsName(item: Friends){
                binding.memberName.text = item.UserName
            }

            fun addFriendsBtnOnclick(item: Friends){

                binding.checkBox.setOnClickListener {
                    // 체크한 친구만 Goal에 추가
                    when ((it as CheckBox).isChecked) {
                        true -> teamList.add(item.Uid.toString())
                        false -> teamList.remove(item.Uid.toString()) // 리스트에서 제거
                    }

                    if(item.Uid != currentUser){
                        val friendRef = firestore?.collection("Goal")?.document(item.Uid.toString())
                        val myRef = firestore?.collection("Goal")?.document("$currentUser")

                        friendRef!!.get()
                            .addOnSuccessListener { friendDocument ->
                                myRef!!.get()
                                    .addOnSuccessListener { myDocument->
                                        val friend: MutableMap<String, String> = HashMap()
                                        val mine: MutableMap<String, String> = HashMap()

                                        // 친구 리스트가 없는 경우
                                        if( myDocument.get("Team") == null ){
                                            mine[item.Uid.toString()] = "request"
                                            // 배열 요소 업데이트
                                            myRef.update("Team", FieldValue.arrayUnion(mine)) // arrayUnion()은 배열에 없는 요소만 추가

                                            friend[currentUser.toString()] = "requested"
                                            friendRef.update("Team", FieldValue.arrayUnion(friend))

                                            Toast.makeText(this@AddGoal,item.UserName.toString()+"에게 친구요청을 보냈습니다.", Toast.LENGTH_LONG).show()
                                            Log.d(friendDocument.get("UserName").toString(),"에게 친구요청 성공")
                                        }
                                    }
                                    .addOnFailureListener {
                                    }
                            }
                    }
                }
            }
        }


        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            holder.addFriendsBtnOnclick(FriendsList[position])
            holder.setFriendsName(FriendsList[position])
            viewHolder.member_name.text = FriendsList[position].UserName
            viewHolder.member_email.text = FriendsList[position].Email
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
                    if (snapshot.getString("UserName")!!.contains(searchWord) || snapshot.getString("Email")!!.contains(searchWord)) {
                        var item = snapshot.toObject(Friends::class.java)
                        FriendsList.add(item!!)
                    }
                }

                notifyDataSetChanged()
            }
        }
    }
}


