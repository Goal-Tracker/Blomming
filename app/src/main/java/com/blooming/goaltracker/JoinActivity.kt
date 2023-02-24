package com.blooming.goaltracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {

    lateinit var signupID : EditText
    //lateinit var emailID_error : TextView
    lateinit var signupPW : EditText
    lateinit var pwCheck : EditText
    lateinit var pwCheck_error : TextView
    var firebaseAuth: FirebaseAuth?=null
    var fireStore : FirebaseFirestore?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        signupID=findViewById(R.id.signupID)
        //emailID_error=findViewById(R.id.emailID_error)
        signupPW=findViewById<EditText?>(R.id.signupPW)
        pwCheck=findViewById(R.id.signupPWcheck)
        pwCheck_error=findViewById(R.id.pwCheck_error)

        firebaseAuth= FirebaseAuth.getInstance()
        fireStore= FirebaseFirestore.getInstance()

        backtologin.setOnClickListener {
            
            finish()
        }

        pwCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                var password = signupPW.text.toString()
                var passwordCheck = signupPWcheck.text.toString()
                if (password != passwordCheck) {
                    pwCheck_error.setText("비밀번호가 일치하지 않습니다.") // 경고 메세지
                    pwCheck.setBackgroundResource(R.drawable.red_edittext) // 적색 테두리 적용
                } else {
                    pwCheck_error.setText("") //에러 메세지 제거
                    pwCheck.setBackgroundResource(R.drawable.pwcheck_edittext) //테투리 흰색으로 변경
                }
            } // afterTextChanged()..
        })

        joinCheckButton.setOnClickListener {
            signinAndSignup()
        }



    }

    fun signinAndSignup() {
        if (signupID.text != null && signupPW.text != null) {
            var password = signupPW.text.toString()
            var passwordCheck = pwCheck.text.toString()
            if (password == passwordCheck) {
                firebaseAuth?.createUserWithEmailAndPassword(signupID.text.toString(), signupPW.text.toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //account db 저장
                            var userAccount = Account()
                            var accountName : String ?= ""

                            accountName=firebaseAuth?.currentUser?.uid.toString()
                            userAccount.email= firebaseAuth?.currentUser?.email.toString()

                            fireStore?.collection("Account")?.document(accountName)?.set(userAccount)
                            Toast.makeText(this, "계정 생성 완료", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, ProfileActivity::class.java))
                            finish()
                        } else if(task.exception?.message.isNullOrEmpty()) {
                            // Show the error message
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                            Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show()
                        } else {
                            // Alert if you have account
                            Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        } else if (signupPW.text==null){
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else if (signupID.text==null){
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        } else if (pwCheck.text==null){
            Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

}