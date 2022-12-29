package com.example.goaltracker

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.change_pw_dialog.*
import kotlinx.android.synthetic.main.profile_setting.*

class ChangePWDialog(context: Context) {
    private val dialog = Dialog(context)
    var firebaseAuth: FirebaseAuth?=null
    var fireStore : FirebaseFirestore?=null
    lateinit var profile_color : String
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    fun showDialog() {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()

        dialog.setContentView(R.layout.change_pw_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        var profileName = dialog.findViewById<TextView>(R.id.changePW_profileName)
        val confirmPW = dialog.findViewById<EditText>(R.id.confirm_PW)
        val dialogLayout = dialog.findViewById<ConstraintLayout>(R.id.change_pw_dialog)

        var accountUId: String? = ""
        accountUId = firebaseAuth?.currentUser?.uid.toString()
        if (accountUId == null) {
            throw Exception("현재 로그인 한 유저가 없습니다.")
        }

        fireStore?.collection("Account")?.document(accountUId)?.get()?.addOnSuccessListener {
            var curUser = it.toObject(Account::class.java)
            var userNickname = curUser?.userName.toString()
            profileName.text = userNickname[0].toString()
        }

        dialog.changePW_Btn.setOnClickListener {

            firebaseAuth!!.sendPasswordResetEmail(confirmPW.text.toString()).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    var snackbar = Snackbar.make(dialogLayout, "비밀번호 재설정 이메일을 성공적으로 보냈습니다.", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    dialog.dismiss()
                } else {
                    var snackbar = Snackbar.make(dialogLayout, "이메일이 일치하지 않습니다.", Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
        }
    }

    interface OnDialogClickListener{
        fun onClicked(name: String)
    }
}