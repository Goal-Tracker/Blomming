package com.example.goaltracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//class Repo {
//    fun getData(): LiveData<MutableList<Goal>> {
//        val mutableData = MutableLiveData<MutableList<Goal>>()
//        val database = Firebase.database
//        val myRef = database.getReference("Goal")
//        myRef.addValueEventListener(object : ValueEventListener {
//            val listData: MutableList<Goal> = mutableListOf<Goal>()
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    for (userSnapshot in snapshot.children){
//                        val getData = userSnapshot.getValue(Goal::class.java)
//                        listData.add(getData!!)
//
//                        mutableData.value = listData
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//        return mutableData
//    }
//}