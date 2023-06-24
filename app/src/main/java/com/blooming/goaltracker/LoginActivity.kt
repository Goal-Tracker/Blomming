package com.blooming.goaltracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    lateinit var emailEt: EditText
    lateinit var pwEt: EditText
    private val RC_SIGN_IN=9001
    //firebase auth
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    private var curUser = Account()
    private var accountUId:String = ""
    private var changePWDialog : ChangePWDialog ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEt=findViewById(R.id.loginEmail)
        pwEt=findViewById(R.id.loginPW)

        emailEt.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && i==KeyEvent.KEYCODE_ENTER){
                pwEt.requestFocus()
                true
            }
            false
        }

        pwEt.setOnKeyListener { view, i, keyEvent ->
            if(i==KeyEvent.KEYCODE_ENTER){
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(pwEt.windowToken, 0)
                true
            }
            false
        }

        loginButton.setOnClickListener {
            signIn(loginEmail.text.toString(), loginPW.text.toString())
        }

        joinButton.setOnClickListener{
            startActivity(Intent(this, JoinActivity::class.java))
        }

        PWButton.setOnClickListener {
            changePWDialog = ChangePWDialog(this)
            changePWDialog?.showDialog()
            changePWDialog?.setOnClickListener(object: ChangePWDialog.OnDialogClickListener {
                override fun onClicked(name: String) {
                    Toast.makeText(this@LoginActivity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        googleLogin.isVisible = false

//        googleLogin.setOnClickListener{
//            startActivity(Intent(this, GoogleLogin::class.java))
//        }

//        facebookLogin.setOnClickListener {
//            startActivity(Intent(this, FacebookLogin::class.java))
//        }
    }

    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
        toMainActivity(auth?.currentUser)
    }

    // 로그인
    private fun signIn(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        accountUId = auth?.currentUser?.uid.toString()
                        Log.d("accountUId", accountUId)
                        db?.collection("Account")?.document(accountUId)?.get()?.addOnSuccessListener {
                            curUser = it.toObject(Account::class.java)!!
                            var color = curUser.userColor.toString()
                            MySharedPreferences.setUserId(this, accountUId)
                            MySharedPreferences.setUserEmail(this, curUser.email)
                            MySharedPreferences.setUserNickname(this, curUser.userName.toString())
                            MySharedPreferences.setUserColor(this, color)
                            MySharedPreferences.setUserColorInt(this, color)
                            MySharedPreferences.setTheme(this, color)
                            MySharedPreferences.setGoalList(this, curUser.myGoalList)

                            db.collection("Account")
                                .document(accountUId)
                                .collection("Friend")
                                .whereEqualTo("status", "friend")
                                .get()
                                .addOnSuccessListener { friendList ->
                                    Log.d("status가 friend인 문서 개수", friendList.size().toString())
                                    val friendDocuments: MutableList<DocumentSnapshot> = friendList.documents
                                    var friendsUId : ArrayList<String> ?= arrayListOf()
                                    for (document in friendDocuments) {
                                        friendsUId?.add(document.id)
                                    }
                                    if (friendsUId != null) {
                                        MySharedPreferences.setFriendList(this, friendsUId)
                                    }
                                    val list : ArrayList<String> =
                                        MySharedPreferences.getFriendList(this)
                                    if (list!=null) {
                                        for (value in list) {
                                            Log.d("mysharedpreferences에 저장된 friend", value)
                                        }
                                    }
                                }
                            moveMainPage(auth?.currentUser)
                        }
                        Toast.makeText(
                            baseContext, "로그인에 성공 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext, "로그인에 실패 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }


    // 유저정보 넘겨주고 프로필 설정 액티비티 호출
    fun moveMainPage(user: FirebaseUser?){
        if(user!= null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(
                baseContext, "사용자 정보가 없습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun toMainActivity(user: FirebaseUser?){
        if(user !=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun findPW() {
        changePWDialog?.showDialog()
        onBackPressed()
        changePWDialog?.setOnClickListener(object: ChangePWDialog.OnDialogClickListener {
            override fun onClicked(name: String) {
                Toast.makeText(this@LoginActivity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
}
