package com.blooming.goaltracker

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.deletegoal_dialog.*

class DeleteGoal(
    context: Context,
    val title: String?,
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

        setContentView(R.layout.deletegoal_dialog)

        // 골 이름 받아오기
        delGoal.text = title

        btnDelete.setOnClickListener(deletebtnListener)
        btnClose.setOnClickListener(cancelbtnListener)
    }
}
