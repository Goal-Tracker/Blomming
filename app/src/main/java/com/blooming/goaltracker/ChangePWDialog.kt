package com.blooming.goaltracker

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.change_pw_dialog.*
import kotlinx.android.synthetic.main.notice_layer.view.*

class ChangePWDialog(context: Context) {
    private val dialog = Dialog(context)
    var firebaseAuth: FirebaseAuth?=null
    var fireStore : FirebaseFirestore?=null
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
//        var profileColor : GradientDrawable = dialog.changePW_profileView.background as GradientDrawable

        var accountUId: String? = ""
        accountUId = firebaseAuth?.currentUser?.uid.toString()
        if (accountUId.isNullOrEmpty()) {
            profileName.isVisible=false
//            profileColor.setColor(Color.parseColor("#7175a5"))
        } else {
            fireStore?.collection("Account")?.document(accountUId)?.get()?.addOnSuccessListener {
                var curUser = it.toObject(Account::class.java)
                var userNickname = curUser?.userName.toString()
                var color = curUser?.userColor.toString()
                profileName.text = userNickname[0].toString()
//                if (color != null) {
//                    profileColor.setColor(Color.parseColor(color))
//                } else {
//                    profileColor.setColor(Color.parseColor("#7175a5"))
//                }
            }
        }

        dialog.changePW_Btn.setOnClickListener {
            if (confirmPW.text!=null){
                var curUserId:String
                var curUserEmail:String
                Log.d("confirmPW text", confirmPW.text.toString())
                fireStore?.collection("Account")
                    ?.whereEqualTo("email", confirmPW.text.toString())
                    ?.get()
                    ?.addOnSuccessListener { documents ->
                        for (document in documents) {
                            var curUser = document.toObject(Account::class.java)
                            curUserId = curUser.uid
                            curUserEmail = curUser.email
                            if (accountUId == curUserId || curUserId!=null){
                                firebaseAuth!!.sendPasswordResetEmail(curUserEmail).addOnCompleteListener { task ->
                                    if(task.isSuccessful) {
                                        var snackbar = Snackbar.make(dialogLayout, "비밀번호 재설정 이메일을 성공적으로 보냈습니다.", Snackbar.LENGTH_LONG)
                                        snackbar.show()
                                        dialog.dismiss()
                                    } else {
                                        var snackbar = Snackbar.make(dialogLayout, "이메일 보내기에 실패하였습니다.", Snackbar.LENGTH_LONG)
                                        snackbar.show()
                                    }
                                }
                            } else {
                                var snackbar = Snackbar.make(dialogLayout, "가입된 이메일 주소가 아닙니다.", Snackbar.LENGTH_LONG)
                                snackbar.show()
                            }
                        }
                    }?.addOnFailureListener {
                        var snackbar = Snackbar.make(dialogLayout, "이메일이 유효하지 않습니다.", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }

//                Log.d("accountUId", accountUId.toString())
//                Log.d("curUserId", curUserId.toString())
//                Log.d("curUserEmail", curUserEmail.toString())
            } else {
                var snackbar = Snackbar.make(dialogLayout, "이메일을 입력하세요.", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }
    }

    interface OnDialogClickListener{
        fun onClicked(name: String)
    }
}