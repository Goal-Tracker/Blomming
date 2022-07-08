package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginTest():AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        googleLogin.setOnClickListener{
            startActivity(Intent(this, GoogleLogin::class.java))
        }

        facebookLogin.setOnClickListener {
            startActivity(Intent(this, FacebookLogin::class.java))
        }
    }
}