package com.example.goaltracker

import FriendAdd
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
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
import com.example.goaltracker.MainActivity
import com.example.goaltracker.R
import com.facebook.internal.CallbackManagerImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_friend_add.view.*
import kotlinx.android.synthetic.main.activity_add_friend.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddFriendActivity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null
    private val uid = Firebase.auth.currentUser?.uid
    //private var friendDocRef: DocumentReference? = null
    //private var myDocRef: DocumentReference? = null

    //private lateinit var viewModel: ShareViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

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

        searchWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                (FriendAddRecyclerView.adapter as RecyclerViewAdapter).search(searchWord.text.toString())
            }
        })

    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Friend 클래스 ArrayList 생성성
        var friend_add: ArrayList<Friends> = arrayListOf()

        init {  // Account의 문서를 불러온 뒤 Friend으로 변환해 ArrayList에 담음
            firestore?.collection("Account")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    friend_add.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(Friends::class.java)
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
            holder.AddFriendBtnOnclick(friend_add[position])
            holder.SetFriendAddColor(friend_add[position])
            holder.SetFriendAddEmail(friend_add[position])

        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val AddName: TextView = itemView.findViewById(R.id.AddName)
            private val AddEmail: TextView = itemView.findViewById(R.id.AddEmail)
            private val AddColor: ImageView = itemView.findViewById(R.id.AddColor)
            private val AddBtn: Button = itemView.findViewById(R.id.AddBtn)

            fun SetFriendAddName(item: Friends) {
                AddName.text = item.UserName
            }


            fun SetFriendAddColor(item: Friends) {
                Glide.with(itemView)
                    .load(item.UserColor).circleCrop()
                    .into(AddColor)
            }

            fun SetFriendAddEmail(item: Friends) {
                AddEmail.text = item.Email
            }

            //0: ADD(친구된 상태) 1:ACCEPT(친구허락) 2: REQUEST(신청 보냄)
            fun AddFriendBtnOnclick(item: Friends) {
                AddBtn.setOnClickListener {
                    if (item.Uid != uid) {
                        FirebaseFirestore.getInstance().collection("Account")
                            .document(FirebaseAuth.getInstance().uid.toString())
                            .collection("Friends")
                            .document(item.Uid.toString())
                            .get().addOnSuccessListener {
                                //이미 친구인지 확인
                                if (it.exists()) {
                                    Toast.makeText(this@AddFriendActivity, "이미 친구입니다.", Toast.LENGTH_SHORT).show()
                                } else {
                                    //자기자신과 친구못하게 막음
                                    if (item.Uid != FirebaseAuth.getInstance().uid.toString()) {
                                        Toast.makeText(
                                            this@AddFriendActivity, " '${item.UserName}' 님과 친구가 되었습니다.", Toast.LENGTH_SHORT).show()

                                        FirebaseFirestore.getInstance()
                                            .collection("Account")
                                            .document(FirebaseAuth.getInstance().uid.toString())
                                            .collection("Friends")
                                            .document(item.Uid.toString())
                                            .set(item)
                                    } else {
                                        Toast.makeText(
                                            this@AddFriendActivity,
                                            "자기자신을 친구로 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                                    }


                                }
                            }
                    }
                }
            }
        }




        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return friend_add.size
        }

        // 파이어스토어에서 데이터를 불러와서 검색어가 있는지 판단
        fun search(searchWord: String) {
            firestore?.collection("Account")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                friend_add.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.getString("Email")!!.contains(searchWord) || snapshot.getString("UserName")!!.contains(searchWord)) {
                        var item = snapshot.toObject(Friends::class.java)
                        friend_add.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
        }
    }

    }

