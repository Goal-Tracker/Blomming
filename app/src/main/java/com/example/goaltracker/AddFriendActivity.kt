package com.example.goaltracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.activity_add_friend.searchWord
import kotlinx.android.synthetic.main.activity_add_friend.*
import kotlinx.android.synthetic.main.item_friend_add.*
import kotlinx.android.synthetic.main.report_dialog.*
import java.util.*
import kotlin.collections.ArrayList


class AddFriendActivity : AppCompatActivity() {
    //파이어스토어
    var firestore: FirebaseFirestore? = null
    private val currentUser = Firebase.auth.currentUser?.uid

    var searchcount = 0  //사람 수
    private lateinit var dialog: ReportDialog  //다이얼로그
    lateinit var index: String
    lateinit var msgindex: String

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MySharedPreferences.getTheme(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        searchWord.addTextChangedListener(object : TextWatcher {

            //텍스트가 변경되기 바로 이전에 동작
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            //텍스트가 변경되는 동시에 동작
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            // 텍스트가 변경된 이후에 동작
            override fun afterTextChanged(editable: Editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                (FriendAddRecyclerView.adapter as RecyclerViewAdapter).search(searchWord.text.toString())
            }
        })

        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()

        FriendAddRecyclerView.adapter = RecyclerViewAdapter()
        FriendAddRecyclerView.layoutManager = LinearLayoutManager(this)


        //x버튼 누르면 main 화면으로 이동
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


        // 검색 옵션에 따라 검색
        searchBtn.setOnClickListener {
            (FriendAddRecyclerView.adapter as RecyclerViewAdapter).search(searchWord.text.toString())
        }


    }

    //인원 카운트
    fun getCount() {
        var text_count = findViewById<TextView>(R.id.search_count)
        text_count.text = "" + searchcount + "명"
    }


    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Friend 클래스 ArrayList 생성성
        var friend_add: ArrayList<Friend> = arrayListOf()

        init {  // Account의 문서를 불러온 뒤 Friend으로 변환해 ArrayList에 담음
            firestore?.collection("Account")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    friend_add.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Friend::class.java)
                        friend_add.add(item!!)
                    }


                    notifyDataSetChanged()
                }

        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_friend_add, parent, false)
            return ViewHolder(view)
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView
            holder.SetFriendAddName(friend_add[position])
            holder.SetFriendUid(friend_add[position])
            holder.SetProfileAddName(friend_add[position])
            holder.AddFriendBtnOnclick(friend_add[position])
            holder.SetFriendAddColor(friend_add[position])
            holder.SetFriendAddEmail(friend_add[position])

            var account = friend_add[position]

            //아이템을 클릭하면 다이얼로그 생성
            holder.itemView.setOnClickListener {
                callDialog(it.context, account)
            }

        }



        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val AddName: TextView = itemView.findViewById(R.id.AddName)
            private val Adduid: TextView = itemView.findViewById(R.id.Adduid)
            private val ProfileName: TextView = itemView.findViewById(R.id.AddProfileName)
            private val AddEmail: TextView = itemView.findViewById(R.id.AddEmail)
            private val AddColor: ImageView = itemView.findViewById(R.id.AddColor)
            private val AddBtn: Button = itemView.findViewById(R.id.AddBtn)

            //친구 추가 이름
            fun SetFriendAddName(item: Friend) {
                AddName.text = item.userName
            }

            //친구 uid
            fun SetFriendUid(item: Friend) {
                Adduid.text = item.uid
            }

            //친구 프로필 이름
            fun SetProfileAddName(item: Friend) {
                ProfileName.text = item.userName?.get(0).toString()
            }

            //친구 추가 프로필
            fun SetFriendAddColor(item: Friend) {
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
                AddColor.setImageResource(circleResource)
            }

            //친구 추가 이메일
            fun SetFriendAddEmail(item: Friend) {
                AddEmail.text = item.email
            }

            //친구 추가 버튼
            @SuppressLint("SuspiciousIndentation")
            fun AddFriendBtnOnclick(item: Friend) {
                AddBtn.setOnClickListener {
                    if (item.uid != currentUser) {

                        firestore?.collection("Account")?.document("$currentUser")
                            ?.collection("Friend")
                            ?.whereEqualTo("uid", item.uid.toString())?.get()
                            ?.addOnCompleteListener { task ->


                                // 친구 리스트가 없는 경우
                                if (task.result?.size() == 0) {
                                    // 내 친구 목록
                                    firestore?.collection("Account")?.document("$currentUser")
                                        ?.collection("Friend")
                                        ?.document("${item.uid}")
                                        ?.set(
                                            hashMapOf(
                                                "uid" to item.uid,
                                                "status" to "request",
                                                "userName" to item.userName,
                                                "email" to item.email,
                                                "userColor" to item.userColor

                                            )
                                        )
                                        ?.addOnSuccessListener {
                                            Toast.makeText(
                                                this@AddFriendActivity,
                                                "친구 신청을 보냈습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                        ?.addOnFailureListener {}

                                    // 상대방 친구 목록
                                    firestore?.collection("Account")?.document(item.uid.toString())
                                        ?.collection("Friend")
                                        ?.document("${currentUser}")
                                        ?.set(
                                            hashMapOf(
                                                "uid" to currentUser,
                                                "status" to "accept",
                                                "userName" to item.userName,
                                                "email" to item.email,
                                                "userColor" to item.userColor

                                            )
                                        )
                                        ?.addOnSuccessListener {
                                        }
                                        ?.addOnFailureListener {
                                        }

                                }

                                // 친구 리스트가 있는 경우
                                else {


                                    // 내 친구 목록
                                    firestore?.collection("Account")?.document("$currentUser")
                                        ?.collection("Friend")
                                        ?.document("${item.uid}")
                                        ?.set(
                                            hashMapOf(
                                                "uid" to item.uid,
                                                "status" to "request",
                                                "userName" to item.userName,
                                                "email" to item.email,
                                                "userColor" to item.userColor

                                            )
                                        )
                                        ?.addOnSuccessListener {
                                            Toast.makeText(
                                                this@AddFriendActivity,
                                                "친구 신청을 보냈습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                        ?.addOnFailureListener {}

                                    // 상대방 친구 목록
                                    firestore?.collection("Account")?.document(item.uid.toString())
                                        ?.collection("Friend")
                                        ?.document("${currentUser}")
                                        ?.set(
                                            hashMapOf(
                                                "uid" to currentUser,
                                                "status" to "accept",
                                                "userName" to item.userName,
                                                "email" to item.email,
                                                "userColor" to item.userColor

                                            )
                                        )
                                        ?.addOnSuccessListener {
                                        }
                                        ?.addOnFailureListener {
                                        }

                                }

                            }

                    } else {
                        Toast.makeText(
                            this@AddFriendActivity,
                            "자기자신을 친구로 추가할 수 없습니다.", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return friend_add.size
        }

        //관리자(신고)Dialog
        fun callDialog(context: Context, item: Friend) {

            dialog = ReportDialog(
                context = context,
                userColor = item.userColor,
                userName = item.userName,
                uid = item.uid,
                userMessage = item.userMessage,
                email = item.email!!,
                namebtnListener = reNameListener,
                messagebtnListener = reMessageListener,
                blockbtnListener = blockbtnListener
            )

            //다이얼로그 타이틀 및 테두리 없애기
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        //닉네임 신고 버튼 클릭 시
        private val reNameListener = View.OnClickListener {
            index = UUID.randomUUID().toString()
            firestore?.collection("Manager")?.document("$index")
                ?.set(
                    hashMapOf(
                        "userName" to dialog.userName,
                        "email" to dialog.email,
                        "index" to index,
                        "id" to dialog.uid,
                        )
                )

            Toast.makeText(
                this@AddFriendActivity,
                "닉네임 신고가 되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }


        //상태 메세지 신고 버튼 클릭 시
        private val reMessageListener = View.OnClickListener {
            msgindex = UUID.randomUUID().toString()
            firestore?.collection("Manager")?.document("$msgindex")
                ?.set(
                    hashMapOf(
                        "userMessage" to dialog.userMessage,
                        "email" to dialog.email,
                        "msgindex" to msgindex,
                        "id" to dialog.uid,
                    )
                )
            Toast.makeText(
                this@AddFriendActivity,
                "상태 메세지 신고가 되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }


        //차단 버튼 클릭 시
        private val blockbtnListener = View.OnClickListener {
            Toast.makeText(
                this@AddFriendActivity,
                "차단 되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }


        // 파이어스토어에서 데이터를 불러와서 검색어가 있는지 판단
        fun search(searchWord: String) {
            firestore?.collection("Account")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    friend_add.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        //에러처리(안 해주면 오류)
                        if (snapshot.getString("email") != null && snapshot.getString("userName") != null)
                            if (snapshot.getString("email")!!
                                    .contains(searchWord) || snapshot.getString("userName")!!
                                    .contains(searchWord)
                            ) {
                                val item = snapshot.toObject(Friend::class.java)
                                friend_add.add(item!!)
                            }
                    }
                    searchcount = friend_add.size
                    getCount()
                    notifyDataSetChanged()
                }
        }
    }

}