package com.example.goaltracker

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.facebook.gamingservices.GameRequestDialog.show
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlinx.android.synthetic.main.notice_layer.view.*
import kotlinx.android.synthetic.main.notice_view.*

class NoticeActivity : AppCompatActivity() {

    lateinit var noticeAdapter: NoticesAdapter
    //    val notices = mutableListOf<Notification>()
    var firebaseAuth : FirebaseAuth ?= null
    var firestore : FirebaseFirestore ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(MySharedPreferences.getTheme(this))
        setContentView(R.layout.notice_view)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        notice_list.adapter = NoticesAdapter()

        backtomain.setOnClickListener {
            finish()
        }

    }

    inner class NoticesAdapter: RecyclerView.Adapter<NoticesAdapter.ViewHolder>(){
        private var noticeDto = arrayListOf<Notifications>()
        init {
            var accountUId: String? = ""
            accountUId = firebaseAuth?.currentUser?.uid.toString()
            firestore?.collection("Account")?.document(accountUId)?.collection("Notification")?.
            orderBy("timestamp", Query.Direction.DESCENDING)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                noticeDto.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(Notifications::class.java)
//                    Log.d("item", item.toString())
                    noticeDto.add(item!!)
                }
                notifyDataSetChanged()
                Log.d("개수", noticeDto.size.toString())
            }
        }

        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticesAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.notice_layer, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            var viewHolder = (holder as ViewHolder).itemView
            val item = noticeDto[position]

            setNoticeList(holder, item)
//            viewHolder.notice_text.text = item.message
//            viewHolder.notice_profile_name.text = item.userName
        }

        override fun getItemCount(): Int = noticeDto.size

    }

    private fun setNoticeList(holder: NoticesAdapter.ViewHolder, item: Notifications) {
        var viewHolder = (holder as NoticesAdapter.ViewHolder).itemView

        if (item.type == 0) { // 관리자 공지
            viewHolder.notice_text.text = item.message
            viewHolder.notice_profile_name.text = item.userName?.substring(0, 1)
            viewHolder.notice_button.setVisibility(View.GONE)
            viewHolder.notice_title.setVisibility(View.GONE)
            var profileColor : GradientDrawable = viewHolder.notice_profile.background as GradientDrawable
            val color = item.userColor
            if (color != null) {
                profileColor.setColor(Color.parseColor(color))
            }

        } else if (item.type == 1) {  // 친구 요청
            viewHolder.notice_text.text = item.userName+"님이 친구 요청을 보냈습니다."
            viewHolder.notice_profile_name.text = item.userName?.substring(0 , 1)
            viewHolder.notice_title.setVisibility(View.GONE)
            var profileColor : GradientDrawable = viewHolder.notice_profile.background as GradientDrawable
            val color = item.userColor
            if (color != null) {
                profileColor.setColor(Color.parseColor(color))
            }

            var accountUId: String? = ""
            accountUId = firebaseAuth?.currentUser?.uid.toString()
            val friendList : ArrayList<String> = MySharedPreferences.getFriendList(this)

            if (friendList.contains(item.requestUserId.toString())){
                viewHolder.notice_button.text = "수락 완료"
                viewHolder.notice_button.isEnabled = false
            }

            if (!friendList.contains(item.requestUserId.toString())) {
                viewHolder.notice_button.text = "친구 수락"
                viewHolder.notice_button.setOnClickListener {
                    try {
                        Log.d(item.requestUserId.toString(), "요청한 유저 아이디")
                        firestore?.collection("Account")?.document(accountUId!!)
                            ?.collection("Friend")
                            ?.document(item.requestUserId.toString())
                            ?.update("status", "friend")
                            ?.addOnSuccessListener {
                                // 내 account db에서 status friend로 바꾸기가 성공적으로 완료되면 상대방 account db에서 status friend로 바꾸기
                                firestore?.collection("Account")?.document(item.requestUserId.toString())
                                    ?.collection("Friend")
                                    ?.document(accountUId!!)
                                    ?.update("status", "friend")
                                    ?.addOnSuccessListener {
                                        viewHolder.notice_button.text = "수락 완료"
                                        viewHolder.notice_button.isEnabled = false
                                        var friendList = MySharedPreferences.getFriendList(this)
                                        friendList.add(item.requestUserId.toString())
                                        MySharedPreferences.setFriendList(this, friendList)
                                    }
                                    ?.addOnFailureListener {
                                        throw IllegalArgumentException()
                                    }
                            }
                            ?.addOnFailureListener {
                                throw IllegalArgumentException()
                            }

                    } catch(e: IllegalArgumentException) {
                        val currentLayout = findViewById<View>(R.id.notice_view)
                        val snackbar = Snackbar.make(currentLayout, "친구 수락에 실패했습니다.", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
                    // 내 친구 목록
                }
            }


        } else if (item.type == 2) {  // 골 초대
            viewHolder.notice_text.text = item.userName+"님이 새로운 골에 초대했습니다."
            viewHolder.notice_profile_name.text = item.userName?.substring(0 , 1)
            viewHolder.notice_title.text = item.goalName
            var profileColor : GradientDrawable = viewHolder.notice_profile.background as GradientDrawable
            val color : String? = item.userColor
            if (color != null) {
                profileColor.setColor(Color.parseColor(color))
            }

            var accountUId: String?=""
            accountUId = firebaseAuth?.currentUser?.uid.toString()
            var goalList : ArrayList<String> = MySharedPreferences.getGoalList(this)

            if (goalList.contains(item.goalUid)) {
                viewHolder.notice_button.text = "수락 완료"
                viewHolder.notice_button.isEnabled = false
            }
            if (!goalList.contains(item.goalUid)) {
                viewHolder.notice_button.text = "Goal 수락"
                viewHolder.notice_button.setOnClickListener {
                    firestore?.collection("Account")?.document(accountUId)?.get()?.addOnSuccessListener {
                        var curUser = it.toObject(Account::class.java)!!
                        val userInfo = GoalTeamData(accountUId, curUser?.userName.toString(), curUser?.userColor.toString(), curUser?.userMessage.toString(), true)

                        firestore?.collection("Goal")?.document(item.goalUid.toString())
                            ?.collection("team")
                            ?.document(accountUId!!)
                            ?.set(userInfo)
                            ?.addOnSuccessListener {
                                goalList.add(item.goalUid.toString())
                                val goalUpdate = hashMapOf<String, Any?>(
                                    "myGoalList" to goalList,
                                )
                                firestore?.collection("Account")?.document(accountUId)?.update(goalUpdate)
                                MySharedPreferences.setGoalList(this, goalList)
                                goalList = MySharedPreferences.getGoalList(this)
                                Log.d("myGoalList", goalList.toString())
                                viewHolder.notice_button.text = "수락 완료"
                                viewHolder.notice_button.isEnabled = false
                            }

                        val myGoalList : List<String>? = curUser.myGoalList

                        firestore?.collection("Account")?.document()
                    }
                }
            }

        } else if (item.type == 3) {  // 콕 찌르기
            viewHolder.notice_text.text = "["+item.goalName+"]\n"+item.userName+"의 콕 찌르기가 도착했습니다.\n아직 오늘의 목표를 완료하지 못하셨나요?"
            viewHolder.notice_profile_name.text = item.userName?.substring(0 , 1)
            viewHolder.notice_button.setVisibility(View.GONE)
            viewHolder.notice_title.setVisibility(View.GONE)
            var profileColor : GradientDrawable = viewHolder.notice_profile.background as GradientDrawable
            val color :String = item.userColor.toString()
            if (color != null) {
                profileColor.setColor(Color.parseColor(color))
            }

        } else {
            throw Exception("해당 유형의 알림이 존재하지 않습니다.")
        }
    }
}