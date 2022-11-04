package com.example.goaltracker



import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_friend.*



class AddFriendActivity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null
    private val currentUser = Firebase.auth.currentUser?.uid
    var searchcount = 0
    private lateinit var dialog : ReportDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MySharedPreferences.getTheme(this))
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
    fun getCount(){
        var text_count = findViewById<TextView>(R.id.search_count)
        text_count.text =  ""+ searchcount + "명"
    }



    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Friend 클래스 ArrayList 생성성
        var friend_add: ArrayList<FriendAdd> = arrayListOf()


        init {  // Account의 문서를 불러온 뒤 Friend으로 변환해 ArrayList에 담음
            firestore?.collection("Account")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    friend_add.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(FriendAdd::class.java)
                        friend_add.add(item!!)
                    }
                    searchcount = friend_add.size
                    getCount()
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

            var account = friend_add[position]

            //아이템을 클릭하면 다이얼로그 생성
            holder.itemView.setOnClickListener {
                callDialog(it.context, account)
            }
        }



        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val AddName: TextView = itemView.findViewById(R.id.AddName)
            private val AddEmail: TextView = itemView.findViewById(R.id.AddEmail)
            private val AddColor: ImageView = itemView.findViewById(R.id.AddColor)
            private val AddBtn: Button = itemView.findViewById(R.id.AddBtn)


            fun SetFriendAddName(item: FriendAdd) {
                AddName.text = item.userName
            }


            fun SetFriendAddColor(item: FriendAdd) {
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

            fun SetFriendAddEmail(item: FriendAdd) {
                AddEmail.text = item.email
            }

            //0: ADD(친구된 상태) 1:ACCEPT(친구허락) 2: REQUEST(신청 보냄)
            fun AddFriendBtnOnclick(item: FriendAdd) {
                AddBtn.setOnClickListener {
                    if(item.uid != currentUser) {
                        val friendDocRef =
                            firestore?.collection("Account")?.document(item.uid.toString())
                        val myDocRef = firestore?.collection("Account")?.document("$currentUser")
                        friendDocRef!!.get()
                            .addOnSuccessListener { friendDoc ->
                                val friendlist: MutableMap<String, Any> = HashMap()
                                myDocRef!!.get()
                                    .addOnSuccessListener { myDoc ->
                                        val mylist: MutableMap<String, Any> = HashMap()


                                        // 친구 리스트가 없는 경우
                                        if (myDoc.get("Friends") == null) {

                                            mylist[item.uid!!] = "request"
                                            // 배열 요소 업데이트
                                            myDocRef.update(
                                                "Friends",
                                                FieldValue.arrayUnion(mylist)
                                            )

                                            friendlist[currentUser.toString()] = "accept"
                                            // 배열 요소 업데이트
                                            friendDocRef.update(
                                                "Friends",
                                                FieldValue.arrayUnion(friendlist)
                                            )

                                            Toast.makeText(
                                                this@AddFriendActivity,
                                                "친구 신청을 보냈습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        // 친구 리스트가 있는 경우
                                        else {
                                            val hashMap: ArrayList<Map<String, String>> =
                                                myDoc.get("Friends") as ArrayList<Map<String, String>>
                                            var notFriend = true
                                            // 이미 신청 보냄
                                            for (keys in hashMap) {
                                                if (keys == mapOf(item.uid to "request")) {
                                                    Toast.makeText(
                                                        this@AddFriendActivity,
                                                        "이미 친구 신청을 보냈습니다.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    notFriend = false

                                                }
                                            }
                                            // 친구 아닐 때
                                            if (notFriend) {
                                                mylist[item.uid!!] = "request"
                                                myDocRef.update(
                                                    "Friends",
                                                    FieldValue.arrayUnion(mylist)
                                                )

                                                friendlist[currentUser.toString()] = "accept"
                                                friendDocRef.update(
                                                    "Friends",
                                                    FieldValue.arrayUnion(friendlist)
                                                )

                                                Toast.makeText(
                                                    this@AddFriendActivity,
                                                    "친구 신청을 보냈습니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this@AddFriendActivity,
                                            "친구 신청을 실패하였습니다",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                    } else{
                        Toast.makeText(
                            this@AddFriendActivity,
                            "자기자신을 친구로 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return friend_add.size
        }

        fun callDialog(context: Context, item: FriendAdd){
            dialog = ReportDialog(
                context = context,
                userColor = item.userColor,
                userName = item.userName,
                email = item.email,
                namebtnListener = reNameListener,
                messagebtnListener = reMessageListener,
                blockbtnListener = blockbtnListener)


            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        private val reNameListener = View.OnClickListener{
            Toast.makeText(
                this@AddFriendActivity,
                "닉네임 신고가 되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        private val reMessageListener = View.OnClickListener{
            Toast.makeText(
                this@AddFriendActivity,
                "상태 메세지 신고가 되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        private val blockbtnListener = View.OnClickListener{
            Toast.makeText(
                this@AddFriendActivity,
                "차단 되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }


        // 파이어스토어에서 데이터를 불러와서 검색어가 있는지 판단
        fun search(searchWord: String) {
            firestore?.collection("Account")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                friend_add.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.getString("email")!!.contains(searchWord) || snapshot.getString("userName")!!.contains(searchWord)) {
                        var item = snapshot.toObject(FriendAdd::class.java)
                        friend_add.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
        }
    }

}