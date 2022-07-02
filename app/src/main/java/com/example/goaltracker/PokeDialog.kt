package com.example.goaltracker

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class PokeDialog(context: Context) : Dialog(context) {

    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    private lateinit var close_dialog_button: ImageButton
    private lateinit var view_profile: View
    private lateinit var tv_profileName: TextView
    private lateinit var commentUpload_button:Button


    fun start(profile: GoalTeamData) {
        setContentView(R.layout.poke_dialog)

//        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(com.example.goaltracker.R.layout.poke_dialog)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        close_dialog_button = dlg.findViewById(R.id.close_dialog_button)
        view_profile = dlg.findViewById(R.id.view_profile)
        tv_profileName = dlg.findViewById(R.id.tv_profileName)
        commentUpload_button = dlg.findViewById(R.id.commentUpload_button)

        // 프로필 초기화
        var bgProfile : GradientDrawable = view_profile.background as GradientDrawable
        var bgButton: GradientDrawable = commentUpload_button.background as GradientDrawable

        commentUpload_button.text = "콕 찌르기"
        commentUpload_button.isEnabled = true
        bgButton.setColor(ContextCompat.getColor(context, R.color.dialog_button))

        tv_profileName.text = profile.name[0].toString()
        bgProfile.setColor(ContextCompat.getColor(context, profile.profileColor))

        close_dialog_button.setOnClickListener {
            Toast.makeText(it.context, "You Click Close Button", Toast.LENGTH_SHORT).show()

            dlg.dismiss()
        }

        commentUpload_button.setOnClickListener {
            Toast.makeText(it.context, profile.name + "을 콕 찔렀습니다.", Toast.LENGTH_SHORT).show()

            commentUpload_button.text = "콕 찔렀습니다"
            commentUpload_button.isEnabled = false
            bgButton.setColor(ContextCompat.getColor(context, R.color.greyish_brown))
        }

        dlg.show()
    }

}