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
import androidx.core.widget.addTextChangedListener
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
        val change_PW = dialog.findViewById<EditText>(R.id.change_PW)
        val changePWconfirm = dialog.findViewById<EditText>(R.id.change_PW_confirm)

        val pwConfirm_error = dialog.findViewById<TextView>(R.id.pwConfirm_error)
        val pwChange_error = dialog.findViewById<TextView>(R.id.pwChange_error)

        var successVerifyPW : Boolean
        successVerifyPW = false

        var accountUId: String? = ""
        accountUId = firebaseAuth?.currentUser?.uid.toString()
        if (accountUId == null) {
            throw Exception("현재 로그인 한 유저가 없습니다.")
        }

        fireStore?.collection("Account")?.document(accountUId)?.get()?.addOnSuccessListener {
            var curUser = it.toObject(Account::class.java)
            var userNickname = curUser?.UserName.toString()
            profileName.text = userNickname[0].toString()
        }

//        confirmPW.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable) {
//                var email:String?=""
//                email = firebaseAuth?.currentUser?.email.toString()
//                var password:String?=""
//                password = confirmPW.text.toString()
//                if (confirmPW!=null) {
//                    firebaseAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener() {
//                        if (it.isSuccessful) {
//                            pwConfirm_error.setText("비밀번호가 일치합니다.")
////                            confirmPW.setBackgroundResource(R.color.green_edittext)
//                            successVerifyPW=true
//                        }
//                        if (it.isCanceled){
//                            pwConfirm_error.setText("비밀번호가 일치하지 않습니다.") // 경고 메세지
//                            confirmPW.setBackgroundResource(R.drawable.red_edittext) // 적색 테두리 적용
//                            return@addOnCompleteListener
//                        }
//                    }
//                }
//            } // afterTextChanged()..
//        })

        changePWconfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                changePWconfirm.setHint("")

                var password:String?=""
                password = change_PW.text.toString()
                var passwordConfirm:String?=""
                passwordConfirm = changePWconfirm.text.toString()

                if (password!=null && passwordConfirm!=null) {
                    if (!password.equals(passwordConfirm)) {
                        pwChange_error.setText("변경할 비밀번호가 일치하지 않습니다.") // 경고 메세지
                        changePWconfirm.setBackgroundResource(R.drawable.red_edittext) // 적색 테두리 적용
                    }
                    if (password.equals(passwordConfirm)) {
                        pwChange_error.setText("") //에러 메세지 제거
                        changePWconfirm.setBackgroundResource(R.drawable.pwcheck_edittext) //테투리 흰색으로 변경
                    }
                }
            } // afterTextChanged()..
        })

        dialog.changePW_Btn.setOnClickListener {
            // 비밀번호 변경 코드 추가
            var password:String?=""
            password = change_PW.text.toString()
            var passwordConfirm:String?=""
            passwordConfirm = changePWconfirm.text.toString()
            if (confirmPW!=null && change_PW!=null && changePWconfirm!=null) {
                if (password.equals(passwordConfirm)) {
                    changePassword(passwordConfirm!!)
                }
            }
        }
    }

    interface OnDialogClickListener{
        fun onClicked(name: String)
    }

    private fun changePassword(password:String) {
        firebaseAuth?.currentUser?.updatePassword(password)?.addOnCompleteListener() {
            if (it.isSuccessful) {
                dialog.dismiss()
            }
        }
    }
}