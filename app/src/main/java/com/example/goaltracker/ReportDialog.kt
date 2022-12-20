package com.example.goaltracker

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.report_dialog.*


class ReportDialog(
    context: Context,
    val userColor: String?,
    val userName: String?,
    val email: String,
    val uid: String?,
    val userMessage: String?,
    val namebtnListener: View.OnClickListener,
    val messagebtnListener: View.OnClickListener,
    val blockbtnListener: View.OnClickListener,

    ) : Dialog(context
) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //다이얼로그 밖의 화면을 흐리게
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.apply {
            this.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            this.dimAmount = 0.8f
        }
        window?.attributes = layoutParams

        setContentView(R.layout.report_dialog)


        //리사이클러뷰 아이템에서 받은 걸 다이얼로그에 넣어줌
        //이미지 설정
        var circleResource = 0
        when (userColor) {
            "#f69b94" -> circleResource = R.drawable.b_f69b94
            "#f8c8c4" -> circleResource = R.drawable.b_f8c8c4
            "#fcdcce" -> circleResource = R.drawable.b_fcdcce
            "#96b0e5" -> circleResource = R.drawable.b_96b0e5
            "#92b9e2" -> circleResource = R.drawable.b_92b9e2
            "#ebc0c7" -> circleResource = R.drawable.b_ebc0c7
            "#7bb6c8" -> circleResource = R.drawable.b_7bb6c8
            "#aad3d7" -> circleResource = R.drawable.b_aad3d7
            "#f5f1f0" -> circleResource = R.drawable.b_f5f1f0
            "#d5e3e6" -> circleResource = R.drawable.b_d5e3e6
            "#f2a4b1" -> circleResource = R.drawable.b_f2a4b1
            "#7175a5" -> circleResource = R.drawable.b_7175a5
            "#a1b3d7" -> circleResource = R.drawable.b_a1b3d7
            "#bd83cf" -> circleResource = R.drawable.b_bd83cf
            "#e5afe9" -> circleResource = R.drawable.b_e5afe9

        }
        Profile_color_dialog.setImageResource(circleResource)

        //이름 설정

        Profile_name_dialog.text = userName

        //프로필 이름 설정
        profile.text = userName?.get(0).toString()


        //이메일 설정
        Profile_email_dialog.text = email


        Profile_msg_dialog.text = userMessage

        //닉네임 신고 버튼 누를시 이벤트가 해당 페이지로 이동함.
        Report_name_button.setOnClickListener(namebtnListener)

        //상태메시지 신고 버튼 누를시 이벤트가 해당 페이지로 이동함.
        Report_message_button.setOnClickListener(messagebtnListener)

        //차단하기 버튼 누를시 이벤트가 해당 페이지로 이동함.
        Block_name_button.setOnClickListener(blockbtnListener)
    }


}
