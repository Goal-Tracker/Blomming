package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    lateinit var name: EditText
    lateinit var backgroundColor: String
    var click : Boolean ?= false
    var firebaseAuth: FirebaseAuth?=null
    var fireStore : FirebaseFirestore?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name=findViewById(R.id.joinName)
        firebaseAuth= FirebaseAuth.getInstance()
        fireStore= FirebaseFirestore.getInstance()

        backtologin2.setOnClickListener {
            finish()
        }

        c_f69b94.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
        }
        c_f8c8c4.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#f8c8c4"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_fcdcce.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#fcdcce"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_96b0e5.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#96b0e5"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_92b9e2.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#92b9e2"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_ebc0c7.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#ebc0c7"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_7bb6c8.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#7bb6c8"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_aad3d7.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#aad3d7"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_f5f1f0.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#f5f1f0"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_d5e3e6.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#d5e3e6"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_f2a4b1.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#f2a4b1"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_7175a5.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#7175a5"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_a1b3d7.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#a1b3d7"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_bd83cf.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#bd83cf"
            Log.d("ProfileActivity", backgroundColor)
        }
        c_e5afe9.setOnClickListener {
            c_f69b94.setBackgroundResource(R.drawable.click_profile)
            backgroundColor="#e5afe9"
            Log.d("ProfileActivity", backgroundColor)
        }

        pfSettingButton.setOnClickListener {
            if (name==null) {
                Toast.makeText(
                    baseContext, "이름을 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {  //user account 프로필 색상 설정 해줘야함

                var accountName:String ?= ""
                accountName=firebaseAuth?.currentUser?.uid.toString()
                var currentAccount = fireStore?.collection("account")?.document(accountName)
                currentAccount?.update("Username", name)

                startActivity(Intent(this, MainActivity::class.java))
            }
        }

    }


}