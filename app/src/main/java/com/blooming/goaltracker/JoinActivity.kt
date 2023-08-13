package com.blooming.goaltracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {

    lateinit var pwCheck_error : TextView
    var firebaseAuth: FirebaseAuth?=null
    var fireStore : FirebaseFirestore?=null
    var email : String ?= null
    var mailSent: Boolean = false

    val actionCodeSettings = actionCodeSettings {
        url = "https://www.example.com/finishSignUp?cartId=1234"
        // This must be true
        handleCodeInApp = true
        setAndroidPackageName(
            "com.blooming.goaltracker",
            true, // installIfNotAvailable
            "12", // minimumVersion
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

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
                var password = signuppw.text.toString()
                var passwordCheck = pwCheck.text.toString()
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
            val user = Firebase.auth.currentUser
            Log.d("user: ", user.toString())
            if (user==null){
                signinAndSignup()
            } else {
                user.reload().addOnSuccessListener {
                    if (mailSent) {
                        if (!user?.isEmailVerified!!){
                            joinCheckButton.setBackgroundColor(R.drawable.login_button)
                            joinCheckButton.setText("이메일 인증 링크를 눌러주세요.")
                        } else {
                            joinCheckButton.setText("로그인 완료하기")
                            joinCheckButton.isEnabled = true
                            createUserAccount()
                        }
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val user = Firebase.auth.currentUser
        if (user != null) {
            user.reload().addOnSuccessListener {
                Log.d("reload", "success")
                if (mailSent) {
                    if (!user?.isEmailVerified!!){
                        joinCheckButton.setBackgroundColor(R.drawable.login_button)
                        joinCheckButton.setText("이메일 인증 링크를 눌러주세요.")
                        joinCheckButton.isEnabled = false
                    } else {
                        joinCheckButton.setText("로그인 완료하기")
                        joinCheckButton.isEnabled = true
                    }
                }
            }
        } else {
            Log.d("reload", "failed")
        }
    }

    private fun signinAndSignup() {
        if (signupID.text != null && signuppw.text != null) {
            var email = signupID.text.toString()
            var password = signuppw.text.toString()
            var passwordCheck = pwCheck.text.toString()
            if (password == passwordCheck) {
                firebaseAuth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnSuccessListener { task ->
                        backtologin.isEnabled = false
                        val user = Firebase.auth.currentUser
                        user?.sendEmailVerification()?.addOnSuccessListener{ sendTask ->
                            Toast.makeText(this, "인증 메일을 보냈습니다.", Toast.LENGTH_SHORT).show()
                            mailSent=true
                        }?.addOnFailureListener {
                            Toast.makeText(this, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }?.addOnFailureListener {
                        Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        } else if (signuppw.text==null){
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

    fun createUserAccount(){
        var user = firebaseAuth?.currentUser
        if (user != null) {
            if (user.isEmailVerified) {
                var userAccount = Account()
                var accountName : String ?= ""

                accountName=user.uid.toString()
                userAccount.email= user.email.toString()

                fireStore?.collection("Account")?.document(accountName)?.set(userAccount)
                Toast.makeText(this, "계정 생성 완료", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}