package com.blooming.goaltracker

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

class PokeDialog(context: Context) : Dialog(context) {

    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    private lateinit var close_dialog_button: ImageButton
    private lateinit var view_profile: View
    private lateinit var tv_profileName: TextView
    private lateinit var commentUpload_button: Button
    private lateinit var profile_name: TextView
    private lateinit var profile_message: TextView

    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언

    fun start(profile: GoalTeamData, goalId: String, goalTitle: String) {
        setContentView(R.layout.poke_dialog)

        dlg.setContentView(R.layout.poke_dialog)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        close_dialog_button = dlg.findViewById(R.id.close_dialog_button)
        view_profile = dlg.findViewById(R.id.view_profile)
        tv_profileName = dlg.findViewById(R.id.tv_profileName)
        commentUpload_button = dlg.findViewById(R.id.commentUpload_button)
        profile_name = dlg.findViewById(R.id.profile_nick_textView)
        profile_message = dlg.findViewById(R.id.profile_message_textView)

        // 프로필 초기화
        var bgProfile: GradientDrawable = view_profile.background as GradientDrawable
        var bgButton: GradientDrawable = commentUpload_button.background as GradientDrawable

        tv_profileName.text = profile.name[0].toString()
        bgProfile.setColor(Color.parseColor(profile.profileColor))

        profile_name.text = profile.name.toString()
        profile_message.text = profile.message.toString()

        close_dialog_button.setOnClickListener {
            dlg.dismiss()
        }

        db.collection("Stamp")
            .whereEqualTo("goalId", goalId)
            .addSnapshotListener { stamp_snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                try {
                    CoroutineScope(Dispatchers.Main).launch {
                        CoroutineScope(Dispatchers.IO).launch {
                            for (document in stamp_snapshot!!.documents) {
                                val data = document.data
                                val startDay = getStartDay(goalId)
                                val dayRecord = data?.get("dayRecord")

                                if (dayRecord is HashMap<*, *> && dayRecord.isNotEmpty()) {
                                    val castedDayRecord = dayRecord as HashMap<String, List<HashMap<String, String>>>

                                    val isAlreadyStamp = checkIsAlreadyStamp(profile, castedDayRecord, startDay)
                                    setPokeDialog(profile, isAlreadyStamp, bgButton, goalTitle)
                                } else {
                                    setPokeDialog(profile, false, bgButton, goalTitle)
                                    Log.d(TAG, "아직 기록이 없습니다.")
                                }
                            }
                        }.join()
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "[Error] $e")
                }
            }

        dlg.show()
    }

    suspend fun getStartDay(goalId: String): String {
        return withContext(Dispatchers.IO) {
            val document = db.collection("Goal").document(goalId).get().await()
            val startDay = document?.get("startDay").toString()
            startDay
        }
    }

    private fun checkIsAlreadyStamp(
        profile: GoalTeamData,
        dayRecord: HashMap<String, List<HashMap<String, String>>>,
        start_day: String,
    ): Boolean {
        val start_date = start_day
        val sf = SimpleDateFormat("yyyy-MM-dd")
        val date = sf.parse(start_date)
        val today = Calendar.getInstance()

        val calcDate = (today.time.time - date!!.time) / (60 * 60 * 24 * 1000)
        val pastDate = (calcDate + 1).toInt()

        val dayRecordForPastDate = dayRecord["day$pastDate"]

        Log.d(TAG, "dayRecordForPastDate: $dayRecordForPastDate")

        if (dayRecordForPastDate != null) {
            var anyMatchResult = false

            for (map in dayRecordForPastDate) {
                if (map.containsKey("uid") && map["uid"] == profile.uid) {
                    anyMatchResult = true
                    break
                }
            }

            return anyMatchResult
        } else {
            return false
        }
    }

    private fun setPokeDialog(
        profile: GoalTeamData,
        isAlreadyStamp: Boolean,
        bgButton: GradientDrawable,
        goalTitle: String
    ) {
        Log.d(TAG, "isAlreadyStamp: $isAlreadyStamp")

        GlobalScope.launch(Dispatchers.Main) {
            if (profile.uid == MySharedPreferences.getUserId(context)) {
                commentUpload_button.text = "자신입니다"
                commentUpload_button.isEnabled = false
                bgButton.setColor(ContextCompat.getColor(context, R.color.greyish_brown))
            } else if (isAlreadyStamp) {
                commentUpload_button.text = "이미 도장을 찍었습니다."
                commentUpload_button.isEnabled = false
                bgButton.setColor(ContextCompat.getColor(context, R.color.greyish_brown))
            } else {
                commentUpload_button.text = "콕 찌르기"
                commentUpload_button.isEnabled = true
                bgButton.setColor(
                    ContextCompat.getColor(
                        context,
                        MySharedPreferences.getUserColorInt(context)
                    )
                )

                commentUpload_button.setOnClickListener {
                    Toast.makeText(it.context, profile.name + " 님을 콕 찔렀습니다.", Toast.LENGTH_SHORT).show()

                    val notifyData = hashMapOf(
                        "goalName" to goalTitle,
                        "message" to profile.name + " 님을 콕 찔렀습니다.",
                        "read" to false,
                        "type" to 3,
                        "requestUserId" to MySharedPreferences.getUserId(context),
                        "userColor" to MySharedPreferences.getUserColor(context),
                        "userName" to MySharedPreferences.getUserNickname(context),
                        "timestamp" to FieldValue.serverTimestamp()
                    )

                    Log.d(TAG, "poke message : $notifyData")
                    Log.d(TAG, "poke message to : ${profile.uid}")

                    db.collection("Account/${profile.uid}/Notification")
                        .add(notifyData)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                    commentUpload_button.text = "콕 찔렀습니다"
                    commentUpload_button.isEnabled = false
                    bgButton.setColor(ContextCompat.getColor(context, R.color.greyish_brown))
                }
            }
        }
    }
}