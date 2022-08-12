package com.example.goaltracker

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.EditText
import kotlinx.android.synthetic.main.profile_setting.*

class CustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    fun showDialog() {
        dialog.setContentView(R.layout.profile_setting)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

//        val edit_profile = dialog.
        val edit_name = dialog.findViewById<EditText>(R.id.pfChangeName)

        dialog.pfChangeButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface OnDialogClickListener{
        fun onClicked(name: String)
    }
}