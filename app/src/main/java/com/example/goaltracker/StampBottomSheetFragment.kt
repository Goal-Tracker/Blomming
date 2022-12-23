package com.example.goaltracker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
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
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언

    private lateinit var stamp_recyclerView : RecyclerView
    private lateinit var noneStamp_textView : TextView
    private lateinit var today_stamp_button : Button
    private lateinit var today_stamp_noneStamp_button: Button
    private lateinit var addPastStamp: LinearLayout


    lateinit var todayStampAdapter: TodayStampAdapter
    val todayStampDatas = ArrayList<TodayStampData>()

    val stampInfo = stamp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("ResourceType")
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
        addPastStamp = view.findViewById(R.id.addPastStamp)

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
            val goal_day = goal_snapshot?.get("day").toString().toInt()

            val start_day = goal_snapshot?.get("startDay") as String

            var start_date = start_day + " 00:00:00"
            var sf = SimpleDateFormat("yyyy-MM-dd 00:00:00")
            var date = sf.parse(start_date)
            var today = Calendar.getInstance()
            var certified = false

            var calcDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)

            val pastDate = (calcDate+1).toInt()

            // notYet = 0 : 기간 지남
            // notYet = 1 : 오늘
            // notYet = 2 : 아직 기간이 아님
            Log.d(TAG, "pastDate : $pastDate")
            Log.d(TAG, "stamp_num : $stamp_num")
            if (pastDate == stamp_num) {
                notYet = 1
            } else if(pastDate < stamp_num) {
                notYet = 2
            } else {
                notYet = 0
            }

            Log.d(TAG, "notYet : $notYet")

            val stamp_id = goal_snapshot?.get("stampId") as String
            val stamp_db = db.collection("Stamp").document(stamp_id)

            stamp_db.addSnapshotListener { stamp_snapshot, e ->
                try {
                    val dayRecord = stamp_snapshot?.get("dayRecord") as HashMap<String, List<HashMap<String, String>>>
                    val commentArray = dayRecord["day$stamp_num"] as List<HashMap<String, String>>

                    Log.d("StampBottomSheetFragment", "commentArray length : ${commentArray.size}")

                    Log.d("User Comment day", "User Comment date : " + commentArray.toString())

                    todayStampDatas.apply {
                        for (commentInfo in commentArray) {
                            val comment = commentInfo["comment"] as String
                            val name = commentInfo["userName"] as String
                            val theme = commentInfo["userColor"] as String
                            val img = commentInfo["image"] as String
                            val type = commentInfo["type"] as Boolean

                            add(
                                TodayStampData(
                                    stamp_id = stamp_id,
                                    num = stamp_num,
                                    nickname = name,
                                    theme = theme,
                                    comment = comment,
                                    image = img,
                                    type = type
                                )
                            )

                            if (name == MySharedPreferences.getUserNickname(requireContext())) {
                                certified = true
                            }
                        }

                        Log.d("User Comment", "User todayStampDatas : " + todayStampDatas.toString())

                        todayStampAdapter.todayStampDatas = todayStampDatas
                        todayStampAdapter.notifyDataSetChanged()
                    }

                    if (!certified && pastDate <= goal_day) {
                        addPastStamp.visibility = View.VISIBLE
                    } else {
                        addPastStamp.visibility = View.GONE
                    }

                    Log.d(TAG, "notYet, if : $notYet")
                    Log.d(TAG, "notYet == 0 : ${notYet == 0}")
                    Log.d(TAG, "notYet == 1 : ${notYet == 1}")
                    Log.d(TAG, "notYet == 2 : ${notYet == 2}")

                    if (notYet == 0){ // 이미 기간이 지난 경우
                        Log.d("stamp status", "notYet: $notYet")

                        // 스탬프가 하나라도 있는 경우
                        noneStamp_textView.visibility = View.GONE
                        today_stamp_noneStamp_button.visibility = View.GONE
                        stamp_recyclerView.visibility = View.VISIBLE
                        today_stamp_button.visibility = View.VISIBLE
                        today_stamp_noneStamp_button.isEnabled = false
                        today_stamp_button.isEnabled = false

                        today_stamp_button.text = "기간이 지났습니다"
                        bgButton.setColor(ContextCompat.getColor(requireContext(), R.color.greyish_brown))

                    } else if(notYet == 1) {  // 오늘인 경우
                        Log.d("stamp status", "notYet: $notYet")

                        addPastStamp.visibility = View.GONE

                        if (certified) {
                            noneStamp_textView.visibility = View.GONE
                            today_stamp_noneStamp_button.visibility = View.GONE
                            stamp_recyclerView.visibility = View.VISIBLE
                            today_stamp_button.visibility = View.VISIBLE
                            today_stamp_noneStamp_button.isEnabled = false
                            today_stamp_button.isEnabled = true

                            today_stamp_button.text = "도장찍기 완료"
                            bgButton.setColor(ContextCompat.getColor(requireContext(), R.color.greyish_brown))

                        } else {
                            noneStamp_textView.visibility = View.GONE
                            today_stamp_noneStamp_button.visibility = View.GONE
                            stamp_recyclerView.visibility = View.VISIBLE
                            today_stamp_button.visibility = View.VISIBLE
                            today_stamp_noneStamp_button.isEnabled = false
                            today_stamp_button.isEnabled = true

                            today_stamp_button.text = "오늘의 도장 찍기"
                            bgButton.setColor(ContextCompat.getColor(requireContext(), MySharedPreferences.getUserColorInt(requireContext())))
                        }
                    }
                } catch (e: Exception){
                    Log.d(ContentValues.TAG, "[Error] $e")

                    if (notYet == 0){ // 이미 기간이 지난 경우
                        Log.d("stamp status", "notYet: $notYet")

                        noneStamp_textView.visibility = View.VISIBLE
                        today_stamp_noneStamp_button.visibility = View.VISIBLE
                        stamp_recyclerView.visibility = View.GONE
                        today_stamp_button.visibility = View.GONE
                        today_stamp_noneStamp_button.isEnabled = false
                        today_stamp_button.isEnabled = false

                        today_stamp_noneStamp_button.text = "기간이 지났습니다"
                        bgNoneButton.setColor(ContextCompat.getColor(requireContext(), R.color.greyish_brown))

                    } else if(notYet == 2) {  // 아직 기간이 아닌 경우
                        Log.d("stamp status", "notYet: $notYet")

                        addPastStamp.visibility = View.GONE

                        noneStamp_textView.visibility = View.VISIBLE
                        today_stamp_noneStamp_button.visibility = View.VISIBLE
                        stamp_recyclerView.visibility = View.GONE
                        today_stamp_button.visibility = View.GONE
                        today_stamp_noneStamp_button.isEnabled = false
                        today_stamp_button.isEnabled = false

                        today_stamp_noneStamp_button.text = "아직 기간이 아닙니다"
                        bgNoneButton.setColor(ContextCompat.getColor(requireContext(), R.color.greyish_brown))

                    } else {
                        Log.d("stamp status", "notYet: $notYet")

                        addPastStamp.visibility = View.GONE

                        noneStamp_textView.visibility = View.VISIBLE
                        today_stamp_noneStamp_button.visibility = View.VISIBLE
                        stamp_recyclerView.visibility = View.GONE
                        today_stamp_button.visibility = View.GONE
                        today_stamp_noneStamp_button.isEnabled = true
                        today_stamp_button.isEnabled = false

                        today_stamp_noneStamp_button.text = "오늘의 도장 찍기"
                        bgNoneButton.setColor(ContextCompat.getColor(requireContext(), MySharedPreferences.getUserColorInt(requireContext())))

                    }
                }

                Log.d("todayStampDatas result : ", todayStampDatas.toString())
            }
        }



//        data class TodayStampData(val num : Int, val title : String, val nickname : String, val comment : String, val image : Int)


        Log.d("todayStampDatas result2 : ", todayStampDatas.toString())


        today_stamp_button.setOnClickListener {
            Toast.makeText(requireContext(), "today stamp button click", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, StampUploadDialogActivity::class.java)
            intent.putExtra("stampInfo", stampInfo)
            intent.putExtra("stampNum", stamp_num)
            intent.putExtra("type", true)
            startActivity(intent)
        }

        today_stamp_noneStamp_button.setOnClickListener {
            Toast.makeText(requireContext(), "today stamp button click", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, StampUploadDialogActivity::class.java)
            intent.putExtra("stampInfo", stampInfo)
            intent.putExtra("stampNum", stamp_num)
            intent.putExtra("type", true)
            startActivity(intent)
        }

        addPastStamp.setOnClickListener {
            Toast.makeText(requireContext(), "addPastStamp", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, StampUploadDialogActivity::class.java)
            intent.putExtra("stampInfo", stampInfo)
            intent.putExtra("stampNum", stamp_num)
            intent.putExtra("type", false)
            startActivity(intent)
        }

        return view
    }
}