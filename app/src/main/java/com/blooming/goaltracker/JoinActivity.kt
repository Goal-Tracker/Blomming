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
    var isEmailVerified : Boolean = false

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

        signupID.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                email = signupID.text.toString()
                if (isEmailVerified){
                    email_verify.setText("인증되었습니다.")
                }
            } // afterTextChanged()..
        })

        sendEmailButton.setOnClickListener {
            Toast.makeText(this, "인증 메일을 보냈습니다.", Toast.LENGTH_SHORT).show()
            verifyEmail(email)
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
            signinAndSignup()
        }

    }

    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser
        if (user!=null){
            user.reload().addOnSuccessListener {
                Log.d("reload", "success")
            }
        } else {
            Log.d("reload", "failed")
        }

    }

    fun signinAndSignup() {
        if (signupID.text != null && signuppw.text != null) {
            var password = signuppw.text.toString()
            var passwordCheck = pwCheck.text.toString()
            if ((password == passwordCheck) && isEmailVerified==true) {
                val user = Firebase.auth.currentUser
                user!!.updatePassword(signuppw.text.toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var userAccount = Account()
                            var accountName : String ?= ""

                            accountName=user.uid.toString()
                            userAccount.email= user.email.toString()

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
        } else if (signuppw.text==null){
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else if (signupID.text==null){
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        } else if (pwCheck.text==null){
            Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    fun verifyEmail(email: String?){
        if (email!=null){
            val randomString = getRandomString(10)
            firebaseAuth?.useAppLanguage()
            firebaseAuth?.createUserWithEmailAndPassword(email, randomString)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = Firebase.auth.currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener{ sendTask ->
                            if (sendTask.isSuccessful){
                                isEmailVerified=true
                                if (user.isEmailVerified){
                                    Log.d("유효성 검증", "성공")
                                    isEmailVerified=true
                                }
                            } else {
                                Toast.makeText(this, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                                //계정 삭제 코드 구현해야함
                            }
                        }
                    } else if(task.exception?.message.isNullOrEmpty()) {
                        // Show the error message
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show()
                    } else {
                        // Alert if you have account
                        Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

    fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

}