package com.example.goaltracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_friend.*
import kotlinx.android.synthetic.main.item_friend_add.view.*


class AddFriendActivity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null
    private val uid = Firebase.auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        val user : String = intent.getStringExtra("Username").toString()
        var myname : Any? = null
        FirebaseFirestore.getInstance()
            .collection("Account")
            .document(user)
            .get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    myname = it.result?.get("Username").toString()
                }
            }

        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()

        FriendAddRecyclerView.adapter = RecyclerViewAdapter(this, this)
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

    inner class RecyclerViewAdapter(
        val context: Context,
        val model: AddFriendActivity
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Friend 클래스 ArrayList 생성성
        var friend_add : ArrayList<Friends> = arrayListOf()

        init {  // Account의 문서를 불러온 뒤 Friend으로 변환해 ArrayList에 담음
            firestore?.collection("Account")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
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
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend_add, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView
            viewHolder.AddName.text = friend_add[position].UserName
            viewHolder.AddEmail.text = friend_add[position].Email
            viewHolder.AddBtn.setOnClickListener {
                FirebaseFirestore.getInstance()
                    .collection("Account")
                    .get()
                    .addOnSuccessListener { querysnapshot ->
                        if (querysnapshot != null) {
                            for (dc in querysnapshot.documents) {
                                val Username = dc["UserName"].toString()
                                val uid = dc["userId"].toString()
                                val map = mutableMapOf<String, Any>()
                                map["nickname"] = Username
                                FirebaseFirestore.getInstance()
                                    .collection("Account")
                                    .document(uid)
                                    .collection("friends")
                                    .document(uid)
                                    .set(map)
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