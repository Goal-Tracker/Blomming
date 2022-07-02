package com.example.goaltracker

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

import android.view.Window
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import java.security.AccessController.getContext

class StampUploadDialog(context: Context, Interface: StampUploadDialogInterface) : Dialog(context), TodayStampUploadInterface {

    // 액티비티에서 인터페이스를 받아옴
    private var StampUploadDialogInterface: StampUploadDialogInterface = Interface

    private val GALLERY = 1

    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    private lateinit var close_dialog_button : ImageButton
    private lateinit var certImage_imageView : ImageView
    private lateinit var comment_editText : EditText
    private lateinit var commentUpload_button : Button

    fun start() {
        setContentView(R.layout.stamp_upload_dialog)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(com.example.goaltracker.R.layout.stamp_upload_dialog)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        close_dialog_button = dlg.findViewById(com.example.goaltracker.R.id.close_dialog_button)
        certImage_imageView = dlg.findViewById(com.example.goaltracker.R.id.certImage_imageView)
        comment_editText = dlg.findViewById(com.example.goaltracker.R.id.comment_editText)
        commentUpload_button = dlg.findViewById(com.example.goaltracker.R.id.commentUpload_button)

        certImage_imageView.setOnClickListener {
            Toast.makeText(it.context, "You Click Certification Image Button", Toast.LENGTH_SHORT).show()

            // 갤러리 열기
//            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*"
//            StampUploadDialog.this.requireContext().startActivityForResult(intent, GALLERY)

            StampUploadDialogInterface.onUploadImageClicked()
        }

        commentUpload_button.setOnClickListener {
            Toast.makeText(it.context, "You Click Comment Upload Button", Toast.LENGTH_SHORT).show()
            StampUploadDialogInterface.onUploadButtonClicked()

            dlg.dismiss()
        }

        close_dialog_button.setOnClickListener {
            Toast.makeText(it.context, "You Click Close Button", Toast.LENGTH_SHORT).show()

            dlg.dismiss()
        }


        dlg.show()
    }

    override fun onUploadImage(bitmap: Bitmap) {
        certImage_imageView.setImageBitmap(bitmap)
    }
}