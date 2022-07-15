package com.example.goaltracker

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StampBottomSheetFragment(stamp: StampBoardData) : BottomSheetDialogFragment() {

    private val GALLERY = 1

    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언

    private lateinit var certification_default_layout: ConstraintLayout
    private lateinit var certification_default_view: View
    private lateinit var certification_default_textView: TextView

    private lateinit var stamp_recyclerView : RecyclerView
    private lateinit var noneStamp_textView : TextView
    private lateinit var today_stamp_button : Button
    private lateinit var today_stamp_noneStamp_button: Button

    lateinit var todayStampAdapter: TodayStampAdapter
    val todayStampDatas = ArrayList<TodayStampData>()

    val stampInfo = stamp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stamp_bottom_sheet, container, false)

        stamp_recyclerView = view.findViewById(R.id.stamp_recyclerView)
        noneStamp_textView = view.findViewById(R.id.noneStamp_textView)
        today_stamp_button = view.findViewById(R.id.today_stamp_button)
        today_stamp_noneStamp_button = view.findViewById(R.id.today_stamp_noneStamp_button)

        var bgButton : GradientDrawable = today_stamp_button.background as GradientDrawable
        var bgNoneButton: GradientDrawable = today_stamp_noneStamp_button.background as GradientDrawable


        // stmap recyvlerview add
        todayStampAdapter = TodayStampAdapter(requireActivity())
        stamp_recyclerView.adapter = todayStampAdapter

        // stamp db 접근
        val goal_id = stampInfo.goal_id
        val stamp_num = stampInfo.num
        var notYet: Int
        Log.d("User Comment", "stamp_id : $goal_id, stamp_num : $stamp_num")


        val goal_db = db.collection("Goal").document(goal_id)

        goal_db.addSnapshotListener { goal_snapshot, e ->
            val start_day = goal_snapshot?.get("Start_day") as String

            var start_date = start_day + " 00:00:00"
            var sf = SimpleDateFormat("yyyy-MM-dd 00:00:00")
            var date = sf.parse(start_date)
            var today = Calendar.getInstance()

            var calcDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)

            val pastDate = (calcDate+1).toInt()

            // notYet = 0 : 기간 지남
            // notYet = 1 : 오늘
            // notYet = 2 : 아직 기간이 아님
            if (pastDate == stamp_num) {
                notYet = 1
            } else if(pastDate < stamp_num) {
                notYet = 2
            } else {
                notYet = 0
            }

            val stamp_id = goal_snapshot?.get("Stamp_id") as String
            val stamp_db = db.collection("Stamp").document(stamp_id)

            stamp_db.addSnapshotListener { stamp_snapshot, e ->
                try {
                    val day_record = stamp_snapshot?.get("Day_record") as HashMap<String, List<HashMap<String, String>>>

                    val commentArray = day_record["Day$stamp_num"] as List<HashMap<String, String>>

                    Log.d("User Comment day", "User Comment date : " + commentArray.toString())

                    todayStampDatas.apply {
                        for (commentInfo in commentArray) {
                            val comment = commentInfo["Comment"] as String
                            val uid = commentInfo["Uid"] as String

                            val uid_db = db.collection("Account").document(uid)

                            uid_db.addSnapshotListener { uid_snapshot, e ->
                                val name = uid_snapshot?.get("UserName") as String
                                val theme = uid_snapshot?.get("UserColor")
                                var theme_color: Int

                                when (theme) {
                                    "profile_color_lightBlue" -> theme_color = R.color.profile_color_lightBlue
                                    "profile_color_coral" -> theme_color = R.color.profile_color_coral
                                    "profile_color_blue" -> theme_color = R.color.profile_color_blue
                                    "profile_color_babyPink" -> theme_color = R.color.profile_color_babyPink
                                    "profile_color_lightOrange" -> theme_color = R.color.profile_color_lightOrange
                                    else -> theme_color = R.color.profile_color_lightBlue
                                }

                                add(
                                    TodayStampData(
                                        stamp_id = stamp_id,
                                        num = stamp_num,
                                        nickname = name,
                                        theme = theme_color,
                                        comment = comment,
                                        image = ""
                                    )
                                )

                                Log.d("User Comment", "User todayStampDatas : " + todayStampDatas.toString())

                                todayStampAdapter.todayStampDatas = todayStampDatas
                                todayStampAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                } catch (e: Exception){
                    Log.d(ContentValues.TAG, "[Error] $e")
                }

                Log.d("todayStampDatas result : ", todayStampDatas.toString())
            }

            Log.d("stampInfo.stampNum", stampInfo.stampNum.toString())


            if (notYet == 0){ // 이미 기간이 지난 경우
                Log.d("stamp status", "stampInfo.stamp: ${stampInfo.stamp}, notYet: $notYet")

                if (stampInfo.stampNum == 0){   // 스탬프가 없는 경우
                    noneStamp_textView.visibility = View.VISIBLE
                    today_stamp_noneStamp_button.visibility = View.VISIBLE
                    stamp_recyclerView.visibility = View.GONE
                    today_stamp_button.visibility = View.GONE
                    today_stamp_noneStamp_button.isEnabled = true
                    today_stamp_button.isEnabled = false
                    today_stamp_noneStamp_button.text = "기간이 지났습니다"
                    bgNoneButton.setColor(ContextCompat.getColor(requireContext(), R.color.greyish_brown))

                } else {  // 스탬프가 하나라도 있는 경우
                    noneStamp_textView.visibility = View.GONE
                    today_stamp_noneStamp_button.visibility = View.GONE
                    stamp_recyclerView.visibility = View.VISIBLE
                    today_stamp_button.visibility = View.VISIBLE
                    today_stamp_noneStamp_button.isEnabled = false
                    today_stamp_button.isEnabled = true
                    today_stamp_button.text = "기간이 지났습니다"
                    bgNoneButton.setColor(ContextCompat.getColor(requireContext(), R.color.greyish_brown))
                }


            } else if(notYet == 2) {  // 아직 기간이 아닌 경우
                Log.d("stamp status", "stampInfo.stamp: ${stampInfo.stamp}, notYet: $notYet")

                noneStamp_textView.visibility = View.VISIBLE
                today_stamp_noneStamp_button.visibility = View.VISIBLE
                stamp_recyclerView.visibility = View.GONE
                today_stamp_button.visibility = View.GONE
                today_stamp_noneStamp_button.isEnabled = true
                today_stamp_button.isEnabled = false

                today_stamp_noneStamp_button.text = "아직 기간이 아닙니다"
                bgNoneButton.setColor(ContextCompat.getColor(requireContext(), R.color.greyish_brown))
            } else {
                Log.d("stamp status", "stampInfo.stamp: ${stampInfo.stamp}, notYet: $notYet")

                if (stampInfo.stampNum == 0){
                    noneStamp_textView.visibility = View.VISIBLE
                    today_stamp_noneStamp_button.visibility = View.VISIBLE
                    stamp_recyclerView.visibility = View.GONE
                    today_stamp_button.visibility = View.GONE
                    today_stamp_noneStamp_button.isEnabled = true
                    today_stamp_button.isEnabled = false

                    today_stamp_noneStamp_button.text = "오늘의 도장 찍기"
                    bgNoneButton.setColor(ContextCompat.getColor(requireContext(), R.color.dialog_button))

                } else {
                    noneStamp_textView.visibility = View.GONE
                    today_stamp_noneStamp_button.visibility = View.GONE
                    stamp_recyclerView.visibility = View.VISIBLE
                    today_stamp_button.visibility = View.VISIBLE
                    today_stamp_noneStamp_button.isEnabled = false
                    today_stamp_button.isEnabled = true

                    today_stamp_button.text = "도장찍기 완료"
                    bgNoneButton.setColor(ContextCompat.getColor(requireContext(), R.color.greyish_brown))
                }
            }
        }



//        data class TodayStampData(val num : Int, val title : String, val nickname : String, val comment : String, val image : Int)


        Log.d("todayStampDatas result2 : ", todayStampDatas.toString())


        today_stamp_button.setOnClickListener {
            Toast.makeText(requireContext(), "today stamp button click", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, StampUploadDialogActivity::class.java))
        }

        today_stamp_noneStamp_button.setOnClickListener {
            Toast.makeText(requireContext(), "today stamp button click", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, StampUploadDialogActivity::class.java))
        }

        return view
    }
}