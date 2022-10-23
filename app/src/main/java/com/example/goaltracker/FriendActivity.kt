package com.example.goaltracker



import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_friend.backBtn
import kotlinx.android.synthetic.main.activity_friend.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.item_accept_friend.*


class FriendActivity : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    private val currentUser = Firebase.auth.currentUser?.uid
    var searchcount = 0
    private lateinit var adapter: FriendAdapter
    private lateinit var accept_adapter: FriendAcceptAdapter
    private lateinit var request_adapter: FriendRequestAdapter
    private val friend = mutableListOf<Friends>()
    private val friend_accept = mutableListOf<Friends>()
    private val friend_request = mutableListOf<Friends>()
    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()
        setFriends()
        adapter = FriendAdapter(friend)
        FriendRecyclerView.adapter = adapter
        FriendRecyclerView.layoutManager = LinearLayoutManager(this)
        FriendRecyclerView.setHasFixedSize(true)
        //setRequests()
        accept_adapter = FriendAcceptAdapter(friend_accept)
        FriendAcceptRecyclerView.adapter = accept_adapter
        FriendAcceptRecyclerView.layoutManager =
            LinearLayoutManager(this).also { it.orientation = LinearLayoutManager.HORIZONTAL }
        FriendAcceptRecyclerView.setHasFixedSize(true)

        request_adapter = FriendRequestAdapter(friend_request)
        FriendRequestRecyclerView.adapter = request_adapter
        FriendRequestRecyclerView.layoutManager =
            LinearLayoutManager(this).also { it.orientation = LinearLayoutManager.HORIZONTAL }
        FriendRequestRecyclerView.setHasFixedSize(true)
//initRecycler()


        //x버튼 누르면 main 화면으로 이동
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }

    fun getCount() {
        var text_count = findViewById<TextView>(R.id.search_friendcount)
        text_count.text = "" + searchcount + "명"
    }

    fun getAcCount() {
        var text_count = findViewById<TextView>(R.id.accept_friendcount)
        text_count.text = "" + searchcount + "명"
    }

    fun getReCount() {
        var text_count = findViewById<TextView>(R.id.request_friendcount)
        text_count.text = "" + searchcount + "명"
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setFriends() {
        val myDocRef = firestore?.collection("Account")?.document("$currentUser")
        myDocRef!!.get()
            .addOnSuccessListener { document ->
                if (document.get("Friends") != null) {
                    val hashMap: ArrayList<HashMap<String, String>> =
                        document.get("Friends") as ArrayList<HashMap<String, String>>
                    friend.clear()
                    for (keys in hashMap) {
                        val key = keys.keys.iterator().next()
                        if (keys[key].toString() == "friend") {
                            val friendDocRef = firestore?.collection("Account")?.document(key)
                            friendDocRef?.get()?.addOnSuccessListener { document ->
                                friend.apply {
                                    val UserName = document.get("UserName").toString()
                                    val Email = document.get("Email").toString()
                                    val UserColor = document.get("UserColor").toString()
                                    add(Friends(UserName, Email, UserColor))
                                    Log.d(TAG, "성공")
                                    searchcount = friend.size
                                    getCount()
                                    adapter.loadFriend(friend)
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            }
    }

    inner class FriendAcceptAdapter(private val context: MutableList<Friends>) :
        RecyclerView.Adapter<FriendAcceptAdapter.ViewHolder>() {
        private var friend_accept = mutableListOf<Friends>()

        init {  // Account의 문서를 불러온 뒤 Friend으로 변환해 ArrayList에 담음
            firestore?.collection("Account")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    friend_accept.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Friends::class.java)
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

            fun setFriendAcceptName(item: Friends) {
                AcceptName.text = item.userName
            }

            fun SetFriendAcceptColor(item: Friends) {
                var circleResource = 0
                when (item.userColor) {
                    "f69b94" -> circleResource = R.drawable.b_f69b94
                    "f8c8c4" -> circleResource = R.drawable.b_f8c8c4
                    "fcdcce" -> circleResource = R.drawable.b_fcdcce
                    "96b0e5" -> circleResource = R.drawable.b_96b0e5
                    "92b9e2" -> circleResource = R.drawable.b_92b9e2
                    "ebc0c7" -> circleResource = R.drawable.b_ebc0c7
                    "7bb6c8" -> circleResource = R.drawable.b_7bb6c8
                    "aad3d7" -> circleResource = R.drawable.b_aad3d7
                    "f5f1f0" -> circleResource = R.drawable.b_f5f1f0
                    "d5e3e6" -> circleResource = R.drawable.b_d5e3e6
                    "f2a4b1" -> circleResource = R.drawable.b_f2a4b1
                    "7175a5" -> circleResource = R.drawable.b_7175a5
                    "a1b3d7" -> circleResource = R.drawable.b_a1b3d7
                    "bd83cf" -> circleResource = R.drawable.b_bd83cf
                    "e5afe9" -> circleResource = R.drawable.b_e5afe9

                }
                AcceptColor.setImageResource(circleResource)
            }

            fun AcceptBtnOnclick(item: Friends) {
                AcceptBtn.setOnClickListener {

                    var myDocRef = firestore?.collection("Account")?.document("$currentUser")
                    myDocRef!!.get()
                        .addOnSuccessListener { document ->
                            if (document.get("Friends") != null) {
                                val hashMap: ArrayList<Map<String, String>> =
                                    document.get("Friends") as ArrayList<Map<String, String>>
                                for (keys in hashMap) {
                                    val friendlist: MutableMap<String, Any> = HashMap()
                                    val mylist: MutableMap<String, Any> = HashMap()
                                    val key = keys.keys.iterator().next()
                                    if (keys[key] == "accept") {
                                        val friendDocRef =
                                            firestore?.collection("Account")?.document(key)
                                        friendDocRef!!.get()
                                            .addOnSuccessListener { document ->
                                                mylist[key] = "accept"
                                                myDocRef.update(
                                                    "Friends",
                                                    FieldValue.arrayRemove(mylist)
                                                )
                                                mylist[key] = "friend"
                                                myDocRef.update(
                                                    "Friends",
                                                    FieldValue.arrayUnion(mylist)
                                                )

                                                friendlist[currentUser.toString()] = "request"
                                                friendDocRef.update(
                                                    "Friends",
                                                    FieldValue.arrayRemove(friendlist)
                                                )
                                                friendlist[currentUser.toString()] = "friend"
                                                friendDocRef.update(
                                                    "Friends",
                                                    FieldValue.arrayUnion(friendlist)
                                                )
                                                Log.d(TAG, "이것도 성공")

                                            }

                                    }
                                }
                            }
                        }
                }

            }

        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount() = friend_accept.size

    }

    inner class FriendRequestAdapter(private val context: MutableList<Friends>) :
        RecyclerView.Adapter<FriendRequestAdapter.ViewHolder>() {
        private var friend_request = mutableListOf<Friends>()

        init {  // Account의 문서를 불러온 뒤 Friend으로 변환해 ArrayList에 담음
            firestore?.collection("Account")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    friend_request.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Friends::class.java)
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

            private val RequestName: TextView = itemView.findViewById(R.id.RequestName)
            private val RequestColor: ImageView = itemView.findViewById(R.id.RequestColor)
            private val RequestBtn: Button = itemView.findViewById(R.id.RequestBtn)

            fun setFriendRequestName(item: Friends) {
                RequestName.text = item.userName
            }

            fun SetFriendRequestColor(item: Friends) {
                var circleResource = 0
                when (item.userColor) {
                    "f69b94" -> circleResource = R.drawable.b_f69b94
                    "f8c8c4" -> circleResource = R.drawable.b_f8c8c4
                    "fcdcce" -> circleResource = R.drawable.b_fcdcce
                    "96b0e5" -> circleResource = R.drawable.b_96b0e5
                    "92b9e2" -> circleResource = R.drawable.b_92b9e2
                    "ebc0c7" -> circleResource = R.drawable.b_ebc0c7
                    "7bb6c8" -> circleResource = R.drawable.b_7bb6c8
                    "aad3d7" -> circleResource = R.drawable.b_aad3d7
                    "f5f1f0" -> circleResource = R.drawable.b_f5f1f0
                    "d5e3e6" -> circleResource = R.drawable.b_d5e3e6
                    "f2a4b1" -> circleResource = R.drawable.b_f2a4b1
                    "7175a5" -> circleResource = R.drawable.b_7175a5
                    "a1b3d7" -> circleResource = R.drawable.b_a1b3d7
                    "bd83cf" -> circleResource = R.drawable.b_bd83cf
                    "e5afe9" -> circleResource = R.drawable.b_e5afe9

                }
                RequestColor.setImageResource(circleResource)
            }

                fun RequestBtnOnclick(item: Friends) {
                    RequestBtn.setOnClickListener {

                        var myDocRef = firestore?.collection("Account")?.document("$currentUser")
                        myDocRef!!.get()
                            .addOnSuccessListener { document ->
                                if (document.get("Friends") != null) {
                                    val hashMap: ArrayList<Map<String, String>> =
                                        document.get("Friends") as ArrayList<Map<String, String>>
                                    for (keys in hashMap) {
                                        val friendlist: MutableMap<String, Any> = HashMap()
                                        val mylist: MutableMap<String, Any> = HashMap()
                                        val key = keys.keys.iterator().next()
                                        if (keys[key] == "request") {
                                            val friendDocRef =
                                                firestore?.collection("Account")?.document(key)
                                            friendDocRef!!.get()
                                                .addOnSuccessListener { document ->
                                                    mylist[key] = "request"
                                                    myDocRef.update(
                                                        "Friends",
                                                        FieldValue.arrayRemove(mylist)
                                                    )

                                                    friendlist[currentUser.toString()] = "accept"
                                                    friendDocRef.update(
                                                        "Friends",
                                                        FieldValue.arrayRemove(friendlist)
                                                    )

                                                    Log.d(TAG, "이것도 성공")

                                                }

                                        }
                                    }
                                }
                            }
                    }

                }

            }

            // 리사이클러뷰의 아이템 총 개수 반환
            override fun getItemCount() = friend_request.size

        }


    }









