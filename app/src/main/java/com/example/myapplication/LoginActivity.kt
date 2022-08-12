package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    lateinit var emailEt: EditText
    lateinit var pwEt: EditText
    private val RC_SIGN_IN=9001
    //firebase auth
    private var firebaseAuth: FirebaseAuth ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEt=findViewById(R.id.loginEmail)
        pwEt=findViewById(R.id.loginPW)

        firebaseAuth = Firebase.auth

        loginButton.setOnClickListener {
            signIn(loginEmail.text.toString(), loginPW.text.toString())
        }

        joinButton.setOnClickListener{
            startActivity(Intent(this, JoinActivity::class.java))
        }

        googleLogin.setOnClickListener{
            startActivity(Intent(this, GoogleLogin::class.java))
        }

        facebookLogin.setOnClickListener {
            startActivity(Intent(this, FacebookLogin::class.java))
        }
    }

    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
        toMainActivity(firebaseAuth?.currentUser)
    }

    // 로그인
    private fun signIn(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(
                            baseContext, "로그인에 성공 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        moveProfilePage(firebaseAuth?.currentUser)
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
    fun moveProfilePage(user: FirebaseUser?){
        if( user!= null){
            startActivity(Intent(this, ProfileActivity::class.java))
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
}



