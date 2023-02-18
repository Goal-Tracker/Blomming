package com.example.goaltracker

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_friend.backBtn
import kotlinx.android.synthetic.main.activity_friend.*


class FriendActivity : AppCompatActivity() {
    // 파이어스토어
    var firestore: FirebaseFirestore? = null
    private val currentUser = Firebase.auth.currentUser?.uid

    private lateinit var dialog: DeleteDialog  //다이얼로그
    var searchcount = 0 //사람 수
    private lateinit var adapter: FriendAdapter
    private lateinit var accept_adapter: FriendAcceptAdapter
    private lateinit var request_adapter: FriendRequestAdapter

    private val friend = mutableListOf<Friend>()
    private val friend_accept = mutableListOf<Friend>()
    private val friend_request = mutableListOf<Friend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MySharedPreferences.getTheme(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()

        //친구 목록
        adapter = FriendAdapter(friend)
        FriendRecyclerView.adapter = adapter
        FriendRecyclerView.layoutManager = LinearLayoutManager(this)
        FriendRecyclerView.setHasFixedSize(true)

        //친구 신청 수락
        accept_adapter = FriendAcceptAdapter(friend_accept)
        FriendAcceptRecyclerView.adapter = accept_adapter
        FriendAcceptRecyclerView.layoutManager =
            LinearLayoutManager(this).also { it.orientation = LinearLayoutManager.HORIZONTAL } //리사이클러뷰 가로로
        FriendAcceptRecyclerView.setHasFixedSize(true)

        //친구 신청 취소
        request_adapter = FriendRequestAdapter(friend_request)
        FriendRequestRecyclerView.adapter = request_adapter
        FriendRequestRecyclerView.layoutManager =
            LinearLayoutManager(this).also { it.orientation = LinearLayoutManager.HORIZONTAL } //리사이클러뷰 가로로
        FriendRequestRecyclerView.setHasFixedSize(true)


        //x버튼 누르면 main 화면으로 이동
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


        //돋보기 버튼 누르면 친구추가 화면으로 이동
        searchBtn.setOnClickListener {
            startActivity(Intent(this, AddFriendActivity::class.java))
        }

        //입력칸 누르면 친구추가 화면으로 이동
        search.setOnClickListener {
            startActivity(Intent(this, AddFriendActivity::class.java))
        }


    }

    //친구 수
    fun getCount() {
        var text_count = findViewById<TextView>(R.id.search_friendcount)
        text_count.text = "" + searchcount + "명"
    }

    //친구 신청받은 수
    fun getAcCount() {
        var text_count = findViewById<TextView>(R.id.accept_friendcount)
        text_count.text = "" + searchcount + "명"
    }

    //친구 신청보낸 수
    fun getReCount() {
        var text_count = findViewById<TextView>(R.id.request_friendcount)
        text_count.text = "" + searchcount + "명"
    }

    //친구 신청 수락
    inner class FriendAcceptAdapter(private val context: MutableList<Friend>) :
        RecyclerView.Adapter<FriendAcceptAdapter.ViewHolder>() {
        private var friend_accept = mutableListOf<Friend>()

        //status가 accept인 것만 불러오기
        init {
            firestore?.collection("Account")?.document("$currentUser")
                ?.collection("Friend")
                ?.whereEqualTo("status", "accept")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    friend_accept.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Friend::class.java)
                        friend_accept.add(item!!)
                    }
                    searchcount = friend_accept.size
                    getAcCount()
                    accept_adapter.notifyDataSetChanged()
                }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_accept_friend, parent, false)
            return ViewHolder(view)
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView
            holder.setFriendAcceptName(friend_accept[position])
            holder.SetFriendAcceptColor(friend_accept[position])
            holder.AcceptBtnOnclick(friend_accept[position])


        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val AcceptName: TextView = itemView.findViewById(R.id.AcceptName)
            private val AcceptColor: ImageView = itemView.findViewById(R.id.AcceptColor)
            private val AcceptBtn: Button = itemView.findViewById(R.id.AcceptBtn)

            //친구 이름
            fun setFriendAcceptName(item: Friend) {
                AcceptName.text = item.userName?.get(0).toString()
            }

            //친구 프로필
            fun SetFriendAcceptColor(item: Friend) {
                var circleResource : GradientDrawable = AcceptColor.background as GradientDrawable
                firestore?.collection("Account")?.document(item.uid.toString())
                    ?.get()?.addOnSuccessListener { document ->
                        var account = document.toObject(Account::class.java)
                        var color = account?.userColor.toString()

                        if (color != null) {
                            circleResource.setColor(Color.parseColor(color))
                        }
                    }
            }

            //친구 수락 버튼
            fun AcceptBtnOnclick(item: Friend) {
                AcceptBtn.setOnClickListener {
                    // 내 친구 목록
                    firestore?.collection("Account")?.document("$currentUser")
                        ?.collection("Friend")
                        ?.document("${item.uid}")
                        ?.update("status", "friend")
                        ?.addOnSuccessListener { }
                        ?.addOnFailureListener { }

                    // 상대방 친구 목록
                    firestore?.collection("Account")?.document(item.uid.toString())
                        ?.collection("Friend")
                        ?.document("${currentUser}")
                        ?.update("status", "friend")
                        ?.addOnSuccessListener { }
                        ?.addOnFailureListener { }

                }

            }

        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount() = friend_accept.size

    }


    //친구 신청 취소
    inner class FriendRequestAdapter(private val context: MutableList<Friend>) :
        RecyclerView.Adapter<FriendRequestAdapter.ViewHolder>() {
        private var friend_request = mutableListOf<Friend>()

        //status가 request인 것만 불러오기
        init {
            firestore?.collection("Account")?.document("$currentUser")
                ?.collection("Friend")
                ?.whereEqualTo("status", "request")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    // ArrayList 비워줌
                    friend_request.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Friend::class.java)
                        friend_request.add(item!!)
                    }
                    searchcount = friend_request.size
                    getReCount()
                    request_adapter.notifyDataSetChanged()
                }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_request_friend, parent, false)
            return ViewHolder(view)
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView
            holder.setFriendRequestName(friend_request[position])
            holder.SetFriendRequestColor(friend_request[position])
            holder.RequestBtnOnclick(friend_request[position])
        }


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val friendList: ArrayList<String> =
                MySharedPreferences.getFriendList(this@FriendActivity)
            private val RequestName: TextView = itemView.findViewById(R.id.RequestName)
            private val RequestColor: ImageView = itemView.findViewById(R.id.RequestColor)
            private val RequestBtn: Button = itemView.findViewById(R.id.RequestBtn)

            //친구 이름
            fun setFriendRequestName(item: Friend) {
                RequestName.text = item.userName?.get(0).toString()
            }

            //친구 프로필
            fun SetFriendRequestColor(item: Friend) {
                var circleResource: GradientDrawable = RequestColor.background as GradientDrawable
                firestore?.collection("Account")?.document(item.uid.toString())
                    ?.get()?.addOnSuccessListener { document ->
                        var account = document.toObject(Account::class.java)
                        var color = account?.userColor.toString()

                        if (color != null) {
                            circleResource.setColor(Color.parseColor(color))
                        }
                    }
            }

            //친구 신청 취소 버튼
            fun RequestBtnOnclick(item: Friend) {
                RequestBtn.setOnClickListener {

                    var friendList = MySharedPreferences.getFriendList(this@FriendActivity)
                    friendList.remove(item.uid.toString())
                    MySharedPreferences.setFriendList(this@FriendActivity, friendList)

                    // 내 친구 목록
                    firestore?.collection("Account")?.document("$currentUser")
                        ?.collection("Friend")
                        ?.document("${item.uid}")
                        ?.delete()

                    // 상대방 친구 목록
                    firestore?.collection("Account")?.document(item.uid.toString())
                        ?.collection("Friend")
                        ?.document("${currentUser}")
                        ?.delete()

                }

            }

        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount() = friend_request.size
    }


    //친구 목록
    inner class FriendAdapter(private val context: MutableList<Friend>) :
        RecyclerView.Adapter<FriendAdapter.ViewHolder>() {
        private var friend = mutableListOf<Friend>()

        //status가 friend인 것만 불러오기
        init {
            firestore?.collection("Account")?.document("$currentUser")
                ?.collection("Friend")
                ?.whereEqualTo("status", "friend")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    friend.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Friend::class.java)
                        friend.add(item!!)
                    }
                    searchcount = friend.size
                    getCount()
                    adapter.notifyDataSetChanged()
                }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_friend, parent, false)
            return ViewHolder(view)
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView
            holder.setFriendName(friend[position])
            holder.setProfileName(friend[position])
            holder.SetFriendColor(friend[position])
            holder.SetFriendEmail(friend[position])

            var account = friend[position]

            //아이템을 클릭하면 다이얼로그 생성
            holder.itemView.setOnClickListener {
                delDialog(it.context, account)
            }
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val FriendName: TextView = itemView.findViewById(R.id.FriendName)
            private val FriendProfileName: TextView = itemView.findViewById(R.id.FriendProfileName)
            private val FriendEmail: TextView = itemView.findViewById(R.id.FriendEmail)
            private val FriendColor: ImageView = itemView.findViewById(R.id.FriendColor)


            //친구 이름
            fun setFriendName(item: Friend) {
                FriendName.text = item.userName
            }

            //친구 프로필 이름
            fun setProfileName(item: Friend) {
                FriendProfileName.text = item.userName?.get(0).toString()
            }

            //친구 프로필
            fun SetFriendColor(item: Friend) {
                var circleResource: GradientDrawable = FriendColor.background as GradientDrawable
                firestore?.collection("Account")?.document(item.uid.toString())
                    ?.get()?.addOnSuccessListener { document ->
                        var account = document.toObject(Account::class.java)
                        var color = account?.userColor.toString()

                        if (color != null) {
                            circleResource.setColor(Color.parseColor(color))
                        }
                    }
            }

            //친구 이메일
            fun SetFriendEmail(item: Friend) {
                FriendEmail.text = item.email
            }
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount() = friend.size

        fun delDialog(context: Context, item: Friend) {

            dialog = DeleteDialog(
                context = context,
                userName = item.userName,
                uid = item.uid,
                email = item.email!!,
                deletebtnListener = deletebtnListener,
                cancelbtnListener = cancelbtnListener
            )

            //다이얼로그 타이틀 및 테두리 없애기
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        //삭제 버튼 클릭 시
        private val deletebtnListener = View.OnClickListener {
            firestore?.collection("Account")
                ?.document("$currentUser")
                ?.collection("Friend")
                ?.document("${dialog.uid}")
                ?.delete()
                ?.addOnSuccessListener {
                    var friendList = MySharedPreferences.getFriendList(this@FriendActivity)
                    friendList.remove(dialog.uid.toString())
                    MySharedPreferences.setFriendList(this@FriendActivity, friendList)

                    Toast.makeText(this@FriendActivity, "친구를 삭제했습니다.", Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }
                ?.addOnFailureListener { }

            firestore?.collection("Account")
                ?.document(dialog.uid.toString())
                ?.collection("Friend")
                ?.document("${currentUser}")
                ?.delete()
                ?.addOnSuccessListener { }
                ?.addOnFailureListener { }


        }

        private val cancelbtnListener = View.OnClickListener {
            dialog?.dismiss()
        }


    }
}






