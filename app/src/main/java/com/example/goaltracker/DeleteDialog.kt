package com.example.goaltracker

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.deletefriend_dialog.*
import kotlinx.android.synthetic.main.report_dialog.*

class DeleteDialog(
    context: Context,
    val userName: String?,
    val email: String,
    val uid: String?,
    val deletebtnListener: View.OnClickListener,
    val cancelbtnListener: View.OnClickListener

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

        setContentView(R.layout.deletefriend_dialog)


        //리사이클러뷰 아이템에서 받은 걸 다이얼로그에 넣어줌
        //이름 설정
        DeleteName.text = userName

        //이메일 설정
        DeleteEmail.text = email

        //친구 삭제 버튼 누를시 이벤트가 해당 페이지로 이동함.
        btnDeleteOk.setOnClickListener(deletebtnListener)

        btnDeleteX.setOnClickListener(cancelbtnListener)

    }


}
