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

        backToJoin.setOnClickListener {
            finish()
        }

        c_f69b94.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_f69b94.setBackgroundResource(R.drawable.p_f69b94)
            click=true
            backgroundColor="f69b94"
        }
        c_f8c8c4.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_f8c8c4.setBackgroundResource(R.drawable.p_f8c8c4)
            click=true
            backgroundColor="f8c8c4"
        }
        c_fcdcce.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_fcdcce.setBackgroundResource(R.drawable.p_fcdcce)
            click=true
            backgroundColor="fcdcce"
        }
        c_96b0e5.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_96b0e5.setBackgroundResource(R.drawable.p_96b0e5)
            click=true
            backgroundColor="96b0e5"
        }
        c_92b9e2.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_92b9e2.setBackgroundResource(R.drawable.p_92b9e2)
            click=true
            backgroundColor="92b9e2"
        }
        c_ebc0c7.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_ebc0c7.setBackgroundResource(R.drawable.p_ebc0c7)
            click=true
            backgroundColor="ebc0c7"
        }
        c_7bb6c8.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_7bb6c8.setBackgroundResource(R.drawable.p_7bb6c8)
            click=true
            backgroundColor="7bb6c8"
        }
        c_aad3d7.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_aad3d7.setBackgroundResource(R.drawable.p_aad3d7)
            click=true
            backgroundColor="aad3d7"
        }
        c_f5f1f0.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_f5f1f0.setBackgroundResource(R.drawable.p_f5f1f0)
            click=true
            backgroundColor="f5f1f0"
        }
        c_d5e3e6.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_d5e3e6.setBackgroundResource(R.drawable.p_d5e3e6)
            click=true
            backgroundColor="d5e3e6"
        }
        c_f2a4b1.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_f2a4b1.setBackgroundResource(R.drawable.p_f2a4b1)
            click=true
            backgroundColor="f2a4b1"
        }
        c_7175a5.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_7175a5.setBackgroundResource(R.drawable.p_7175a5)
            click=true
            backgroundColor="7175a5"
        }
        c_a1b3d7.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_a1b3d7.setBackgroundResource(R.drawable.p_a1b3d7)
            click=true
            backgroundColor="a1b3d7"
        }
        c_bd83cf.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_bd83cf.setBackgroundResource(R.drawable.p_bd83cf)
            click=true
            backgroundColor="bd83cf"
        }
        c_e5afe9.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            c_e5afe9.setBackgroundResource(R.drawable.p_e5afe9)
            click=true
            backgroundColor="e5afe9"
        }

        pfSettingButton.setOnClickListener {
            if (name==null) {
                Toast.makeText(
                    baseContext, "이름을 입력해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                var accountName:String ?= ""
                accountName=firebaseAuth?.currentUser?.uid.toString()
                var currentAccount = fireStore?.collection("account")?.document(accountName)
                currentAccount?.update("Username", name)
                currentAccount?.update("UserColor", backgroundColor)

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }

    fun initializeProfile() {  // 프로필 색상 선택 초기화 함수
        c_f69b94.setBackgroundResource(R.drawable.b_f69b94)
        c_f8c8c4.setBackgroundResource(R.drawable.b_f8c8c4)
        c_fcdcce.setBackgroundResource(R.drawable.b_fcdcce)
        c_96b0e5.setBackgroundResource(R.drawable.b_96b0e5)
        c_92b9e2.setBackgroundResource(R.drawable.b_92b9e2)
        c_ebc0c7.setBackgroundResource(R.drawable.b_ebc0c7)
        c_7bb6c8.setBackgroundResource(R.drawable.b_7bb6c8)
        c_aad3d7.setBackgroundResource(R.drawable.b_aad3d7)
        c_f5f1f0.setBackgroundResource(R.drawable.b_f5f1f0)
        c_d5e3e6.setBackgroundResource(R.drawable.b_d5e3e6)
        c_f2a4b1.setBackgroundResource(R.drawable.b_f2a4b1)
        c_7175a5.setBackgroundResource(R.drawable.b_7175a5)
        c_a1b3d7.setBackgroundResource(R.drawable.b_a1b3d7)
        c_bd83cf.setBackgroundResource(R.drawable.b_bd83cf)
        c_e5afe9.setBackgroundResource(R.drawable.b_e5afe9)
    }

}