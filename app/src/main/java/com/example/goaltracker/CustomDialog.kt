package com.example.goaltracker

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.profile_setting.*

class CustomDialog(context: Context) {
    private val dialog = Dialog(context)
    var click : Boolean ?= false
    var firebaseAuth: FirebaseAuth?=null
    var fireStore : FirebaseFirestore?=null
    lateinit var edit_profile_color : String
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    fun showDialog() {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()

        dialog.setContentView(R.layout.profile_setting)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val edit_nick = dialog.findViewById<EditText>(R.id.pfChangeName)

        dialog.s_f69b94.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_f69b94.setBackgroundResource(R.drawable.p_f69b94)
            click=true
            edit_profile_color="f69b94"
        }

        dialog.s_f8c8c4.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_f8c8c4.setBackgroundResource(R.drawable.p_f8c8c4)
            click=true
            edit_profile_color="f8c8c4"
        }

        dialog.s_fcdcce.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_fcdcce.setBackgroundResource(R.drawable.p_fcdcce)
            click=true
            edit_profile_color="fcdcce"
        }

        dialog.s_96b0e5.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_96b0e5.setBackgroundResource(R.drawable.p_96b0e5)
            click=true
            edit_profile_color="96b0e5"
        }

        dialog.s_92b9e2.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_92b9e2.setBackgroundResource(R.drawable.p_92b9e2)
            click=true
            edit_profile_color="92b9e2"
        }

        dialog.s_ebc0c7.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_ebc0c7.setBackgroundResource(R.drawable.p_ebc0c7)
            click=true
            edit_profile_color="ebc0c7"
        }

        dialog.s_7bb6c8.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_7bb6c8.setBackgroundResource(R.drawable.p_7bb6c8)
            click=true
            edit_profile_color="7bb6c8"
        }

        dialog.s_aad3d7.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_aad3d7.setBackgroundResource(R.drawable.p_aad3d7)
            click=true
            edit_profile_color="aad3d7"
        }

        dialog.s_f5f1f0.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_f5f1f0.setBackgroundResource(R.drawable.p_f5f1f0)
            click=true
            edit_profile_color="f5f1f0"
        }

        dialog.s_d5e3e6.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_d5e3e6.setBackgroundResource(R.drawable.p_d5e3e6)
            click=true
            edit_profile_color="d5e3e6"
        }

        dialog.s_f2a4b1.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_f2a4b1.setBackgroundResource(R.drawable.p_f2a4b1)
            click=true
            edit_profile_color="f2a4b1"
        }

        dialog.s_7175a5.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_7175a5.setBackgroundResource(R.drawable.p_7175a5)
            click=true
            edit_profile_color="7175a5"
        }

        dialog.s_a1b3d7.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_a1b3d7.setBackgroundResource(R.drawable.p_a1b3d7)
            click=true
            edit_profile_color="a1b3d7"
        }

        dialog.s_bd83cf.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_bd83cf.setBackgroundResource(R.drawable.p_bd83cf)
            click=true
            edit_profile_color="bd83cf"
        }

        dialog.s_e5afe9.setOnClickListener {
            if (click==true) {
                initializeProfile()
                click=false
            }
            dialog.s_e5afe9.setBackgroundResource(R.drawable.p_e5afe9)
            click=true
            edit_profile_color="e5afe9"
        }

        dialog.pfChangeButton.setOnClickListener {
            // 프로필 색상 및 닉네임 수정 코드 추가 필요
            if (edit_nick == null) {
                
            } else {

                var accountUId: String? = ""
                accountUId = firebaseAuth?.currentUser?.uid.toString()

                curUser.Email = firebaseAuth?.currentUser?.email.toString()
                Log.d("이름", edit_nick.toString())
                curUser.UserName = edit_nick.toString()
                curUser.UserColor = edit_profile_color

                var curUserAccount = fireStore?.collection("Account")?.document(accountUId)
            }
            dialog.dismiss()
        }
    }

    interface OnDialogClickListener{
        fun onClicked(name: String)
    }

    private fun initializeProfile() {  // 프로필 색상 선택 초기화 함수
        dialog.s_f69b94.setBackgroundResource(R.drawable.b_f69b94)
        dialog.s_f8c8c4.setBackgroundResource(R.drawable.b_f8c8c4)
        dialog.s_fcdcce.setBackgroundResource(R.drawable.b_fcdcce)
        dialog.s_96b0e5.setBackgroundResource(R.drawable.b_96b0e5)
        dialog.s_92b9e2.setBackgroundResource(R.drawable.b_92b9e2)
        dialog.s_ebc0c7.setBackgroundResource(R.drawable.b_ebc0c7)
        dialog.s_7bb6c8.setBackgroundResource(R.drawable.b_7bb6c8)
        dialog.s_aad3d7.setBackgroundResource(R.drawable.b_aad3d7)
        dialog.s_f5f1f0.setBackgroundResource(R.drawable.b_f5f1f0)
        dialog.s_d5e3e6.setBackgroundResource(R.drawable.b_d5e3e6)
        dialog.s_f2a4b1.setBackgroundResource(R.drawable.b_f2a4b1)
        dialog.s_7175a5.setBackgroundResource(R.drawable.b_7175a5)
        dialog.s_a1b3d7.setBackgroundResource(R.drawable.b_a1b3d7)
        dialog.s_bd83cf.setBackgroundResource(R.drawable.b_bd83cf)
        dialog.s_e5afe9.setBackgroundResource(R.drawable.b_e5afe9)
    }
}