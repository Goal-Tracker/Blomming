package com.example.myapplication

import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_join.*


class JoinActivity : AppCompatActivity() {

    lateinit var signupID : EditText
    //lateinit var emailID_error : TextView
    lateinit var pwCheck : EditText
    lateinit var pwCheck_error : TextView
    var firebaseAuth: FirebaseAuth?=null
    var fireStore : FirebaseFirestore?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        signupID=findViewById(R.id.signupID)
        //emailID_error=findViewById(R.id.emailID_error)
        pwCheck=findViewById(R.id.signupPWcheck)
        pwCheck_error=findViewById(R.id.pwCheck_error)

        firebaseAuth= FirebaseAuth.getInstance()
        fireStore= FirebaseFirestore.getInstance()

        backtologin.setOnClickListener {
            finish()
        }

        /*
        signupID.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    emailID_error.setText("이메일 형식으로 입력해주세요.") // 경고 메세지
                    signupID.setBackgroundResource(R.drawable.red_edittext) // 적색 테두리 적용
                } else {
                    emailID_error.setText("") //에러 메세지 제거
                    signupID.setBackgroundResource(R.drawable.pwcheck_edittext) //테투리 흰색으로 변경
                }
            } // afterTextChanged()..

        })*/

        pwCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                var password = signupPW.text.toString()
                var passwordCheck = signupPWcheck.text.toString()
                if (password != passwordCheck) {
                    pwCheck_error.setText("이메일 형식으로 입력해주세요.") // 경고 메세지
                    pwCheck.setBackgroundResource(R.drawable.red_edittext) // 적색 테두리 적용
                } else {
                    pwCheck_error.setText("") //에러 메세지 제거
                    pwCheck.setBackgroundResource(R.drawable.pwcheck_edittext) //테투리 흰색으로 변경
                }
            } // afterTextChanged()..
        })

        joinCheckButton.setOnClickListener {
            createAccount(signupID.text.toString(), signupPW.text.toString(), signupPWcheck.text.toString())
            startActivity(Intent(this, ProfileActivity::class.java))
        }



    }

    private fun createAccount(email: String, password: String, passwordCheck:String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (password == passwordCheck) {
                firebaseAuth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //account db 연동
                            var userAccount = Account()
                            var accountName : String

                            accountName=firebaseAuth?.currentUser?.uid.toString()
                            userAccount.userId=firebaseAuth?.currentUser?.email
                            userAccount.password=password

                            fireStore?.collection("account")?.document(accountName)?.set(userAccount)
                            Toast.makeText(this, "계정 생성 완료", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        } else if (password.isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()){
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        } else if (passwordCheck.isEmpty()){
            Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
        }
    }


}