package com.blooming.goaltracker

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlinx.android.synthetic.main.notice_layer.view.*
import kotlinx.android.synthetic.main.notice_view.*

class NoticeActivity : AppCompatActivity() {

    lateinit var noticeAdapter: NoticesAdapter
    //    val notices = mutableListOf<Notification>()
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    var accountUId = firebaseAuth?.currentUser?.uid.toString()
    val notices = firestore.collection("Account").document(accountUId).collection("Notification")

//    private val binding: ActivitySwipeBinding by lazy{
//        ActivitySwiprBinding.inflate(
//            layoutInflater
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notices
            .whereEqualTo("read", false).get().addOnSuccessListener { querySnapshot ->

                val notReadNoticeDocuments: MutableList<DocumentSnapshot> = querySnapshot.documents

                for (document in notReadNoticeDocuments){
                    notices.document(document.id).update("read", true)
//                    Log.d("read", "true로 바꿈")
                }

            }

        setTheme(MySharedPreferences.getTheme(this))
        setContentView(R.layout.notice_view)

        notice_list.adapter = NoticesAdapter()

        backtomain.setOnClickListener {
            finish()
        }

    }

    inner class NoticesAdapter: RecyclerView.Adapter<NoticesAdapter.ViewHolder>(){
        private var noticeDto = arrayListOf<Notifications>()
        init {
            notices
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    noticeDto.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Notifications::class.java)
//                        Log.d("item", item.toString())
                        noticeDto.add(item!!)
                    }
                    notifyDataSetChanged()
//                    Log.d("개수", noticeDto.size.toString())
                }
        }

        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

        val getRequestNotice = firestore.collection("Account").document(item.requestUserId.toString())

        if (item.type == 0) { // 관리자 공지
            viewHolder.notice_text.text = item.message
            viewHolder.notice_profile_name.text = item.userName?.substring(0, 1)
            viewHolder.notice_button.setVisibility(View.GONE)
            viewHolder.notice_title.setVisibility(View.GONE)
            var profileColor : GradientDrawable = viewHolder.notice_profile.background as GradientDrawable
            Log.d("item requestUserId", item?.requestUserId.toString())
            getRequestNotice
                .get()
                .addOnSuccessListener { document ->
                    var account = document.toObject(Account::class.java)
                    var name = account?.userName.toString()
                    var profileName = account?.userName?.substring(0, 1)
                    var color = account?.userColor.toString()

                    if (color != null) {
                        profileColor.setColor(Color.parseColor(color))
                    }
                }

        } else if (item.type == 1) {  // 친구 요청
            viewHolder.notice_title.setVisibility(View.GONE)
            var profileColor : GradientDrawable = viewHolder.notice_profile.background as GradientDrawable
            getRequestNotice
                .get()
                .addOnSuccessListener { document ->
                    var account = document.toObject(Account::class.java)
                    var name = account?.userName.toString()
                    var profileName = account?.userName?.substring(0, 1)
                    var color = account?.userColor.toString()

                    if (name!=null) {
                        viewHolder.notice_text.text = name+"님이 친구 요청을 보냈습니다."
                    }

                    if (profileName!=null){
                        viewHolder.notice_profile_name.text = profileName
                    }

                    if (color != null) {
                        try {
                            profileColor.setColor(Color.parseColor(color))
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                            // 색상 값이 올바르지 않을 때의 예외 처리
                        }
                    }
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
                        firestore
                            .collection("Account")
                            .document(accountUId!!)
                            .collection("Friend")
                            .document(item.requestUserId.toString())
                            .update("status", "friend")
                            .addOnSuccessListener {
                                // 내 account db에서 status friend로 바꾸기가 성공적으로 완료되면 상대방 account db에서 status friend로 바꾸기
                                getRequestNotice
                                    .collection("Friend")
                                    .document(accountUId!!)
                                    .update("status", "friend")
                                    .addOnSuccessListener {
                                        NoticesAdapter().notifyDataSetChanged()
                                        viewHolder.notice_button.text = "수락 완료"
                                        viewHolder.notice_button.isEnabled = false
                                        var friendList = MySharedPreferences.getFriendList(this)
                                        friendList.add(item.requestUserId.toString())
                                        MySharedPreferences.setFriendList(this, friendList)

                                    }
                                    .addOnFailureListener {
                                        throw IllegalArgumentException()
                                    }
                            }
                            .addOnFailureListener {
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

            viewHolder.notice_delete_button.setOnClickListener {
                notices
                    .document(item!!.requestUserId.toString())
                    .delete()
                    .addOnSuccessListener {

                        Log.d("Delete", "Success!!!!")
                    }
                    .addOnFailureListener {
                        throw IllegalArgumentException()
                    }
            }


        } else if (item.type == 2) {  // 골 초대
            var profileColor : GradientDrawable = viewHolder.notice_profile.background as GradientDrawable
            getRequestNotice
                .get()
                .addOnSuccessListener { document ->
                    var account = document.toObject(Account::class.java)
                    var name = account?.userName.toString()
                    var profileName = account?.userName?.substring(0, 1)
                    var color = account?.userColor.toString()

                    if (name!=null) {
                        viewHolder.notice_text.text = name+"님이 새로운 골에 초대하셨습니다."
                    }

                    if (profileName!=null){
                        viewHolder.notice_profile_name.text = profileName
                    }

                    if (color != null) {
                        profileColor.setColor(Color.parseColor(color))
                    }
                }
            viewHolder.notice_title.text = item.goalName.toString()

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
                    firestore
                        .collection("Account")
                        .document(accountUId)
                        .get()
                        .addOnSuccessListener {
                        var curUser = it.toObject(Account::class.java)!!
                        val userInfo = GoalTeamData(accountUId, curUser?.userName.toString(), curUser?.userColor.toString(), curUser?.userMessage.toString(), true)

                        firestore.collection("Goal")
                            .document(item.goalUid.toString())
                            .collection("team")
                            .document(accountUId!!)
                            .set(userInfo)
                            .addOnSuccessListener {
                                goalList.add(item.goalUid.toString())
                                val goalUpdate = hashMapOf<String, Any?>(
                                    "myGoalList" to goalList,
                                )
                                firestore
                                    .collection("Account")
                                    .document(accountUId)
                                    .update(goalUpdate)
                                MySharedPreferences.setGoalList(this, goalList)
                                goalList = MySharedPreferences.getGoalList(this)
                                Log.d("myGoalList", goalList.toString())
                                viewHolder.notice_button.text = "수락 완료"
                                viewHolder.notice_button.isEnabled = false
                                NoticesAdapter().notifyDataSetChanged()
                            }

//                        val myGoalList : List<String>? = curUser.myGoalList
//
//                        firestore.collection("Account").document()
                    }
                }
            }

            viewHolder.notice_delete_button.setOnClickListener {
                notices
                    .document(item!!.goalUid.toString())
                    .delete()
                    .addOnSuccessListener {
                        Log.d("Delete", "Success!!!!")
                    }
                    ?.addOnFailureListener {
                        throw IllegalArgumentException()
                    }
            }

        } else if (item.type == 3) {  // 콕 찌르기
            viewHolder.notice_text.text = item.userName+"님의 콕 찌르기가 도착했습니다.\n아직 오늘의 목표를 완료하지 못하셨나요?"
            viewHolder.notice_profile_name.text = item.userName?.substring(0 , 1)
            viewHolder.notice_button.setVisibility(View.GONE)
            viewHolder.notice_title.text = item.goalName
            var profileColor : GradientDrawable = viewHolder.notice_profile.background as GradientDrawable
            firestore
                .collection("Account")
                .document(item.requestUserId.toString())
                .get()
                .addOnSuccessListener { document ->
                    var account = document.toObject(Account::class.java)
                    var color = account?.userColor.toString()

                    if (color != null) {
                        profileColor.setColor(Color.parseColor(color))
                    }
                }

        } else {
            throw Exception("해당 유형의 알림이 존재하지 않습니다.")
        }
    }
}