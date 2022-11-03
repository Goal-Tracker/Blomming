package com.example.goaltracker

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AnnouncementActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    lateinit var announcementAdapter: AnnouncementAdapter
    lateinit var announcement_list: RecyclerView
    lateinit var backtomain: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        announcement_list = findViewById(R.id.announcement_list)
        backtomain = findViewById(R.id.backtomain)

        backtomain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        announcementAdapter = AnnouncementAdapter(this)
        announcement_list.adapter = announcementAdapter

        db.collection("Announcement")
            .get()
            .addOnSuccessListener { annoucements ->
                val announcementData = ArrayList<AnnouncementData>()
                for (announcement in annoucements) {
                    Log.d(TAG, "${announcement.id} => ${announcement.data}")

                    val subject = announcement.data["subject"].toString()
                    val content = announcement.data["content"].toString()
                    val createDate = announcement.data["createDate"].toString()

                    announcementData.add(
                        AnnouncementData(
                            subject = subject,
                            content = content,
                            createDate = createDate
                        )
                    )
                }

                announcementData.sortBy { it.createDate }
                announcementAdapter.announcementDatas = announcementData
                announcementAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }
}